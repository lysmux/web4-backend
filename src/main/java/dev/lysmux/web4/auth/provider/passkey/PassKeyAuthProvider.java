package dev.lysmux.web4.auth.provider.passkey;

import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.util.JsonConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.*;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.verifier.exception.VerificationException;
import dev.lysmux.web4.UUIDUtils;
import dev.lysmux.web4.auth.provider.passkey.exception.InvalidKeyException;
import dev.lysmux.web4.auth.provider.passkey.model.*;
import dev.lysmux.web4.auth.provider.passkey.repository.PassKeyChallengeRepository;
import dev.lysmux.web4.auth.provider.passkey.repository.PassKeyCredentialsRepository;
import dev.lysmux.web4.domain.model.User;
import dev.lysmux.web4.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class PassKeyAuthProvider {
    @ConfigProperty(name = "app.domain")
    String rpId;

    @ConfigProperty(name = "app.name")
    String rpName;

    @ConfigProperty(name = "app.origin")
    String origin;

    private static final JsonConverter jsonConverter = new ObjectConverter().getJsonConverter();
    private static final WebAuthnManager webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();

    private static final List<PublicKeyCredentialParameters> ALLOWED_PUB_KEYS = List.of(
            new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256)
    );

    private static final UserVerificationRequirement USER_VERIFICATION_REQUIREMENT = UserVerificationRequirement.PREFERRED;

    private static final AuthenticatorSelectionCriteria AUTHENTICATOR_SELECTION = new AuthenticatorSelectionCriteria(
            AuthenticatorAttachment.PLATFORM,
            true,
            USER_VERIFICATION_REQUIREMENT
    );

    @Inject
    UserService userService;

    @Inject
    PassKeyChallengeRepository challengeRepository;

    @Inject
    PassKeyCredentialsRepository credentialsRepository;

    public PassKeyRegisterStart registerStart(UUID userId) {
        User user = userService.getById(userId);

        PassKeyChallenge challenge = new PassKeyChallenge();
        challengeRepository.addChallenge(challenge);

        byte[] userHandle = UUIDUtils.convertUUIDToBytes(userId);
        PublicKeyCredentialUserEntity userIdentity = new PublicKeyCredentialUserEntity(
                userHandle,
                user.getEmail(),
                user.getEmail()
        );

        List<PublicKeyCredentialDescriptor> excludeCredentials = credentialsRepository.getUserCredentials(user.getId())
                .stream()
                .map(cr -> new PublicKeyCredentialDescriptor(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        cr.getCredentialId(),
                        null
                ))
                .toList();

        PublicKeyCredentialCreationOptions options = new PublicKeyCredentialCreationOptions(
                new PublicKeyCredentialRpEntity(rpId, rpName),
                userIdentity,
                challenge.toChallenge(),
                ALLOWED_PUB_KEYS,
                60_000L,
                excludeCredentials,
                AUTHENTICATOR_SELECTION,
                AttestationConveyancePreference.NONE,
                null
        );
        return new PassKeyRegisterStart(challenge.getOperationId(), jsonConverter.writeValueAsString(options));
    }

    @Transactional
    public void registerFinish(UUID userId, PassKeyRegisterFinishRequest request) {
        RegistrationData registrationData = webAuthnManager.parseRegistrationResponseJSON(request.registerResponseJSON().toString());
        PassKeyChallenge challenge = challengeRepository.getChallenge(request.operationId());
        if (challenge == null) {
            throw new InvalidKeyException();
        }

        RegistrationParameters registrationParameters = new RegistrationParameters(
                getServerProperty(challenge),
                ALLOWED_PUB_KEYS,
                true,
                true
        );

        try {
            webAuthnManager.verify(registrationData, registrationParameters);
        } catch (VerificationException e) {
            throw new InvalidKeyException();
        }

        CredentialRecord credentialRecord = new CredentialRecordImpl(
                registrationData.getAttestationObject(),
                registrationData.getCollectedClientData(),
                registrationData.getClientExtensions(),
                registrationData.getTransports()
        );

        PassKeyCredential credential = PassKeyCredential.builder()
                .userId(userId)
                .credentialId(credentialRecord.getAttestedCredentialData().getCredentialId())
                .publicKey(credentialRecord.getAttestedCredentialData().getCOSEKey().getPublicKey().getEncoded())
                .aaguId(credentialRecord.getAttestedCredentialData().getAaguid().getBytes())
                .signCount(credentialRecord.getCounter())
                .build();
        credentialsRepository.addCredential(credential);
    }

    public PassKeyLoginStart loginStart() {
        PassKeyChallenge challenge = new PassKeyChallenge();
        challengeRepository.addChallenge(challenge);

        PublicKeyCredentialRequestOptions options = new PublicKeyCredentialRequestOptions(
                challenge.toChallenge(),
                600L,
                rpId,
                null,
                USER_VERIFICATION_REQUIREMENT,
                null
        );

        return new PassKeyLoginStart(
                challenge.getOperationId(),
                jsonConverter.writeValueAsString(options)
        );
    }

    public User loginFinish(PassKeyLoginFinishRequest request) {
        AuthenticationData authenticationData = webAuthnManager.parseAuthenticationResponseJSON(request.loginResponseJSON().toString());

        PassKeyChallenge challenge = challengeRepository.getChallenge(request.operationId());
        if (challenge == null) {
            throw new InvalidKeyException();
        }

        PassKeyCredential passKeyCredential = credentialsRepository.getCredential(authenticationData.getCredentialId());
        if (passKeyCredential == null) {
            throw new InvalidKeyException();
        }

        AuthenticationParameters authenticationParameters =
                new AuthenticationParameters(
                        getServerProperty(challenge),
                        new PassKeyCredentialAdapter(passKeyCredential),
                        null,
                        true,
                        true
                );

        try {
            webAuthnManager.verify(authenticationData, authenticationParameters);
        } catch (VerificationException e) {
            throw new InvalidKeyException();
        }

        return userService.getById(passKeyCredential.getUserId());
    }

    private ServerProperty getServerProperty(PassKeyChallenge challenge) {
        return ServerProperty.builder()
                .origin(new Origin(origin))
                .rpId(rpId)
                .challenge(challenge.toChallenge())
                .build();
    }
}

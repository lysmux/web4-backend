package dev.lysmux.web4.auth.provider.passkey;

import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.data.attestation.authenticator.AAGUID;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.attestation.authenticator.EC2COSEKey;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.CollectedClientData;
import dev.lysmux.web4.auth.provider.passkey.model.PassKeyCredential;
import lombok.AllArgsConstructor;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@AllArgsConstructor
public class PassKeyCredentialAdapter implements CredentialRecord {
    private final PassKeyCredential credential;

    @Override
    public CollectedClientData getClientData() {
        return null;
    }

    @Override
    public Boolean isUvInitialized() {
        return null;
    }

    @Override
    public void setUvInitialized(boolean value) {

    }

    @Override
    public Boolean isBackupEligible() {
        return null;
    }

    @Override
    public void setBackupEligible(boolean value) {

    }

    @Override
    public Boolean isBackedUp() {
        return null;
    }

    @Override
    public void setBackedUp(boolean value) {

    }

    @Override
    public AttestedCredentialData getAttestedCredentialData() {
        ECPublicKey key;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(credential.getPublicKey());
            key = (ECPublicKey) keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        EC2COSEKey coseKey = EC2COSEKey.create(key, COSEAlgorithmIdentifier.ES256);

        return new AttestedCredentialData(
                new AAGUID(credential.getAaguId()),
                credential.getCredentialId(),
                coseKey
        );
    }

    @Override
    public long getCounter() {
        return credential.getSignCount();
    }

    @Override
    public void setCounter(long value) {

    }
}

package dev.lysmux.web4.auth.provider.passkey.model;

import com.github.f4b6a3.uuid.UuidCreator;
import com.webauthn4j.data.client.challenge.Challenge;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Value
@AllArgsConstructor
public class PassKeyChallenge {
    UUID operationId = UuidCreator.getTimeOrdered();

    byte[] challenge;

    Instant createdAt = Instant.now();

    public PassKeyChallenge() {
        challenge = new byte[32];
        new SecureRandom().nextBytes(challenge);
    }

    public Challenge toChallenge() {
        return () -> Arrays.copyOf(challenge, challenge.length);
    }
}

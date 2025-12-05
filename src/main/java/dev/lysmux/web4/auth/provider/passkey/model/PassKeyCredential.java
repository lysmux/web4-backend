package dev.lysmux.web4.auth.provider.passkey.model;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class PassKeyCredential {
    @Builder.Default
    UUID id = UuidCreator.getTimeOrdered();;

    UUID userId;

    byte[] credentialId;

    byte[] publicKey;

    byte[] aaguId;

    long signCount;

    @Builder.Default
    Instant createdAt = Instant.now();
}

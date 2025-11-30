package dev.lysmux.web4.auth.provider.password.model;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordCredentials {
    private UUID userId;

    private String password;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();

    public static PasswordCredentials withPassword(UUID userId, String password) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        return PasswordCredentials.builder()
                .userId(userId)
                .password(hashedPassword)
                .build();
    }

    public static PasswordCredentials withHashedPassword(UUID userId, String hashedPassword) {
        return PasswordCredentials.builder()
                .userId(userId)
                .password(hashedPassword)
                .build();
    }

    public boolean verifyPassword(String password) {
        return BCrypt.verifyer().verify(password.toCharArray(), this.password).verified;
    }
}

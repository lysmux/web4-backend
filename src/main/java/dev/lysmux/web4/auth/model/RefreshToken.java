package dev.lysmux.web4.auth.model;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Builder.Default
    private UUID id = UuidCreator.getTimeOrdered();

    private UUID userId;

    private boolean revoked;

    @Builder.Default
    private Instant createdAt = Instant.now();
}

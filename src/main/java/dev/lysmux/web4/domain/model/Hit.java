package dev.lysmux.web4.domain.model;

import com.github.f4b6a3.uuid.UuidCreator;
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
public class Hit {
    @Builder.Default
    private UUID id = UuidCreator.getTimeOrdered();

    private UUID ownerId;

    private double x;

    private double y;

    private double r;

    private boolean hit;

    private long executionTime;

    @Builder.Default
    private Instant createdAt = Instant.now();
}

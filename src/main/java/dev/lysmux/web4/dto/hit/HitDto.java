package dev.lysmux.web4.dto.hit;

import lombok.Builder;

import java.util.UUID;

@Builder
public record HitDto (
        UUID id,
        double x,
        double y,
        double r,
        boolean hit,
        long executionTime,
        long createdAt
) {
}

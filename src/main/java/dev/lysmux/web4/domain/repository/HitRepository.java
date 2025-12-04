package dev.lysmux.web4.domain.repository;

import dev.lysmux.web4.domain.model.Hit;

import java.util.List;
import java.util.UUID;

public interface HitRepository {
    void addHit(Hit hit);

    List<Hit> getHits();

    Hit getHit(UUID id);

    List<Hit> getUserHits(UUID userId, int limit, int offset);

    void clearUserHits(UUID userId);

    long countByUser(UUID userId);
}

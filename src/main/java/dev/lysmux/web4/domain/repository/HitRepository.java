package dev.lysmux.web4.domain.repository;

import dev.lysmux.web4.domain.model.Hit;

import java.util.List;
import java.util.UUID;

public interface HitRepository {
    void addHit(Hit hit);

    List<Hit> getHits();

    Hit getHit(UUID id);

    List<Hit> getUserHits(UUID userId);

    void clearUserHits(UUID userId);
}

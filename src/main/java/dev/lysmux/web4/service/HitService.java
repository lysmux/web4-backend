package dev.lysmux.web4.service;

import dev.lysmux.web4.domain.model.Hit;
import dev.lysmux.web4.domain.checker.HitChecker;
import dev.lysmux.web4.domain.repository.HitRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Slf4j
public class HitService {
    @Inject
    @Any
    Instance<HitChecker> checkers;

    @Inject
    HitRepository repository;

    @Transactional
    public Hit checkHit(UUID userId, double x, double y, double r) {
        long startTime = System.nanoTime();

        boolean hit = checkers.stream()
                .anyMatch(checker -> checker.contains(x, y, r));
        Hit result = Hit.builder()
                .ownerId(userId)
                .x(x)
                .y(y)
                .r(r)
                .hit(hit)
                .executionTime(System.nanoTime() - startTime)
                .build();
        repository.addHit(result);

        return result;
    }

    public List<Hit> getUserHits(UUID userId) {
        return repository.getUserHits(userId);
    }

    @Transactional
    public void clearUserHits(UUID userId) {
        repository.clearUserHits(userId);
    }
}

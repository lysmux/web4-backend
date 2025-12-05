package dev.lysmux.web4.service;

import dev.lysmux.web4.domain.model.Hit;
import dev.lysmux.web4.domain.checker.HitChecker;
import dev.lysmux.web4.domain.repository.HitRepository;
import dev.lysmux.web4.dto.PaginationDto;
import dev.lysmux.web4.dto.hit.HitDto;
import dev.lysmux.web4.dto.hit.HitMapper;
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

    @Inject
    HitMapper mapper;

    @Transactional
    public HitDto checkHit(UUID userId, double x, double y, double r) {
        long startTime = System.nanoTime();

        boolean hit = checkers.stream()
                .anyMatch(checker -> checker.contains(x, y, r));
        Hit result = Hit.builder()
                .ownerId(userId)
                .x(x)
                .y(y)
                .r(r)
                .hit(hit)
                .executionTime((System.nanoTime() - startTime) / 1000)
                .build();
        repository.addHit(result);

        return mapper.toDto(result);
    }

    public PaginationDto<List<HitDto>> getUserHits(UUID userId, int page, int perPage) {
        long totalHits = repository.countByUser(userId);
        boolean hasNextPage = totalHits > (long) page * perPage;
        List<HitDto> hits = repository.getUserHits(userId, perPage, (page - 1) * perPage).stream()
                .map(mapper::toDto)
                .toList();

        return new PaginationDto<>(totalHits, hasNextPage, hits);
    }

    @Transactional
    public void clearUserHits(UUID userId) {
        repository.clearUserHits(userId);
    }
}

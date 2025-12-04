package dev.lysmux.web4.db.hit;

import dev.lysmux.web4.db.user.UserEntity;
import dev.lysmux.web4.domain.model.Hit;
import dev.lysmux.web4.domain.model.User;
import dev.lysmux.web4.domain.repository.HitRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HitRepositoryJPA implements PanacheRepository<HitEntity>, HitRepository {
    @Inject
    HitMapper mapper;

    @Override
    public void addHit(Hit hit) {
        UserEntity ownerRef = getEntityManager().getReference(UserEntity.class, hit.getOwnerId());
        persist(mapper.toEntity(hit, ownerRef));
    }

    @Override
    public List<Hit> getHits() {
        return findAll().stream()
                .map(mapper::fromEntity)
                .toList();
    }

    @Override
    public Hit getHit(UUID id) {
        return find("id", id).firstResultOptional()
                .map(mapper::fromEntity)
                .orElse(null);
    }

    @Override
    public List<Hit> getUserHits(UUID userId, int limit, int offset) {
        return find("owner.id", userId)
                .range(offset, offset + limit - 1).stream()
                .map(mapper::fromEntity)
                .toList();
    }

    @Override
    public void clearUserHits(UUID userId) {
        delete("owner.id", userId);
    }

    @Override
    public long countByUser(UUID userId) {
        return count("owner.id", userId);
    }
}

package dev.lysmux.web4.db.auth.refreshtoken;

import dev.lysmux.web4.auth.RefreshTokenRepository;
import dev.lysmux.web4.auth.model.RefreshToken;
import dev.lysmux.web4.db.user.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class JPARefreshTokenRepository implements PanacheRepository<RefreshTokenEntity>, RefreshTokenRepository {
    @Inject
    RefreshTokenMapper mapper;

    @Override
    public void addToken(RefreshToken token) {
        UserEntity userRef = getEntityManager().getReference(UserEntity.class, token.getUserId());
        persist(mapper.toEntity(token, userRef));
    }

    @Override
    public void revokeToken(UUID tokenId) {
        update("revoked = true where id = ?1", tokenId);
    }

    @Override
    public RefreshToken getTokenById(UUID tokenId) {
        return find("id", tokenId).firstResultOptional()
                .map(mapper::fromEntity)
                .orElse(null);
    }
}

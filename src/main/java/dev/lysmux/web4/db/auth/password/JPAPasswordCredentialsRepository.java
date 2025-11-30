package dev.lysmux.web4.db.auth.password;

import dev.lysmux.web4.auth.provider.password.model.PasswordCredentials;
import dev.lysmux.web4.auth.provider.password.repository.PasswordCredentialsRepository;
import dev.lysmux.web4.db.user.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class JPAPasswordCredentialsRepository implements PanacheRepository<PasswordCredentialsEntity>, PasswordCredentialsRepository {
    @Inject
    PasswordCredentialsMapper mapper;

    @Override
    public void addCredential(PasswordCredentials credentials) {
        UserEntity userRef = getEntityManager().getReference(UserEntity.class, credentials.getUserId());
        persist(mapper.toEntity(credentials, userRef));
    }

    @Override
    public PasswordCredentials getByUserId(UUID userId) {
        return find("user.id", userId).firstResultOptional()
                .map(mapper::fromEntity)
                .orElse(null);
    }
}

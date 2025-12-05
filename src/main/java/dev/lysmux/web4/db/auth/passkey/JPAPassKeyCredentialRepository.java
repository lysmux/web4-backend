package dev.lysmux.web4.db.auth.passkey;

import dev.lysmux.web4.auth.provider.passkey.model.PassKeyCredential;
import dev.lysmux.web4.auth.provider.passkey.repository.PassKeyCredentialsRepository;
import dev.lysmux.web4.db.user.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class JPAPassKeyCredentialRepository implements PanacheRepository<PassKeyCredentialEntity>, PassKeyCredentialsRepository {
    @Inject
    PassKeyCredentialsMapper mapper;

    @Override
    public void addCredential(PassKeyCredential credential) {
        UserEntity userRef = getEntityManager().getReference(UserEntity.class, credential.getUserId());
        persist(mapper.toEntity(credential, userRef));
    }

    @Override
    public PassKeyCredential getCredential(byte[] id) {
        return find("credentialId", (Object) id).firstResultOptional()
                .map(mapper::fromEntity)
                .orElse(null);
    }

    @Override
    public List<PassKeyCredential> getUserCredentials(UUID userId) {
        return find("user.id", userId).stream()
                .map(mapper::fromEntity)
                .toList();
    }
}

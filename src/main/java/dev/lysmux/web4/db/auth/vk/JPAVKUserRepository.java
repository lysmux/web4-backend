package dev.lysmux.web4.db.auth.vk;

import dev.lysmux.web4.auth.provider.vk.model.VKUser;
import dev.lysmux.web4.auth.provider.vk.repository.VKAuthRepository;
import dev.lysmux.web4.db.user.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JPAVKUserRepository implements PanacheRepository<VKUserEntity>, VKAuthRepository {
    @Inject
    VKUserMapper mapper;

    @Override
    public void addUser(VKUser user) {
        UserEntity userRef = getEntityManager().getReference(UserEntity.class, user.getUserId());
        persist(mapper.toEntity(user, userRef));
    }

    @Override
    public VKUser getUser(long vkId) {
        return find("vkId", vkId).firstResultOptional()
                .map(mapper::fromEntity)
                .orElse(null);
    }
}

package dev.lysmux.web4.db.user;

import dev.lysmux.web4.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {
    @Inject
    UserMapper mapper;

    public void addUser(User user) {
        persist(mapper.toEntity(user));
    }

    public User getUserById(UUID id) {
        return find("id", id).firstResultOptional()
                .map(mapper::fromEntity)
                .orElse(null);
    }

    public User getByUsername(String username) {
        return find("username", username).firstResultOptional()
                .map(mapper::fromEntity)
                .orElse(null);
    }

    public User getByEmail(String email) {
        return find("email", email).firstResultOptional()
                .map(mapper::fromEntity)
                .orElse(null);
    }

    public boolean isUsernameExists(String username) {
        return count("username", username) > 0;
    }

    public boolean isEmailExists(String email) {
        return count("email", email) > 0;
    }

    public void update(User user) {
        getEntityManager().merge(mapper.toEntity(user));
    }
}

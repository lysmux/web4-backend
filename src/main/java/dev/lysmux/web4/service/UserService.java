package dev.lysmux.web4.service;

import dev.lysmux.web4.db.user.UserRepository;
import dev.lysmux.web4.domain.model.User;
import dev.lysmux.web4.domain.model.UserStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository repository;

    @Inject
    UserConfirmationService confirmationService;

    @Transactional
    public User createUser(
            String username,
            String email
    ) {
        if (repository.isUsernameExists(username)) {
            throw new UsernameExistsException(username);
        }

        if (repository.isEmailExists(email)) {
            throw new EmailExistsException(email);
        }

        User user = User.builder()
                .email(email)
                .username(username)
                .status(UserStatus.PENDING)
                .build();

        repository.addUser(user);

        confirmationService.sendConfirmationEmail(user);

        return user;
    }

    @Transactional
    public User createConfirmedUser(
            String username,
            String email
    ) {
        User user = User.builder()
                .email(email)
                .username(username)
                .build();

        repository.addUser(user);

        return user;
    }

    public User getById(UUID id) {
        User user = repository.getUserById(id);
        if (user == null) {
            throw UserNotFoundException.ofId(id);
        };

        return user;
    }

    public User getByUsername(String username) {
        User user = repository.getByUsername(username);
        if (user == null) {
            throw UserNotFoundException.ofUsername(username);
        };

        return user;
    }

    public User getByEmail(String email) {
        User user = repository.getByEmail(email);
        if (user == null) {
            throw UserNotFoundException.ofEmail(email);
        };

        return user;
    }
}

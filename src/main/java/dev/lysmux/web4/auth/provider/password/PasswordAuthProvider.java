package dev.lysmux.web4.auth.provider.password;


import dev.lysmux.web4.auth.exception.PendingConfirmationException;
import dev.lysmux.web4.auth.provider.password.exception.InvalidCredentialsException;
import dev.lysmux.web4.auth.provider.password.model.PasswordCredentials;
import dev.lysmux.web4.auth.provider.password.repository.PasswordCredentialsRepository;
import dev.lysmux.web4.domain.model.User;
import dev.lysmux.web4.domain.model.UserStatus;
import dev.lysmux.web4.service.UserNotFoundException;
import dev.lysmux.web4.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.regex.Pattern;

@ApplicationScoped
public class PasswordAuthProvider {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Inject
    UserService userService;

    @Inject
    PasswordCredentialsRepository repository;

    @Transactional
    public User register(String username, String email, String password) {
        User user = userService.createUser(username, email);
        PasswordCredentials pwdUser = PasswordCredentials.withPassword(user.getId(), password);

        repository.addCredential(pwdUser);

        return user;
    }

    public User login(String usernameOrEmail, String password) {
        User user = null;

        try {
            if (EMAIL_PATTERN.matcher(usernameOrEmail).matches()) {
                user = userService.getByEmail(usernameOrEmail);
            } else {
                user = userService.getByUsername(usernameOrEmail);
            }
        } catch (UserNotFoundException e) {
            throw new InvalidCredentialsException();
        }

        PasswordCredentials passwordUser = repository.getByUserId(user.getId());
        if (passwordUser != null && passwordUser.verifyPassword(password)) {
            if (user.getStatus() == UserStatus.ACTIVE) return user;
            else if (user.getStatus() == UserStatus.PENDING) throw new PendingConfirmationException();
            else throw new InvalidCredentialsException();
        }

        throw new InvalidCredentialsException();
    }

    public void resetPassword(String email) {}
}

package dev.lysmux.web4.auth.provider.password;


import dev.lysmux.web4.auth.provider.password.exception.InvalidCredentialsException;
import dev.lysmux.web4.auth.provider.password.model.PasswordCredentials;
import dev.lysmux.web4.auth.provider.password.repository.PasswordCredentialsRepository;
import dev.lysmux.web4.domain.model.User;
import dev.lysmux.web4.domain.model.UserStatus;
import dev.lysmux.web4.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PasswordAuthProvider {
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

    public User login(String username, String email, String password) {
        User user = null;

        if (username != null) {
            user = userService.getByUsername(username);
        } else if (email != null) {
            user = userService.getByEmail(email);
        }

        if (user == null) {
            throw new InvalidCredentialsException();

        }

        PasswordCredentials passwordUser = repository.getByUserId(user.getId());
        if (passwordUser != null && passwordUser.verifyPassword(password)) {
            if (user.getStatus() == UserStatus.ACTIVE) return user;
            else throw new InvalidCredentialsException();
        }

        throw new InvalidCredentialsException();
    }

    public void resetPassword(String email) {}
}

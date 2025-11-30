package dev.lysmux.web4.service;

import dev.lysmux.web4.JWTHelper;
import dev.lysmux.web4.db.user.UserRepository;
import dev.lysmux.web4.domain.model.User;
import dev.lysmux.web4.domain.model.UserStatus;
import io.jsonwebtoken.JwtException;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class UserConfirmationService {
    @ConfigProperty(name = "app.confirmation.link")
    String confirmationLink;

    @Inject
    Mailer reactiveMailer;

    @Inject
    Template emailConfirmation;

    @Inject
    UserRepository repository;

    @Inject
    JWTHelper jwtHelper;

    public void sendConfirmationEmail(User user) {
        String confirmationToken = jwtHelper.generateJWT(
                user.getId().toString(),
                new ConfirmationData(user.getEmail()),
                1000 * 60 * 60 * 24
        );

        TemplateInstance template = emailConfirmation
                .data("username", user.getUsername())
                .data("confirmationLink", confirmationLink.formatted(confirmationToken));

        reactiveMailer.send(
                Mail.withHtml(user.getEmail(), "Подтверждение аккаунта", template.render())
        );

    }

    public void resendConfirmationEmail(User user) {
        if (user.getStatus() != UserStatus.PENDING) {
            throw new UserAlreadyConfirmed();
        }

        sendConfirmationEmail(user);
    }

    @Transactional
    public User confirmEmail(String token) {
        JWTHelper.JWTData<ConfirmationData> jwtData;

        try {
            jwtData = jwtHelper.parseJWT(token, ConfirmationData.class);
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }


        User user = repository.getByEmail(jwtData.data().email());
        if (user == null) {
            throw new InvalidTokenException();
        }

        if (user.getStatus() == UserStatus.PENDING) {
            user.setStatus(UserStatus.ACTIVE);
            repository.update(user);
        }

        return user;
    }

    record ConfirmationData(String email) {
    }

}

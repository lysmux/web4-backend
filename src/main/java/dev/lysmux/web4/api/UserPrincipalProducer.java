package dev.lysmux.web4.api;

import dev.lysmux.web4.auth.UserPrincipal;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.SecurityContext;

@ApplicationScoped
public class UserPrincipalProducer {

    @Inject
    SecurityContext securityContext;

    @Produces
    @RequestScoped
    public UserPrincipal produceUserPrincipal() {
        return (UserPrincipal) securityContext.getUserPrincipal();
    }
}
package dev.lysmux.web4.api;

import dev.lysmux.web4.auth.UserPrincipal;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AllArgsConstructor;

import java.security.Principal;

@AllArgsConstructor
public class AppSecurityContext implements SecurityContext {
    private UserPrincipal user;

    private boolean isSecure;

    private String scheme;

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return scheme;
    }
}

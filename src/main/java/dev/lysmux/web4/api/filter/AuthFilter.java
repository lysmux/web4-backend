package dev.lysmux.web4.api.filter;

import dev.lysmux.web4.api.AppSecurityContext;
import dev.lysmux.web4.auth.AuthService;
import dev.lysmux.web4.auth.UserPrincipal;
import dev.lysmux.web4.auth.exception.AuthException;
import dev.lysmux.web4.auth.exception.InvalidTokenException;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Priority(Priorities.AUTHENTICATION)
@Slf4j
public class AuthFilter implements ContainerRequestFilter {
    @Inject
    AuthService authService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        UserPrincipal user = null;

        String headerToken = requestContext.getHeaderString("Authorization");
        try {
            user = validateToken(headerToken);
        } catch (AuthException e) {
            log.warn("Invalid JWT token");
        }

        boolean isSecure = requestContext.getSecurityContext().isSecure();
        SecurityContext securityContext = new AppSecurityContext(user, isSecure, "JWT");
        requestContext.setSecurityContext(securityContext);
    }

    private UserPrincipal validateToken(String headerToken) {
        if (headerToken == null) {
            throw new InvalidTokenException("Authorization header is missing");
        }

        AuthToken token = parseAuthToken(headerToken);
        if (!token.schema().equals("Bearer")) {
            throw new InvalidTokenException("Invalid token schema. Expected 'Bearer'");
        }

        return authService.validateAccessToken(token.token());
    }

    private AuthToken parseAuthToken(String headerToken) {
        String[] parts = headerToken.split(" ", 2);
        if (parts.length != 2) {
            throw new InvalidTokenException("Invalid Authorization header format");
        }

        String schema = parts[0].trim();
        String token = parts[1].trim();

        if (schema.isEmpty() || token.isEmpty()) {
            throw new InvalidTokenException("Schema or token is empty");
        }

        return new AuthToken(schema, token);
    }

    record AuthToken(String schema, String token) {}
}

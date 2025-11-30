package dev.lysmux.web4.api.schema.auth;

import dev.lysmux.web4.auth.model.Token;

public record AuthResponse(
        String accessToken,
        int expiresIn
) {
    public static AuthResponse of(Token token) {
        return new AuthResponse(token.token(), token.expiresIn());
    }
}

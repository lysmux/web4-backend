package dev.lysmux.web4.api;

import dev.lysmux.web4.auth.model.Token;
import jakarta.ws.rs.core.NewCookie;

public class TokenUtil {
    public static final String COOKIE_NAME = "refreshToken";

    public static NewCookie makeRefreshCookie(Token refreshToken) {
        return new NewCookie.Builder(COOKIE_NAME)
                .value(refreshToken.token())
                .maxAge(refreshToken.expiresIn() / 1000)
                .path("/")
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.LAX)
                .build();
    }

    public static NewCookie clearRefreshCookie() {
        return new NewCookie.Builder(COOKIE_NAME)
                .maxAge(0)
                .build();
    }
}

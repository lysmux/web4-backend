package dev.lysmux.web4.auth.model;

public record Token(
        String token,
        int expiresIn
) {
}

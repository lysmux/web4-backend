package dev.lysmux.web4.auth.model;

public record TokensPair(
        Token accessToken,
        Token refreshToken
) {
}

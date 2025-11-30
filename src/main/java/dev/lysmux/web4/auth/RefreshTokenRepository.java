package dev.lysmux.web4.auth;

import dev.lysmux.web4.auth.model.RefreshToken;

import java.util.UUID;

public interface RefreshTokenRepository {
    void addToken(RefreshToken token);

    void revokeToken(UUID tokenId);

    RefreshToken getTokenById(UUID tokenId);
}

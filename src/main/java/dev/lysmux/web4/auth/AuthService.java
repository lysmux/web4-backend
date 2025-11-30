package dev.lysmux.web4.auth;

import dev.lysmux.web4.JWTHelper;
import dev.lysmux.web4.auth.exception.InvalidTokenException;
import dev.lysmux.web4.auth.model.RefreshToken;
import dev.lysmux.web4.auth.model.Token;
import dev.lysmux.web4.auth.model.TokensPair;
import dev.lysmux.web4.domain.model.User;
import io.jsonwebtoken.JwtException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class AuthService {
    private final static int ACCESS_TOKEN_EXPIRATION_MS = 1000 * 60 * 5; // 5 min
    private final static int REFRESH_TOKEN_EXPIRATION_MS = 1000 * 60 * 60 * 24 * 7; // 7 days

    @Inject
    JWTHelper jwtHelper;

    @Inject
    RefreshTokenRepository tokensRepository;

    @Transactional
    public TokensPair generateTokensPair(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        return new TokensPair(
                new Token(accessToken, ACCESS_TOKEN_EXPIRATION_MS),
                new Token(refreshToken, REFRESH_TOKEN_EXPIRATION_MS)
        );
    }

    public UserPrincipal validateAccessToken(String token) {
        JWTHelper.JWTData<AccessTokenData> jwtData;

        try {
            jwtData = jwtHelper.parseJWT(token, AccessTokenData.class);
        } catch (JwtException e) {
            throw new InvalidTokenException("Cannot parse JWT token");
        }

        return UserPrincipal.builder()
                .id(UUID.fromString(jwtData.subject()))
                .username(jwtData.data().username())
                .email(jwtData.data().email())
                .build();
    }

    private String generateAccessToken(User user) {
        AccessTokenData tokenData = new AccessTokenData(user.getUsername(), user.getEmail());
        return jwtHelper.generateJWT(user.getId().toString(), tokenData, ACCESS_TOKEN_EXPIRATION_MS);
    }

    private String generateRefreshToken(User user) {
        RefreshToken token = RefreshToken.builder()
                .userId(user.getId())
                .revoked(false)
                .build();
        tokensRepository.addToken(token);

        RefreshTokenData tokenData = new RefreshTokenData(user.getId(), user.getUsername(), user.getEmail());
        return jwtHelper.generateJWT(token.getId().toString(), tokenData, REFRESH_TOKEN_EXPIRATION_MS);
    }

    @Transactional
    public TokensPair refreshTokensPair(String token) {
        JWTHelper.JWTData<RefreshTokenData> jwtData;
        try {
            jwtData = jwtHelper.parseJWT(token, RefreshTokenData.class);
        } catch (JwtException e) {
            throw new InvalidTokenException("Cannot parse JWT token");
        }

        UUID tokenId = UUID.fromString(jwtData.subject());
        UUID userId = jwtData.data().userId();

        RefreshToken refreshToken = tokensRepository.getTokenById(tokenId);

        if (refreshToken == null || refreshToken.isRevoked()) {
            throw new InvalidTokenException("Invalid refresh token");

        }

        User user = User.builder()
                .id(userId)
                .username(jwtData.data().username())
                .email(jwtData.data().email())
                .build();

        tokensRepository.revokeToken(tokenId);
        return generateTokensPair(user);
    }

    @Transactional
    public void revokeToken(String token) {
        JWTHelper.JWTData<RefreshTokenData> jwtData;
        try {
            jwtData = jwtHelper.parseJWT(token, RefreshTokenData.class);
        } catch (JwtException e) {
            return;
        }

        tokensRepository.revokeToken(UUID.fromString(jwtData.subject()));
    }

    record AccessTokenData(String username, String email) {
    }

    record RefreshTokenData(UUID userId, String username, String email) {
    }
}

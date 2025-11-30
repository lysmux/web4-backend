package dev.lysmux.web4;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@ApplicationScoped
public class JWTHelper {
    private final SecretKey SECRET = Keys.hmacShaKeyFor("MySuperSecretKey32BytesExactly!!".getBytes(StandardCharsets.UTF_8));

    private final JwtParser jwtParser = Jwts.parser()
            .verifyWith(SECRET)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void configureObjectMapper() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> String generateJWT(String subject, T data, long expirationMs) {
        Map<String, Object> claims = objectMapper.convertValue(data, new TypeReference<>() {});

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SECRET)
                .compact();
    }

    public <T> JWTData<T> parseJWT(String jwt, Class<T> clazz) {
        Claims claims = jwtParser.parseSignedClaims(jwt).getPayload();
        T data = objectMapper.convertValue(claims, clazz);

        return new JWTData<>(claims.getSubject(), data);
    }

    public record JWTData<T>(String subject, T data) {}
}

package io.github.carlospc1987.accountlifecycle.bootstrap.security.token;

import io.github.carlospc1987.accountlifecycle.bootstrap.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DefaultJwtTokenService implements JwtTokenService {

    private final JwtProperties jwtProperties;
    private final Clock clock;

    public DefaultJwtTokenService(JwtProperties jwtProperties) {
        this(jwtProperties, Clock.systemUTC());
    }

    DefaultJwtTokenService(JwtProperties jwtProperties, Clock clock) {
        this.jwtProperties = Objects.requireNonNull(jwtProperties, "jwtProperties is required");
        this.clock = Objects.requireNonNull(clock, "clock is required");
    }

    @Override
    public String generateAccessToken(UUID accountId, List<String> roles) {
        Objects.requireNonNull(accountId, "accountId is required");
        List<String> safeRoles = Objects.requireNonNull(roles, "roles is required");

        Instant now = Instant.now(clock);
        Instant expiresAt = now.plusSeconds(jwtProperties.getExpirationMinutes() * 60);

        return Jwts.builder()
                .subject(accountId.toString())
                .issuer(requireIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .claim("roles", safeRoles)
                .signWith(signingKey(), mapAlgorithm())
                .compact();
    }

    @Override
    public JwtClaims validateAndExtract(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("token is required");
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey())
                    .requireIssuer(requireIssuer())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            validateRequiredClaims(claims);
            List<String> roles = extractRoles(claims);

            return new JwtClaims(
                    claims.getSubject(),
                    claims.getIssuer(),
                    roles,
                    claims.getIssuedAt().toInstant(),
                    claims.getExpiration().toInstant()
            );
        } catch (JwtException ex) {
            throw new IllegalArgumentException("Invalid JWT token", ex);
        }
    }

    private SecretKey signingKey() {
        String secret = jwtProperties.getSecret();
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret must be provided via environment variable");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private String requireIssuer() {
        String issuer = jwtProperties.getIssuer();
        if (issuer == null || issuer.isBlank()) {
            throw new IllegalStateException("JWT issuer is required");
        }
        return issuer;
    }

    private SecureDigestAlgorithm<? super SecretKey, ?> mapAlgorithm() {
        return switch (jwtProperties.getAlgorithm()) {
            case "HS256" -> Jwts.SIG.HS256;
            case "HS512" -> Jwts.SIG.HS512;
            default -> throw new IllegalArgumentException("Unsupported JWT algorithm: " + jwtProperties.getAlgorithm());
        };
    }

    private void validateRequiredClaims(Claims claims) {
        for (String claimName : jwtProperties.getRequiredClaims()) {
            Object value = claims.get(claimName);
            if (value == null || (value instanceof String str && str.isBlank())) {
                throw new IllegalArgumentException("Missing required claim: " + claimName);
            }
        }
    }

    private List<String> extractRoles(Claims claims) {
        Object rolesClaim = claims.get("roles");
        if (!(rolesClaim instanceof List<?> rolesRaw)) {
            throw new IllegalArgumentException("Invalid roles claim");
        }
        return rolesRaw.stream().map(String::valueOf).toList();
    }
}

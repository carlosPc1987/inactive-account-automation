package io.github.carlospc1987.accountlifecycle.bootstrap.security.auth.service;

import io.github.carlospc1987.accountlifecycle.bootstrap.security.auth.api.AuthTokenResponse;
import io.github.carlospc1987.accountlifecycle.bootstrap.security.config.JwtProperties;
import io.github.carlospc1987.accountlifecycle.bootstrap.security.token.JwtTokenService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DefaultAuthTokenService implements AuthTokenService {

    private static final String TOKEN_TYPE = "Bearer";
    private static final List<String> DEFAULT_ROLES = List.of("ROLE_USER");

    private final JwtTokenService jwtTokenService;
    private final JwtProperties jwtProperties;

    public DefaultAuthTokenService(JwtTokenService jwtTokenService, JwtProperties jwtProperties) {
        this.jwtTokenService = Objects.requireNonNull(jwtTokenService, "jwtTokenService is required");
        this.jwtProperties = Objects.requireNonNull(jwtProperties, "jwtProperties is required");
    }

    @Override
    public AuthTokenResponse issueAccessToken(String email) {
        String normalizedEmail = normalizeEmail(email);
        UUID accountId = UUID.nameUUIDFromBytes(normalizedEmail.getBytes(StandardCharsets.UTF_8));
        String accessToken = jwtTokenService.generateAccessToken(accountId, DEFAULT_ROLES);
        long expiresInSeconds = jwtProperties.getExpirationMinutes() * 60;
        return new AuthTokenResponse(accessToken, TOKEN_TYPE, expiresInSeconds);
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email is required");
        }
        return email.trim().toLowerCase();
    }
}

package io.github.carlospc1987.accountlifecycle.bootstrap.security.auth.api;

public record AuthTokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}

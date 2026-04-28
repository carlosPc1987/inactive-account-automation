package io.github.carlospc1987.accountlifecycle.bootstrap.security.token;

import java.time.Instant;
import java.util.List;

public record JwtClaims(
        String subject,
        String issuer,
        List<String> roles,
        Instant issuedAt,
        Instant expiresAt
) {
}

package io.github.carlospc1987.accountlifecycle.bootstrap.security.token;

import io.github.carlospc1987.accountlifecycle.bootstrap.security.config.JwtProperties;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultJwtTokenServiceTest {

    private static final String SECRET_32_BYTES = "12345678901234567890123456789012";

    @Test
    void givenValidConfiguration_whenGenerateAndValidateToken_thenReturnExpectedClaims() {
        JwtProperties properties = jwtProperties(60, "account-lifecycle-manager");
        DefaultJwtTokenService tokenService = new DefaultJwtTokenService(properties);
        UUID accountId = UUID.randomUUID();

        String token = tokenService.generateAccessToken(accountId, List.of("ADMIN", "USER"));
        JwtClaims claims = tokenService.validateAndExtract(token);

        assertEquals(accountId.toString(), claims.subject());
        assertEquals("account-lifecycle-manager", claims.issuer());
        assertEquals(List.of("ADMIN", "USER"), claims.roles());
    }

    @Test
    void givenTokenWithDifferentIssuer_whenValidateToken_thenThrowIllegalArgumentException() {
        JwtProperties sourceProperties = jwtProperties(60, "account-lifecycle-manager");
        DefaultJwtTokenService sourceService = new DefaultJwtTokenService(sourceProperties);
        String token = sourceService.generateAccessToken(UUID.randomUUID(), List.of("USER"));

        JwtProperties targetProperties = jwtProperties(60, "another-issuer");
        DefaultJwtTokenService targetService = new DefaultJwtTokenService(targetProperties);

        assertThrows(IllegalArgumentException.class, () -> targetService.validateAndExtract(token));
    }

    @Test
    void givenExpiredToken_whenValidateToken_thenThrowIllegalArgumentException() {
        JwtProperties properties = jwtProperties(-1, "account-lifecycle-manager");
        Clock fixedClock = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
        DefaultJwtTokenService tokenService = new DefaultJwtTokenService(properties, fixedClock);

        String token = tokenService.generateAccessToken(UUID.randomUUID(), List.of("USER"));

        assertThrows(IllegalArgumentException.class, () -> tokenService.validateAndExtract(token));
    }

    @Test
    void givenMissingSecret_whenGenerateToken_thenThrowIllegalStateException() {
        JwtProperties properties = jwtProperties(60, "account-lifecycle-manager");
        properties.setSecret("");
        DefaultJwtTokenService tokenService = new DefaultJwtTokenService(properties);

        assertThrows(IllegalStateException.class, () -> tokenService.generateAccessToken(UUID.randomUUID(), List.of("USER")));
    }

    private JwtProperties jwtProperties(long expirationMinutes, String issuer) {
        JwtProperties properties = new JwtProperties();
        properties.setSecret(SECRET_32_BYTES);
        properties.setExpirationMinutes(expirationMinutes);
        properties.setIssuer(issuer);
        properties.setAlgorithm("HS256");
        properties.setRequiredClaims(List.of("sub", "roles", "iat", "exp"));
        return properties;
    }
}

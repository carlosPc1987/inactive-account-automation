package io.github.carlospc1987.accountlifecycle.bootstrap.security.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtPropertiesTest {

    @Test
    void shouldUseSecureDefaults() {
        JwtProperties properties = new JwtProperties();

        assertEquals("", properties.getSecret());
        assertEquals(60, properties.getExpirationMinutes());
        assertEquals("account-lifecycle-manager", properties.getIssuer());
        assertEquals("HS256", properties.getAlgorithm());
        assertEquals(List.of("sub", "roles", "iat", "exp"), properties.getRequiredClaims());
    }

    @Test
    void shouldAllowOverridingValues() {
        JwtProperties properties = new JwtProperties();

        properties.setSecret("env-secret");
        properties.setExpirationMinutes(30);
        properties.setIssuer("account-lifecycle-manager");
        properties.setAlgorithm("HS256");
        properties.setRequiredClaims(List.of("sub", "roles", "iat", "exp"));

        assertEquals("env-secret", properties.getSecret());
        assertEquals(30, properties.getExpirationMinutes());
        assertEquals("account-lifecycle-manager", properties.getIssuer());
        assertEquals("HS256", properties.getAlgorithm());
        assertEquals(List.of("sub", "roles", "iat", "exp"), properties.getRequiredClaims());
    }
}

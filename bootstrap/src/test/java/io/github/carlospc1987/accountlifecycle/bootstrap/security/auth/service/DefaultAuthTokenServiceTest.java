package io.github.carlospc1987.accountlifecycle.bootstrap.security.auth.service;

import io.github.carlospc1987.accountlifecycle.bootstrap.security.auth.api.AuthTokenResponse;
import io.github.carlospc1987.accountlifecycle.bootstrap.security.config.JwtProperties;
import io.github.carlospc1987.accountlifecycle.bootstrap.security.token.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

class DefaultAuthTokenServiceTest {

    @Test
    void shouldIssueAccessTokenFromEmail() {
        JwtTokenService jwtTokenService = mock(JwtTokenService.class);
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setExpirationMinutes(60);

        when(jwtTokenService.generateAccessToken(org.mockito.ArgumentMatchers.any(UUID.class), org.mockito.ArgumentMatchers.anyList()))
                .thenReturn("generated-jwt");

        DefaultAuthTokenService service = new DefaultAuthTokenService(jwtTokenService, jwtProperties);

        AuthTokenResponse response = service.issueAccessToken("User@Example.com");

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<String>> rolesCaptor = ArgumentCaptor.forClass(List.class);
        verify(jwtTokenService).generateAccessToken(idCaptor.capture(), rolesCaptor.capture());

        UUID expectedId = UUID.nameUUIDFromBytes("user@example.com".getBytes(StandardCharsets.UTF_8));
        assertEquals(expectedId, idCaptor.getValue());
        assertEquals(List.of("ROLE_USER"), rolesCaptor.getValue());
        assertEquals("generated-jwt", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals(3600, response.expiresIn());
    }

    @Test
    void shouldFailWhenEmailIsBlank() {
        JwtTokenService jwtTokenService = mock(JwtTokenService.class);
        JwtProperties jwtProperties = new JwtProperties();
        DefaultAuthTokenService service = new DefaultAuthTokenService(jwtTokenService, jwtProperties);

        assertThrows(IllegalArgumentException.class, () -> service.issueAccessToken(" "));
    }

    @Test
    void shouldFailWhenEmailIsNull() {
        JwtTokenService jwtTokenService = mock(JwtTokenService.class);
        JwtProperties jwtProperties = new JwtProperties();
        DefaultAuthTokenService service = new DefaultAuthTokenService(jwtTokenService, jwtProperties);

        assertThrows(IllegalArgumentException.class, () -> service.issueAccessToken(null));
    }

    @Test
    void shouldNormalizeEmailBeforeGeneratingToken() {
        JwtTokenService jwtTokenService = mock(JwtTokenService.class);
        JwtProperties jwtProperties = new JwtProperties();
        when(jwtTokenService.generateAccessToken(org.mockito.ArgumentMatchers.any(UUID.class), org.mockito.ArgumentMatchers.anyList()))
                .thenReturn("generated-jwt");
        DefaultAuthTokenService service = new DefaultAuthTokenService(jwtTokenService, jwtProperties);

        service.issueAccessToken("  USER@Example.com  ");

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<String>> rolesCaptor = ArgumentCaptor.forClass(List.class);
        verify(jwtTokenService).generateAccessToken(idCaptor.capture(), rolesCaptor.capture());

        UUID expectedId = UUID.nameUUIDFromBytes("user@example.com".getBytes(StandardCharsets.UTF_8));
        assertEquals(expectedId, idCaptor.getValue());
        assertEquals(List.of("ROLE_USER"), rolesCaptor.getValue());
    }
}

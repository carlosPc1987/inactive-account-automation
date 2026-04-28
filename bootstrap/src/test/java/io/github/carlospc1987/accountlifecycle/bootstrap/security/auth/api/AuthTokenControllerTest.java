package io.github.carlospc1987.accountlifecycle.bootstrap.security.auth.api;

import io.github.carlospc1987.accountlifecycle.bootstrap.security.auth.service.AuthTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AuthTokenControllerTest {

    @Test
    void shouldIssueToken() {
        AuthTokenService authTokenService = Mockito.mock(AuthTokenService.class);
        AuthTokenController controller = new AuthTokenController(authTokenService);
        AuthTokenResponse expected = new AuthTokenResponse("token-value", "Bearer", 3600);
        when(authTokenService.issueAccessToken("user@example.com")).thenReturn(expected);

        AuthTokenResponse response = controller.issueToken(new AuthTokenRequest("user@example.com"));

        assertEquals(expected, response);
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() {
        AuthTokenService authTokenService = Mockito.mock(AuthTokenService.class);
        AuthTokenController controller = new AuthTokenController(authTokenService);
        when(authTokenService.issueAccessToken("")).thenThrow(new IllegalArgumentException("email is required"));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> controller.issueToken(new AuthTokenRequest(""))
        );

        assertEquals(400, ex.getStatusCode().value());
    }
}

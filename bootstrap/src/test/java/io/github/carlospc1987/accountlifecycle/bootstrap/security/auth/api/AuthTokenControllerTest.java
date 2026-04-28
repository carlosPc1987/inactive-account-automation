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
    void givenValidEmail_whenIssueToken_thenReturnTokenResponse() {
        AuthTokenService authTokenService = Mockito.mock(AuthTokenService.class);
        AuthTokenController controller = new AuthTokenController(authTokenService);
        AuthTokenResponse expected = new AuthTokenResponse("token-value", "Bearer", 3600);
        when(authTokenService.issueAccessToken("user@example.com")).thenReturn(expected);

        AuthTokenResponse response = controller.issueToken(new AuthTokenRequest("user@example.com"));

        assertEquals(expected, response);
    }

    @Test
    void givenInvalidEmail_whenIssueToken_thenReturnBadRequest() {
        AuthTokenService authTokenService = Mockito.mock(AuthTokenService.class);
        AuthTokenController controller = new AuthTokenController(authTokenService);
        when(authTokenService.issueAccessToken("")).thenThrow(new IllegalArgumentException("email is required"));
        AuthTokenRequest request = new AuthTokenRequest("");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> controller.issueToken(request)
        );

        assertEquals(400, ex.getStatusCode().value());
    }
}

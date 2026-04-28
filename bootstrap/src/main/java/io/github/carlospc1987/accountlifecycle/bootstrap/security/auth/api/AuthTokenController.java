package io.github.carlospc1987.accountlifecycle.bootstrap.security.auth.api;

import io.github.carlospc1987.accountlifecycle.bootstrap.security.auth.service.AuthTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthTokenController {

    private final AuthTokenService authTokenService;

    public AuthTokenController(AuthTokenService authTokenService) {
        this.authTokenService = Objects.requireNonNull(authTokenService, "authTokenService is required");
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public AuthTokenResponse issueToken(@RequestBody AuthTokenRequest request) {
        try {
            return authTokenService.issueAccessToken(request.email());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}

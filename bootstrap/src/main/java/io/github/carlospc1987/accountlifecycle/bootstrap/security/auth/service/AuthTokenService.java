package io.github.carlospc1987.accountlifecycle.bootstrap.security.auth.service;

import io.github.carlospc1987.accountlifecycle.bootstrap.security.auth.api.AuthTokenResponse;

public interface AuthTokenService {

    AuthTokenResponse issueAccessToken(String email);
}

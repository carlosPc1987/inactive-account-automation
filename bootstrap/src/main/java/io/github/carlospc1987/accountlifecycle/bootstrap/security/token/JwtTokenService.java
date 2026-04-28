package io.github.carlospc1987.accountlifecycle.bootstrap.security.token;

import java.util.List;
import java.util.UUID;

public interface JwtTokenService {

    String generateAccessToken(UUID accountId, List<String> roles);

    JwtClaims validateAndExtract(String token);
}

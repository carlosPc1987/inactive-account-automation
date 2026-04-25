package io.github.carlospc1987.accountlifecycle.bootstrap.account.api;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;

import java.time.Instant;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String email,
        Instant createdAt,
        Instant lastActivityAt,
        boolean inactive
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getEmail(),
                account.getCreatedAt(),
                account.getLastActivityAt(),
                account.isInactive()
        );
    }
}

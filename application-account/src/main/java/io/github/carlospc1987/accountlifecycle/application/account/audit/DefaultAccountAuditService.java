package io.github.carlospc1987.accountlifecycle.application.account.audit;

import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEvent;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEventRepository;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class DefaultAccountAuditService implements AccountAuditService {

    private final AccountAuditEventRepository accountAuditEventRepository;

    public DefaultAccountAuditService(AccountAuditEventRepository accountAuditEventRepository) {
        this.accountAuditEventRepository = Objects.requireNonNull(
                accountAuditEventRepository,
                "accountAuditEventRepository is required"
        );
    }

    @Override
    public AccountAuditEvent recordEvent(UUID accountId, String eventType, String description) {
        UUID validatedAccountId = Objects.requireNonNull(accountId, "accountId is required");
        String normalizedEventType = requireNonBlank(eventType, "eventType is required");
        String normalizedDescription = requireNonBlank(description, "description is required");

        AccountAuditEvent event = new AccountAuditEvent(
                UUID.randomUUID(),
                validatedAccountId,
                normalizedEventType,
                normalizedDescription,
                Instant.now()
        );
        return accountAuditEventRepository.save(event);
    }

    private String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

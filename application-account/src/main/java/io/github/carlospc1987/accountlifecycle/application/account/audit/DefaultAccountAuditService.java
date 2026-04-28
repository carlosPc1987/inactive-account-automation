package io.github.carlospc1987.accountlifecycle.application.account.audit;

import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEvent;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEventRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class DefaultAccountAuditService implements AccountAuditService {

    private final AccountAuditEventRepository accountAuditEventRepository;
    private final Clock clock;

    public DefaultAccountAuditService(AccountAuditEventRepository accountAuditEventRepository) {
        this(accountAuditEventRepository, Clock.systemUTC());
    }

    DefaultAccountAuditService(AccountAuditEventRepository accountAuditEventRepository, Clock clock) {
        this.accountAuditEventRepository = Objects.requireNonNull(accountAuditEventRepository, "accountAuditEventRepository is required");
        this.clock = Objects.requireNonNull(clock, "clock is required");
    }

    @Override
    public AccountAuditEvent recordAccountEvent(UUID accountId, String eventType, String eventDetails) {
        Objects.requireNonNull(accountId, "accountId is required");
        Objects.requireNonNull(eventType, "eventType is required");
        Objects.requireNonNull(eventDetails, "eventDetails is required");

        Instant now = Instant.now(clock);
        AccountAuditEvent event = new AccountAuditEvent(
                UUID.randomUUID(),
                accountId,
                eventType,
                eventDetails,
                now
        );
        return accountAuditEventRepository.save(event);
    }
}

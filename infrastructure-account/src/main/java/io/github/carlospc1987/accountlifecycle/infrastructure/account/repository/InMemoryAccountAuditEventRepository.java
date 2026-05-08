package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository;

import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEvent;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryAccountAuditEventRepository implements AccountAuditEventRepository {

    private final ConcurrentMap<UUID, AccountAuditEvent> storage = new ConcurrentHashMap<>();

    @Override
    public AccountAuditEvent save(AccountAuditEvent event) {
        AccountAuditEvent validatedEvent = Objects.requireNonNull(event, "event is required");
        storage.put(validatedEvent.getId(), validatedEvent);
        return validatedEvent;
    }

    @Override
    public Optional<AccountAuditEvent> findById(UUID eventId) {
        UUID validatedEventId = Objects.requireNonNull(eventId, "eventId is required");
        return Optional.ofNullable(storage.get(validatedEventId));
    }

    @Override
    public List<AccountAuditEvent> findAllByAccountId(UUID accountId) {
        UUID validatedAccountId = Objects.requireNonNull(accountId, "accountId is required");
        List<AccountAuditEvent> result = new ArrayList<>();
        for (AccountAuditEvent event : storage.values()) {
            if (validatedAccountId.equals(event.getAccountId())) {
                result.add(event);
            }
        }
        return result;
    }
}

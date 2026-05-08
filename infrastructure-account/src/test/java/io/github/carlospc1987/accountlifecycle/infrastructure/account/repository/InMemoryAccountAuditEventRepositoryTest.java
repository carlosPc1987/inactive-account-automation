package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository;

import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryAccountAuditEventRepositoryTest {

    @Test
    void givenSavedEvent_whenFindById_thenReturnPersistedEvent() {
        InMemoryAccountAuditEventRepository repository = new InMemoryAccountAuditEventRepository();
        AccountAuditEvent event = event(UUID.randomUUID(), "ACCOUNT_MARKED_INACTIVE");
        repository.save(event);

        Optional<AccountAuditEvent> found = repository.findById(event.getId());

        assertTrue(found.isPresent());
        assertEquals(event, found.get());
    }

    @Test
    void givenSavedEventsForDifferentAccounts_whenFindAllByAccountId_thenReturnOnlyMatchingEvents() {
        InMemoryAccountAuditEventRepository repository = new InMemoryAccountAuditEventRepository();
        UUID accountId = UUID.randomUUID();
        AccountAuditEvent first = event(accountId, "ACCOUNT_MARKED_INACTIVE");
        AccountAuditEvent second = event(accountId, "ACCOUNT_REACTIVATED");
        AccountAuditEvent otherAccountEvent = event(UUID.randomUUID(), "ACCOUNT_MARKED_INACTIVE");
        repository.save(first);
        repository.save(second);
        repository.save(otherAccountEvent);

        List<AccountAuditEvent> events = repository.findAllByAccountId(accountId);

        assertEquals(2, events.size());
        assertTrue(events.stream().anyMatch(saved -> saved.getId().equals(first.getId())));
        assertTrue(events.stream().anyMatch(saved -> saved.getId().equals(second.getId())));
    }

    @Test
    void givenNoEventsForAccount_whenFindAllByAccountId_thenReturnEmptyList() {
        InMemoryAccountAuditEventRepository repository = new InMemoryAccountAuditEventRepository();
        UUID accountId = UUID.randomUUID();

        List<AccountAuditEvent> events = repository.findAllByAccountId(accountId);

        assertTrue(events.isEmpty());
    }

    private AccountAuditEvent event(UUID accountId, String eventType) {
        return new AccountAuditEvent(
                UUID.randomUUID(),
                accountId,
                eventType,
                "description",
                Instant.parse("2026-05-08T09:00:00Z")
        );
    }
}

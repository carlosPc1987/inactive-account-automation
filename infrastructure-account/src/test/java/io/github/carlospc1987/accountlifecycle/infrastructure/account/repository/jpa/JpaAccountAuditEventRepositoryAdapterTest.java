package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa;

import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaAccountAuditEventRepositoryAdapterTest {

    @Mock
    private SpringDataAccountAuditEventJpaRepository repository;

    private JpaAccountAuditEventRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new JpaAccountAuditEventRepositoryAdapter(repository);
    }

    @Test
    void givenDomainAuditEvent_whenSave_thenReturnMappedDomainAuditEvent() {
        AccountAuditEvent event = event();
        when(repository.save(any(JpaAccountAuditEventEntity.class))).thenReturn(entityFrom(event));

        AccountAuditEvent saved = adapter.save(event);

        assertEquals(event.getId(), saved.getId());
        assertEquals(event.getAccountId(), saved.getAccountId());
    }

    @Test
    void givenExistingEventId_whenFindById_thenReturnMappedDomainAuditEvent() {
        AccountAuditEvent event = event();
        when(repository.findById(event.getId())).thenReturn(Optional.of(entityFrom(event)));

        Optional<AccountAuditEvent> found = adapter.findById(event.getId());

        assertTrue(found.isPresent());
        assertEquals(event.getEventType(), found.get().getEventType());
    }

    @Test
    void givenAccountId_whenFindAllByAccountId_thenReturnMappedDomainAuditEvents() {
        UUID accountId = UUID.randomUUID();
        AccountAuditEvent first = event(accountId, "ACCOUNT_MARKED_INACTIVE");
        AccountAuditEvent second = event(accountId, "ACCOUNT_REACTIVATED");
        when(repository.findAllByAccountId(accountId)).thenReturn(List.of(entityFrom(first), entityFrom(second)));

        List<AccountAuditEvent> result = adapter.findAllByAccountId(accountId);

        assertEquals(2, result.size());
        assertEquals(first.getId(), result.get(0).getId());
        assertEquals(second.getId(), result.get(1).getId());
    }

    private AccountAuditEvent event() {
        return event(UUID.randomUUID(), "ACCOUNT_MARKED_INACTIVE");
    }

    private AccountAuditEvent event(UUID accountId, String eventType) {
        return new AccountAuditEvent(
                UUID.randomUUID(),
                accountId,
                eventType,
                "description",
                Instant.parse("2026-05-08T10:00:00Z")
        );
    }

    private JpaAccountAuditEventEntity entityFrom(AccountAuditEvent event) {
        return new JpaAccountAuditEventEntity(
                event.getId(),
                event.getAccountId(),
                event.getEventType(),
                event.getDescription(),
                event.getOccurredAt()
        );
    }
}

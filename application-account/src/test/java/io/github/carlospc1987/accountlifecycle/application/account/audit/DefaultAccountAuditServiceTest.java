package io.github.carlospc1987.accountlifecycle.application.account.audit;

import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEvent;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultAccountAuditServiceTest {

    @Test
    void givenValidAuditInput_whenRecordAccountEvent_thenPersistAndReturnAuditEvent() {
        AccountAuditEventRepository repository = mock(AccountAuditEventRepository.class);
        Instant now = Instant.parse("2026-04-28T00:00:00Z");
        Clock fixedClock = Clock.fixed(now, ZoneOffset.UTC);
        DefaultAccountAuditService service = new DefaultAccountAuditService(repository, fixedClock);
        when(repository.save(any(AccountAuditEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UUID accountId = UUID.randomUUID();

        AccountAuditEvent saved = service.recordAccountEvent(accountId, "ACCOUNT_INACTIVITY_MARKED", "Marked by daily scheduler");

        assertNotNull(saved.getEventId());
        assertEquals(accountId, saved.getAccountId());
        assertEquals("ACCOUNT_INACTIVITY_MARKED", saved.getEventType());
        assertEquals("Marked by daily scheduler", saved.getEventDetails());
        assertEquals(now, saved.getOccurredAt());
    }

    @Test
    void givenValidAuditInput_whenRecordAccountEvent_thenSendBuiltEventToRepository() {
        AccountAuditEventRepository repository = mock(AccountAuditEventRepository.class);
        DefaultAccountAuditService service = new DefaultAccountAuditService(repository);
        when(repository.save(any(AccountAuditEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UUID accountId = UUID.randomUUID();

        service.recordAccountEvent(accountId, "ACCOUNT_EVENT", "Details");

        ArgumentCaptor<AccountAuditEvent> captor = ArgumentCaptor.forClass(AccountAuditEvent.class);
        verify(repository).save(captor.capture());
        assertEquals(accountId, captor.getValue().getAccountId());
        assertEquals("ACCOUNT_EVENT", captor.getValue().getEventType());
        assertEquals("Details", captor.getValue().getEventDetails());
    }

    @Test
    void givenNullAccountId_whenRecordAccountEvent_thenThrowNullPointerException() {
        AccountAuditEventRepository repository = mock(AccountAuditEventRepository.class);
        DefaultAccountAuditService service = new DefaultAccountAuditService(repository);

        assertThrows(NullPointerException.class, () -> service.recordAccountEvent(null, "TYPE", "DETAILS"));
    }

    @Test
    void givenNullEventType_whenRecordAccountEvent_thenThrowNullPointerException() {
        AccountAuditEventRepository repository = mock(AccountAuditEventRepository.class);
        DefaultAccountAuditService service = new DefaultAccountAuditService(repository);

        assertThrows(NullPointerException.class, () -> service.recordAccountEvent(UUID.randomUUID(), null, "DETAILS"));
    }

    @Test
    void givenNullEventDetails_whenRecordAccountEvent_thenThrowNullPointerException() {
        AccountAuditEventRepository repository = mock(AccountAuditEventRepository.class);
        DefaultAccountAuditService service = new DefaultAccountAuditService(repository);

        assertThrows(NullPointerException.class, () -> service.recordAccountEvent(UUID.randomUUID(), "TYPE", null));
    }

    @Test
    void givenRepositoryFailure_whenRecordAccountEvent_thenPropagateException() {
        AccountAuditEventRepository repository = mock(AccountAuditEventRepository.class);
        DefaultAccountAuditService service = new DefaultAccountAuditService(repository);
        RuntimeException persistenceError = new RuntimeException("repository failure");
        when(repository.save(any(AccountAuditEvent.class))).thenThrow(persistenceError);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> service.recordAccountEvent(UUID.randomUUID(), "TYPE", "DETAILS")
        );

        assertEquals("repository failure", thrown.getMessage());
    }
}

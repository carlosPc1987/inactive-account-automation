package io.github.carlospc1987.accountlifecycle.application.account.audit;

import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEvent;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultAccountAuditServiceTest {

    @Mock
    private AccountAuditEventRepository accountAuditEventRepository;

    private DefaultAccountAuditService accountAuditService;

    @BeforeEach
    void setUp() {
        accountAuditService = new DefaultAccountAuditService(accountAuditEventRepository);
    }

    @Test
    void givenValidValues_whenRecordEvent_thenPersistAndReturnAuditEvent() {
        UUID accountId = UUID.randomUUID();
        String eventType = "ACCOUNT_MARKED_INACTIVE";
        String description = "scheduler marked account inactive";
        when(accountAuditEventRepository.save(any(AccountAuditEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountAuditEvent savedEvent = accountAuditService.recordEvent(accountId, eventType, description);

        assertNotNull(savedEvent.getId());
        assertEquals(accountId, savedEvent.getAccountId());
        assertEquals(eventType, savedEvent.getEventType());
        assertEquals(description, savedEvent.getDescription());
        assertNotNull(savedEvent.getOccurredAt());

        ArgumentCaptor<AccountAuditEvent> eventCaptor = ArgumentCaptor.forClass(AccountAuditEvent.class);
        verify(accountAuditEventRepository).save(eventCaptor.capture());
        assertEquals(accountId, eventCaptor.getValue().getAccountId());
    }

    @Test
    void givenValuesWithOuterSpaces_whenRecordEvent_thenTrimAndPersistNormalizedValues() {
        UUID accountId = UUID.randomUUID();
        String eventType = "  ACCOUNT_MARKED_INACTIVE  ";
        String description = "  scheduler marked account inactive  ";
        when(accountAuditEventRepository.save(any(AccountAuditEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountAuditEvent savedEvent = accountAuditService.recordEvent(accountId, eventType, description);

        assertEquals("ACCOUNT_MARKED_INACTIVE", savedEvent.getEventType());
        assertEquals("scheduler marked account inactive", savedEvent.getDescription());
    }

    @Test
    void givenNullAccountId_whenRecordEvent_thenThrowNullPointerException() {
        String eventType = "ACCOUNT_MARKED_INACTIVE";
        String description = "scheduler marked account inactive";

        assertThrows(
                NullPointerException.class,
                () -> accountAuditService.recordEvent(null, eventType, description)
        );
        verify(accountAuditEventRepository, never()).save(any(AccountAuditEvent.class));
    }

    @Test
    void givenNullEventType_whenRecordEvent_thenThrowIllegalArgumentException() {
        UUID accountId = UUID.randomUUID();
        String description = "scheduler marked account inactive";

        assertThrows(
                IllegalArgumentException.class,
                () -> accountAuditService.recordEvent(accountId, null, description)
        );
        verify(accountAuditEventRepository, never()).save(any(AccountAuditEvent.class));
    }

    @Test
    void givenBlankEventType_whenRecordEvent_thenThrowIllegalArgumentException() {
        UUID accountId = UUID.randomUUID();
        String eventType = "  ";
        String description = "scheduler marked account inactive";

        assertThrows(
                IllegalArgumentException.class,
                () -> accountAuditService.recordEvent(accountId, eventType, description)
        );
        verify(accountAuditEventRepository, never()).save(any(AccountAuditEvent.class));
    }

    @Test
    void givenNullDescription_whenRecordEvent_thenThrowIllegalArgumentException() {
        UUID accountId = UUID.randomUUID();
        String eventType = "ACCOUNT_MARKED_INACTIVE";

        assertThrows(
                IllegalArgumentException.class,
                () -> accountAuditService.recordEvent(accountId, eventType, null)
        );
        verify(accountAuditEventRepository, never()).save(any(AccountAuditEvent.class));
    }

    @Test
    void givenBlankDescription_whenRecordEvent_thenThrowIllegalArgumentException() {
        UUID accountId = UUID.randomUUID();
        String eventType = "ACCOUNT_MARKED_INACTIVE";
        String description = " ";

        assertThrows(
                IllegalArgumentException.class,
                () -> accountAuditService.recordEvent(accountId, eventType, description)
        );
        verify(accountAuditEventRepository, never()).save(any(AccountAuditEvent.class));
    }

    @Test
    void givenRepositoryFailure_whenRecordEvent_thenPropagateException() {
        UUID accountId = UUID.randomUUID();
        String eventType = "ACCOUNT_MARKED_INACTIVE";
        String description = "scheduler marked account inactive";
        RuntimeException persistenceFailure = new RuntimeException("database unavailable");
        when(accountAuditEventRepository.save(any(AccountAuditEvent.class))).thenThrow(persistenceFailure);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> accountAuditService.recordEvent(accountId, eventType, description)
        );

        assertEquals("database unavailable", thrown.getMessage());
    }

    @Test
    void givenNullRepository_whenCreateService_thenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DefaultAccountAuditService(null));
    }
}

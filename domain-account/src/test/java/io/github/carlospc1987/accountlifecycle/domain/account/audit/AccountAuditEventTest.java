package io.github.carlospc1987.accountlifecycle.domain.account.audit;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountAuditEventTest {

    @Test
    void givenValidInput_whenCreateAccountAuditEvent_thenExposeAllFieldValues() {
        UUID eventId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        Instant occurredAt = Instant.parse("2026-04-28T00:00:00Z");

        AccountAuditEvent event = new AccountAuditEvent(
                eventId,
                accountId,
                "ACCOUNT_INACTIVITY_MARKED",
                "Marked by daily scheduler",
                occurredAt
        );

        assertEquals(eventId, event.getEventId());
        assertEquals(accountId, event.getAccountId());
        assertEquals("ACCOUNT_INACTIVITY_MARKED", event.getEventType());
        assertEquals("Marked by daily scheduler", event.getEventDetails());
        assertEquals(occurredAt, event.getOccurredAt());
    }

    @Test
    void givenNullEventId_whenCreateAccountAuditEvent_thenThrowNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new AccountAuditEvent(
                        null,
                        UUID.randomUUID(),
                        "TYPE",
                        "DETAILS",
                        Instant.now()
                )
        );
    }

    @Test
    void givenNullAccountId_whenCreateAccountAuditEvent_thenThrowNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new AccountAuditEvent(
                        UUID.randomUUID(),
                        null,
                        "TYPE",
                        "DETAILS",
                        Instant.now()
                )
        );
    }

    @Test
    void givenNullEventType_whenCreateAccountAuditEvent_thenThrowNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new AccountAuditEvent(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        null,
                        "DETAILS",
                        Instant.now()
                )
        );
    }

    @Test
    void givenNullEventDetails_whenCreateAccountAuditEvent_thenThrowNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new AccountAuditEvent(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "TYPE",
                        null,
                        Instant.now()
                )
        );
    }

    @Test
    void givenNullOccurredAt_whenCreateAccountAuditEvent_thenThrowNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new AccountAuditEvent(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "TYPE",
                        "DETAILS",
                        null
                )
        );
    }
}

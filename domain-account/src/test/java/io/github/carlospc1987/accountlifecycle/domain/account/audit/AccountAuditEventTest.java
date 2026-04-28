package io.github.carlospc1987.accountlifecycle.domain.account.audit;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountAuditEventTest {

    @Test
    void givenValidValues_whenCreateAccountAuditEvent_thenExposeAllFields() {
        UUID id = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        String eventType = "ACCOUNT_MARKED_INACTIVE";
        String description = "Account was marked inactive by scheduler.";
        Instant occurredAt = Instant.parse("2026-04-28T10:15:30Z");

        AccountAuditEvent event = new AccountAuditEvent(id, accountId, eventType, description, occurredAt);

        assertEquals(id, event.getId());
        assertEquals(accountId, event.getAccountId());
        assertEquals(eventType, event.getEventType());
        assertEquals(description, event.getDescription());
        assertEquals(occurredAt, event.getOccurredAt());
    }

    @Test
    void givenNullId_whenCreateAccountAuditEvent_thenThrowNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new AccountAuditEvent(
                        null,
                        UUID.randomUUID(),
                        "ACCOUNT_MARKED_INACTIVE",
                        "description",
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
                        "ACCOUNT_MARKED_INACTIVE",
                        "description",
                        Instant.now()
                )
        );
    }

    @Test
    void givenNullEventType_whenCreateAccountAuditEvent_thenThrowIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountAuditEvent(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        null,
                        "description",
                        Instant.now()
                )
        );
    }

    @Test
    void givenBlankEventType_whenCreateAccountAuditEvent_thenThrowIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountAuditEvent(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "   ",
                        "description",
                        Instant.now()
                )
        );
    }

    @Test
    void givenNullDescription_whenCreateAccountAuditEvent_thenThrowIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountAuditEvent(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "ACCOUNT_MARKED_INACTIVE",
                        null,
                        Instant.now()
                )
        );
    }

    @Test
    void givenBlankDescription_whenCreateAccountAuditEvent_thenThrowIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountAuditEvent(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "ACCOUNT_MARKED_INACTIVE",
                        " ",
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
                        "ACCOUNT_MARKED_INACTIVE",
                        "description",
                        null
                )
        );
    }
}

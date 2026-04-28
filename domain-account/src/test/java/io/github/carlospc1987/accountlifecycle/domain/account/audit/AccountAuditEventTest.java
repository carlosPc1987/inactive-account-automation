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
        UUID accountId = UUID.randomUUID();
        String eventType = "ACCOUNT_MARKED_INACTIVE";
        String description = "description";
        Instant occurredAt = Instant.now();

        assertThrows(
                NullPointerException.class,
                () -> new AccountAuditEvent(
                        null,
                        accountId,
                        eventType,
                        description,
                        occurredAt
                )
        );
    }

    @Test
    void givenNullAccountId_whenCreateAccountAuditEvent_thenThrowNullPointerException() {
        UUID id = UUID.randomUUID();
        String eventType = "ACCOUNT_MARKED_INACTIVE";
        String description = "description";
        Instant occurredAt = Instant.now();

        assertThrows(
                NullPointerException.class,
                () -> new AccountAuditEvent(
                        id,
                        null,
                        eventType,
                        description,
                        occurredAt
                )
        );
    }

    @Test
    void givenNullEventType_whenCreateAccountAuditEvent_thenThrowIllegalArgumentException() {
        UUID id = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        String description = "description";
        Instant occurredAt = Instant.now();

        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountAuditEvent(
                        id,
                        accountId,
                        null,
                        description,
                        occurredAt
                )
        );
    }

    @Test
    void givenBlankEventType_whenCreateAccountAuditEvent_thenThrowIllegalArgumentException() {
        UUID id = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        String description = "description";
        Instant occurredAt = Instant.now();

        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountAuditEvent(
                        id,
                        accountId,
                        "   ",
                        description,
                        occurredAt
                )
        );
    }

    @Test
    void givenNullDescription_whenCreateAccountAuditEvent_thenThrowIllegalArgumentException() {
        UUID id = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        String eventType = "ACCOUNT_MARKED_INACTIVE";
        Instant occurredAt = Instant.now();

        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountAuditEvent(
                        id,
                        accountId,
                        eventType,
                        null,
                        occurredAt
                )
        );
    }

    @Test
    void givenBlankDescription_whenCreateAccountAuditEvent_thenThrowIllegalArgumentException() {
        UUID id = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        String eventType = "ACCOUNT_MARKED_INACTIVE";
        Instant occurredAt = Instant.now();

        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountAuditEvent(
                        id,
                        accountId,
                        eventType,
                        " ",
                        occurredAt
                )
        );
    }

    @Test
    void givenNullOccurredAt_whenCreateAccountAuditEvent_thenThrowNullPointerException() {
        UUID id = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        String eventType = "ACCOUNT_MARKED_INACTIVE";
        String description = "description";

        assertThrows(
                NullPointerException.class,
                () -> new AccountAuditEvent(
                        id,
                        accountId,
                        eventType,
                        description,
                        null
                )
        );
    }
}

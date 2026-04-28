package io.github.carlospc1987.accountlifecycle.domain.account;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountTest {

    @Test
    void givenActiveAccount_whenMarkAsInactive_thenSetInactiveTrue() {
        Account account = new Account(
                UUID.randomUUID(),
                "user@example.com",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z"),
                false
        );

        account.markAsInactive();

        assertTrue(account.isInactive());
    }

    @Test
    void givenInactiveAccount_whenRegisterActivity_thenUpdateLastActivityAndSetInactiveFalse() {
        Account account = new Account(
                UUID.randomUUID(),
                "user@example.com",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z"),
                true
        );
        Instant activityTime = Instant.parse("2026-02-01T12:00:00Z");

        account.registerActivity(activityTime);

        assertEquals(activityTime, account.getLastActivityAt());
        assertFalse(account.isInactive());
    }

    @Test
    void givenNullActivityTime_whenRegisterActivity_thenThrowNullPointerException() {
        Account account = new Account(
                UUID.randomUUID(),
                "user@example.com",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z"),
                false
        );

        assertThrows(NullPointerException.class, () -> account.registerActivity(null));
    }

    @Test
    void givenNullId_whenCreateAccount_thenThrowNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new Account(
                        null,
                        "user@example.com",
                        Instant.parse("2026-01-01T00:00:00Z"),
                        Instant.parse("2026-01-02T00:00:00Z"),
                        false
                )
        );
    }

    @Test
    void givenNullEmail_whenCreateAccount_thenThrowNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new Account(
                        UUID.randomUUID(),
                        null,
                        Instant.parse("2026-01-01T00:00:00Z"),
                        Instant.parse("2026-01-02T00:00:00Z"),
                        false
                )
        );
    }

    @Test
    void givenNullCreatedAt_whenCreateAccount_thenThrowNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new Account(
                        UUID.randomUUID(),
                        "user@example.com",
                        null,
                        Instant.parse("2026-01-02T00:00:00Z"),
                        false
                )
        );
    }

    @Test
    void givenNullLastActivityAt_whenCreateAccount_thenThrowNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new Account(
                        UUID.randomUUID(),
                        "user@example.com",
                        Instant.parse("2026-01-01T00:00:00Z"),
                        null,
                        false
                )
        );
    }
}

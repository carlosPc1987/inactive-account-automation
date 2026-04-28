package io.github.carlospc1987.accountlifecycle.domain.account.audit;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class AccountAuditEvent {

    private final UUID id;
    private final UUID accountId;
    private final String eventType;
    private final String description;
    private final Instant occurredAt;

    public AccountAuditEvent(UUID id, UUID accountId, String eventType, String description, Instant occurredAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.accountId = Objects.requireNonNull(accountId, "accountId is required");
        this.eventType = requireNonBlank(eventType, "eventType is required");
        this.description = requireNonBlank(description, "description is required");
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt is required");
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    private static String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}

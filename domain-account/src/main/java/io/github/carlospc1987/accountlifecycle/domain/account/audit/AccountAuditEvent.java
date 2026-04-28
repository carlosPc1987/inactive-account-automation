package io.github.carlospc1987.accountlifecycle.domain.account.audit;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class AccountAuditEvent {

    private final UUID eventId;
    private final UUID accountId;
    private final String eventType;
    private final String eventDetails;
    private final Instant occurredAt;

    public AccountAuditEvent(UUID eventId, UUID accountId, String eventType, String eventDetails, Instant occurredAt) {
        this.eventId = Objects.requireNonNull(eventId, "eventId is required");
        this.accountId = Objects.requireNonNull(accountId, "accountId is required");
        this.eventType = Objects.requireNonNull(eventType, "eventType is required");
        this.eventDetails = Objects.requireNonNull(eventDetails, "eventDetails is required");
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt is required");
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}

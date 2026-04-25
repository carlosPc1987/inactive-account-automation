package io.github.carlospc1987.accountlifecycle.domain.account;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Account {

    private final UUID id;
    private final String email;
    private final Instant createdAt;
    private Instant lastActivityAt;
    private boolean inactive;

    public Account(UUID id, String email, Instant createdAt, Instant lastActivityAt, boolean inactive) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.email = Objects.requireNonNull(email, "email is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.lastActivityAt = Objects.requireNonNull(lastActivityAt, "lastActivityAt is required");
        this.inactive = inactive;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastActivityAt() {
        return lastActivityAt;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void registerActivity(Instant occurredAt) {
        this.lastActivityAt = Objects.requireNonNull(occurredAt, "occurredAt is required");
        this.inactive = false;
    }

    public void markAsInactive() {
        this.inactive = true;
    }
}

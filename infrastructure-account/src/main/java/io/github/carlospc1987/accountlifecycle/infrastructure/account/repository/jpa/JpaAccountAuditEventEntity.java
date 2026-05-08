package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "account_audit_events")
public class JpaAccountAuditEventEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    UUID id;

    @Column(name = "account_id", nullable = false)
    UUID accountId;

    @Column(name = "event_type", nullable = false)
    String eventType;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    Instant occurredAt;

    protected JpaAccountAuditEventEntity() {
        // JPA only
    }

    public JpaAccountAuditEventEntity(UUID id, UUID accountId, String eventType, String description, Instant occurredAt) {
        this.id = id;
        this.accountId = accountId;
        this.eventType = eventType;
        this.description = description;
        this.occurredAt = occurredAt;
    }
}

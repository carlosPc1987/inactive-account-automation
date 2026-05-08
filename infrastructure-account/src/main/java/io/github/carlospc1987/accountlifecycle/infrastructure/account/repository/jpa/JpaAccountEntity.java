package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class JpaAccountEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    UUID id;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @Column(name = "last_activity_at", nullable = false)
    Instant lastActivityAt;

    @Column(name = "inactive", nullable = false)
    boolean inactive;

    protected JpaAccountEntity() {
        // JPA only
    }

    public JpaAccountEntity(UUID id, String email, Instant createdAt, Instant lastActivityAt, boolean inactive) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.lastActivityAt = lastActivityAt;
        this.inactive = inactive;
    }
}

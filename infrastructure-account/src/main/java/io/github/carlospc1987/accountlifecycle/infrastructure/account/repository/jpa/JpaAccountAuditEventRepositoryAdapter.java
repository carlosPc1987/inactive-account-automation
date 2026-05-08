package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa;

import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEvent;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEventRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class JpaAccountAuditEventRepositoryAdapter implements AccountAuditEventRepository {

    private final SpringDataAccountAuditEventJpaRepository repository;

    public JpaAccountAuditEventRepositoryAdapter(SpringDataAccountAuditEventJpaRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository is required");
    }

    @Override
    public AccountAuditEvent save(AccountAuditEvent event) {
        AccountAuditEvent validatedEvent = Objects.requireNonNull(event, "event is required");
        JpaAccountAuditEventEntity saved = repository.save(toEntity(validatedEvent));
        return toDomain(saved);
    }

    @Override
    public Optional<AccountAuditEvent> findById(UUID eventId) {
        UUID validatedEventId = Objects.requireNonNull(eventId, "eventId is required");
        return repository.findById(validatedEventId).map(this::toDomain);
    }

    @Override
    public List<AccountAuditEvent> findAllByAccountId(UUID accountId) {
        UUID validatedAccountId = Objects.requireNonNull(accountId, "accountId is required");
        return repository.findAllByAccountId(validatedAccountId).stream().map(this::toDomain).toList();
    }

    private JpaAccountAuditEventEntity toEntity(AccountAuditEvent event) {
        return new JpaAccountAuditEventEntity(
                event.getId(),
                event.getAccountId(),
                event.getEventType(),
                event.getDescription(),
                event.getOccurredAt()
        );
    }

    private AccountAuditEvent toDomain(JpaAccountAuditEventEntity entity) {
        return new AccountAuditEvent(
                entity.getId(),
                entity.getAccountId(),
                entity.getEventType(),
                entity.getDescription(),
                entity.getOccurredAt()
        );
    }
}

package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataAccountAuditEventJpaRepository extends JpaRepository<JpaAccountAuditEventEntity, UUID> {

    List<JpaAccountAuditEventEntity> findAllByAccountId(UUID accountId);
}

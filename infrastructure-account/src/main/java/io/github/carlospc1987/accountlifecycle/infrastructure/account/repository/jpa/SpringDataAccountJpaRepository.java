package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataAccountJpaRepository extends JpaRepository<JpaAccountEntity, UUID> {
}

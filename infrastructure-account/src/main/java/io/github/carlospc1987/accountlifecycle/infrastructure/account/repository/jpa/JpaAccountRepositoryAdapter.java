package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import io.github.carlospc1987.accountlifecycle.domain.account.AccountRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class JpaAccountRepositoryAdapter implements AccountRepository {

    private final SpringDataAccountJpaRepository repository;

    public JpaAccountRepositoryAdapter(SpringDataAccountJpaRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository is required");
    }

    @Override
    public Account save(Account account) {
        Account validatedAccount = Objects.requireNonNull(account, "account is required");
        JpaAccountEntity saved = repository.save(toEntity(validatedAccount));
        return toDomain(saved);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        UUID validatedId = Objects.requireNonNull(id, "id is required");
        return repository.findById(validatedId).map(this::toDomain);
    }

    @Override
    public List<Account> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        UUID validatedId = Objects.requireNonNull(id, "id is required");
        repository.deleteById(validatedId);
    }

    private JpaAccountEntity toEntity(Account account) {
        return new JpaAccountEntity(
                account.getId(),
                account.getEmail(),
                account.getCreatedAt(),
                account.getLastActivityAt(),
                account.isInactive()
        );
    }

    private Account toDomain(JpaAccountEntity entity) {
        return new Account(
                entity.id,
                entity.email,
                entity.createdAt,
                entity.lastActivityAt,
                entity.inactive
        );
    }
}

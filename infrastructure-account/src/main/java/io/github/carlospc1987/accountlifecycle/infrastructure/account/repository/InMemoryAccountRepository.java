package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import io.github.carlospc1987.accountlifecycle.domain.account.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryAccountRepository implements AccountRepository {

    private final ConcurrentMap<UUID, Account> storage = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        Account validatedAccount = Objects.requireNonNull(account, "account is required");
        storage.put(validatedAccount.getId(), validatedAccount);
        return validatedAccount;
    }

    @Override
    public Optional<Account> findById(UUID id) {
        UUID validatedId = Objects.requireNonNull(id, "id is required");
        return Optional.ofNullable(storage.get(validatedId));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(UUID id) {
        UUID validatedId = Objects.requireNonNull(id, "id is required");
        storage.remove(validatedId);
    }
}

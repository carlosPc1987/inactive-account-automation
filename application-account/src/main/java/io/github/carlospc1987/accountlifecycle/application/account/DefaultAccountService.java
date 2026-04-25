package io.github.carlospc1987.accountlifecycle.application.account;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import io.github.carlospc1987.accountlifecycle.domain.account.AccountRepository;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

public class DefaultAccountService implements AccountService {

    private final AccountRepository accountRepository;

    public DefaultAccountService(AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository is required");
    }

    @Override
    public Account createAccount(String email) {
        String normalizedEmail = requireEmail(email);
        Instant now = Instant.now();
        Account newAccount = new Account(UUID.randomUUID(), normalizedEmail, now, now, false);
        return accountRepository.save(newAccount);
    }

    @Override
    public Account getAccountById(UUID id) {
        return accountRepository.findById(requireId(id))
                .orElseThrow(() -> new NoSuchElementException("Account not found"));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account updateAccountEmail(UUID id, String email) {
        Account existing = getAccountById(id);
        Account updated = new Account(
                existing.getId(),
                requireEmail(email),
                existing.getCreatedAt(),
                existing.getLastActivityAt(),
                existing.isInactive()
        );
        return accountRepository.save(updated);
    }

    @Override
    public void deleteAccount(UUID id) {
        getAccountById(id);
        accountRepository.deleteById(id);
    }

    private UUID requireId(UUID id) {
        return Objects.requireNonNull(id, "id is required");
    }

    private String requireEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email is required");
        }
        return email.trim();
    }
}

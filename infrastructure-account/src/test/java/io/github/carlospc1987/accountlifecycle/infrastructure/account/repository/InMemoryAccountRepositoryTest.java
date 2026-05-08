package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryAccountRepositoryTest {

    @Test
    void givenSavedAccount_whenFindById_thenReturnPersistedAccount() {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        Account account = account("user@example.com");
        repository.save(account);

        Optional<Account> found = repository.findById(account.getId());

        assertTrue(found.isPresent());
        assertEquals(account, found.get());
    }

    @Test
    void givenSavedAccounts_whenFindAll_thenReturnAllAccounts() {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        Account first = account("first@example.com");
        Account second = account("second@example.com");
        repository.save(first);
        repository.save(second);

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void givenDeletedAccountId_whenFindById_thenReturnEmpty() {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        Account account = account("user@example.com");
        repository.save(account);

        repository.deleteById(account.getId());

        assertTrue(repository.findById(account.getId()).isEmpty());
    }

    private Account account(String email) {
        Instant now = Instant.parse("2026-05-08T09:00:00Z");
        return new Account(UUID.randomUUID(), email, now, now, false);
    }
}

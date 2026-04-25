package io.github.carlospc1987.accountlifecycle.application.account;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import io.github.carlospc1987.accountlifecycle.domain.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private DefaultAccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new DefaultAccountService(accountRepository);
    }

    @Test
    void shouldCreateAccount() {
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account created = accountService.createAccount("user@example.com");

        assertEquals("user@example.com", created.getEmail());
        assertFalse(created.isInactive());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldGetAccountById() {
        UUID id = UUID.randomUUID();
        Account account = new Account(
                id,
                "user@example.com",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z"),
                false
        );
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        Account found = accountService.getAccountById(id);

        assertEquals(account, found);
    }

    @Test
    void shouldThrowWhenAccountNotFoundById() {
        UUID id = UUID.randomUUID();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> accountService.getAccountById(id));
    }

    @Test
    void shouldReturnAllAccounts() {
        List<Account> accounts = List.of(
                new Account(UUID.randomUUID(), "a@example.com", Instant.now(), Instant.now(), false),
                new Account(UUID.randomUUID(), "b@example.com", Instant.now(), Instant.now(), true)
        );
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts();

        assertEquals(2, result.size());
        assertEquals(accounts, result);
    }

    @Test
    void shouldUpdateAccountEmail() {
        UUID id = UUID.randomUUID();
        Account existing = new Account(
                id,
                "old@example.com",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z"),
                true
        );
        when(accountRepository.findById(id)).thenReturn(Optional.of(existing));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account updated = accountService.updateAccountEmail(id, "new@example.com");

        assertEquals("new@example.com", updated.getEmail());
        assertEquals(existing.getId(), updated.getId());
        assertEquals(existing.getCreatedAt(), updated.getCreatedAt());
        assertEquals(existing.getLastActivityAt(), updated.getLastActivityAt());
        assertEquals(existing.isInactive(), updated.isInactive());
    }

    @Test
    void shouldDeleteAccountById() {
        UUID id = UUID.randomUUID();
        Account existing = new Account(
                id,
                "user@example.com",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z"),
                false
        );
        when(accountRepository.findById(id)).thenReturn(Optional.of(existing));

        accountService.deleteAccount(id);

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(accountRepository).deleteById(idCaptor.capture());
        assertEquals(id, idCaptor.getValue());
    }

    @Test
    void shouldNotDeleteWhenAccountDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> accountService.deleteAccount(id));
        verify(accountRepository, never()).deleteById(any(UUID.class));
    }
}

package io.github.carlospc1987.accountlifecycle.bootstrap.account.api;

import io.github.carlospc1987.accountlifecycle.application.account.AccountService;
import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    private AccountController accountController;

    @BeforeEach
    void setUp() {
        accountController = new AccountController(accountService);
    }

    @Test
    void shouldCreateAccount() {
        Account created = account(UUID.randomUUID(), "user@example.com", false);
        when(accountService.createAccount("user@example.com")).thenReturn(created);

        AccountResponse response = accountController.createAccount(new CreateAccountRequest("user@example.com"));

        assertEquals(created.getId(), response.id());
        assertEquals(created.getEmail(), response.email());
    }

    @Test
    void shouldReturnBadRequestWhenCreateInputInvalid() {
        when(accountService.createAccount("")).thenThrow(new IllegalArgumentException("email is required"));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> accountController.createAccount(new CreateAccountRequest(""))
        );

        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void shouldGetAccountById() {
        UUID id = UUID.randomUUID();
        Account account = account(id, "user@example.com", false);
        when(accountService.getAccountById(id)).thenReturn(account);

        AccountResponse response = accountController.getAccountById(id);

        assertEquals(id, response.id());
    }

    @Test
    void shouldReturnNotFoundWhenAccountDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(accountService.getAccountById(id)).thenThrow(new NoSuchElementException("Account not found"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> accountController.getAccountById(id));

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void shouldReturnAllAccounts() {
        when(accountService.getAllAccounts()).thenReturn(List.of(
                account(UUID.randomUUID(), "a@example.com", false),
                account(UUID.randomUUID(), "b@example.com", true)
        ));

        List<AccountResponse> response = accountController.getAllAccounts();

        assertEquals(2, response.size());
    }

    @Test
    void shouldUpdateAccountEmail() {
        UUID id = UUID.randomUUID();
        Account updated = account(id, "new@example.com", false);
        when(accountService.updateAccountEmail(id, "new@example.com")).thenReturn(updated);

        AccountResponse response = accountController.updateAccountEmail(id, new UpdateAccountEmailRequest("new@example.com"));

        assertEquals("new@example.com", response.email());
    }

    @Test
    void shouldDeleteAccount() {
        UUID id = UUID.randomUUID();

        accountController.deleteAccount(id);

        verify(accountService).deleteAccount(id);
    }

    @Test
    void shouldReturnNotFoundWhenDeleteTargetMissing() {
        UUID id = UUID.randomUUID();
        doThrow(new NoSuchElementException("Account not found")).when(accountService).deleteAccount(id);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> accountController.deleteAccount(id));

        assertEquals(404, ex.getStatusCode().value());
    }

    private Account account(UUID id, String email, boolean inactive) {
        return new Account(
                id,
                email,
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z"),
                inactive
        );
    }
}

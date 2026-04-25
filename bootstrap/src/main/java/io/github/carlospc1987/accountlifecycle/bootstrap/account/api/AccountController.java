package io.github.carlospc1987.accountlifecycle.bootstrap.account.api;

import io.github.carlospc1987.accountlifecycle.application.account.AccountService;
import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = Objects.requireNonNull(accountService, "accountService is required");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@RequestBody CreateAccountRequest request) {
        return mapWithClientValidation(() -> accountService.createAccount(request.email()));
    }

    @GetMapping("/{id}")
    public AccountResponse getAccountById(@PathVariable UUID id) {
        return mapWithNotFoundHandling(() -> accountService.getAccountById(id));
    }

    @GetMapping
    public List<AccountResponse> getAllAccounts() {
        return accountService.getAllAccounts().stream().map(AccountResponse::from).toList();
    }

    @PutMapping("/{id}/email")
    public AccountResponse updateAccountEmail(@PathVariable UUID id, @RequestBody UpdateAccountEmailRequest request) {
        return mapWithClientValidation(() -> accountService.updateAccountEmail(id, request.email()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable UUID id) {
        try {
            accountService.deleteAccount(id);
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    private AccountResponse mapWithNotFoundHandling(AccountSupplier accountSupplier) {
        try {
            return AccountResponse.from(accountSupplier.get());
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    private AccountResponse mapWithClientValidation(AccountSupplier accountSupplier) {
        try {
            return AccountResponse.from(accountSupplier.get());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @FunctionalInterface
    private interface AccountSupplier {
        Account get();
    }
}

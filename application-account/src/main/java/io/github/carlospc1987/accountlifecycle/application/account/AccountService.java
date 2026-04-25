package io.github.carlospc1987.accountlifecycle.application.account;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    Account createAccount(String email);

    Account getAccountById(UUID id);

    List<Account> getAllAccounts();

    Account updateAccountEmail(UUID id, String email);

    void deleteAccount(UUID id);
}

package io.github.carlospc1987.accountlifecycle.domain.account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(UUID id);

    List<Account> findAll();

    void deleteById(UUID id);
}

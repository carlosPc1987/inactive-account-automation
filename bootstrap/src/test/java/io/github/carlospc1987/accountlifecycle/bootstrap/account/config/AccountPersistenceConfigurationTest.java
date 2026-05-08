package io.github.carlospc1987.accountlifecycle.bootstrap.account.config;

import io.github.carlospc1987.accountlifecycle.domain.account.AccountRepository;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEventRepository;
import io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa.SpringDataAccountAuditEventJpaRepository;
import io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa.SpringDataAccountJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountPersistenceConfigurationTest {

    @Test
    void givenSpringDataAccountRepository_whenCreateAccountRepositoryBean_thenReturnAdapterInstance() {
        AccountPersistenceConfiguration configuration = new AccountPersistenceConfiguration();
        SpringDataAccountJpaRepository springDataRepository = Mockito.mock(SpringDataAccountJpaRepository.class);

        AccountRepository accountRepository = configuration.accountRepository(springDataRepository);

        assertNotNull(accountRepository);
    }

    @Test
    void givenSpringDataAuditRepository_whenCreateAccountAuditRepositoryBean_thenReturnAdapterInstance() {
        AccountPersistenceConfiguration configuration = new AccountPersistenceConfiguration();
        SpringDataAccountAuditEventJpaRepository springDataRepository = Mockito.mock(SpringDataAccountAuditEventJpaRepository.class);

        AccountAuditEventRepository auditRepository = configuration.accountAuditEventRepository(springDataRepository);

        assertNotNull(auditRepository);
    }
}

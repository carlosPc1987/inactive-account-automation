package io.github.carlospc1987.accountlifecycle.bootstrap.account.config;

import io.github.carlospc1987.accountlifecycle.domain.account.AccountRepository;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEventRepository;
import io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa.JpaAccountAuditEventRepositoryAdapter;
import io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa.JpaAccountRepositoryAdapter;
import io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa.SpringDataAccountAuditEventJpaRepository;
import io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa.SpringDataAccountJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountPersistenceConfiguration {

    @Bean
    AccountRepository accountRepository(SpringDataAccountJpaRepository repository) {
        return new JpaAccountRepositoryAdapter(repository);
    }

    @Bean
    AccountAuditEventRepository accountAuditEventRepository(SpringDataAccountAuditEventJpaRepository repository) {
        return new JpaAccountAuditEventRepositoryAdapter(repository);
    }
}

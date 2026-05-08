package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = JpaRepositoryAdaptersIntegrationTest.TestJpaConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        JpaAccountRepositoryAdapter.class,
        JpaAccountAuditEventRepositoryAdapter.class
})
class JpaRepositoryAdaptersIntegrationTest {

    @Autowired
    private JpaAccountRepositoryAdapter accountRepositoryAdapter;

    @Autowired
    private JpaAccountAuditEventRepositoryAdapter accountAuditEventRepositoryAdapter;

    @Test
    void givenPersistedAccount_whenFindById_thenReturnStoredAccount() {
        Account account = new Account(
                UUID.randomUUID(),
                "user@example.com",
                Instant.parse("2026-05-08T10:00:00Z"),
                Instant.parse("2026-05-08T10:00:00Z"),
                false
        );
        accountRepositoryAdapter.save(account);

        Optional<Account> found = accountRepositoryAdapter.findById(account.getId());

        assertTrue(found.isPresent());
        assertEquals("user@example.com", found.get().getEmail());
    }

    @Test
    void givenPersistedAuditEvents_whenFindAllByAccountId_thenReturnOnlyMatchingAccountEvents() {
        UUID accountId = UUID.randomUUID();
        AccountAuditEvent first = new AccountAuditEvent(
                UUID.randomUUID(),
                accountId,
                "ACCOUNT_MARKED_INACTIVE",
                "first",
                Instant.parse("2026-05-08T10:00:00Z")
        );
        AccountAuditEvent second = new AccountAuditEvent(
                UUID.randomUUID(),
                accountId,
                "ACCOUNT_REACTIVATED",
                "second",
                Instant.parse("2026-05-08T11:00:00Z")
        );
        AccountAuditEvent other = new AccountAuditEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "ACCOUNT_MARKED_INACTIVE",
                "other",
                Instant.parse("2026-05-08T12:00:00Z")
        );
        accountAuditEventRepositoryAdapter.save(first);
        accountAuditEventRepositoryAdapter.save(second);
        accountAuditEventRepositoryAdapter.save(other);

        List<AccountAuditEvent> events = accountAuditEventRepositoryAdapter.findAllByAccountId(accountId);

        assertEquals(2, events.size());
        assertTrue(events.stream().anyMatch(event -> event.getId().equals(first.getId())));
        assertTrue(events.stream().anyMatch(event -> event.getId().equals(second.getId())));
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EntityScan(basePackageClasses = {JpaAccountEntity.class, JpaAccountAuditEventEntity.class})
    @EnableJpaRepositories(basePackageClasses = {
            SpringDataAccountJpaRepository.class,
            SpringDataAccountAuditEventJpaRepository.class
    })
    static class TestJpaConfiguration {
    }
}

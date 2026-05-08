package io.github.carlospc1987.accountlifecycle.bootstrap.e2e;

import io.github.carlospc1987.accountlifecycle.application.account.audit.AccountAuditService;
import io.github.carlospc1987.accountlifecycle.application.account.audit.DefaultAccountAuditService;
import io.github.carlospc1987.accountlifecycle.application.account.inactivity.DefaultInactivityEvaluationService;
import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEvent;
import io.github.carlospc1987.accountlifecycle.domain.account.rules.InactivityRule;
import io.github.carlospc1987.accountlifecycle.domain.account.rules.RuleEvaluator;
import io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.InMemoryAccountAuditEventRepository;
import io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.InMemoryAccountRepository;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InactivityAuditFlowEndToEndTest {

    @Test
    void givenActiveAccountBeyondThreshold_whenEvaluate_thenMarkInactiveAndPersistAuditEvent() {
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryAccountAuditEventRepository auditRepository = new InMemoryAccountAuditEventRepository();
        AccountAuditService accountAuditService = new DefaultAccountAuditService(auditRepository);
        RuleEvaluator ruleEvaluator = new RuleEvaluator(List.of(new InactiveForThirtyDaysRule()));
        DefaultInactivityEvaluationService inactivityEvaluationService = new DefaultInactivityEvaluationService(
                accountRepository,
                ruleEvaluator,
                accountAuditService
        );

        Account account = new Account(
                UUID.randomUUID(),
                "user@example.com",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-03-01T00:00:00Z"),
                false
        );
        accountRepository.save(account);

        int appliedRules = inactivityEvaluationService.evaluate(Instant.parse("2026-05-08T09:00:00Z"));

        assertEquals(1, appliedRules);
        Account updatedAccount = accountRepository.findById(account.getId()).orElseThrow();
        assertTrue(updatedAccount.isInactive());

        List<AccountAuditEvent> events = auditRepository.findAllByAccountId(account.getId());
        assertEquals(1, events.size());
        assertEquals("ACCOUNT_MARKED_INACTIVE", events.get(0).getEventType());
    }

    @Test
    void givenAlreadyInactiveAccount_whenEvaluate_thenDoNotCreateDuplicateInactiveAuditEvent() {
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryAccountAuditEventRepository auditRepository = new InMemoryAccountAuditEventRepository();
        AccountAuditService accountAuditService = new DefaultAccountAuditService(auditRepository);
        RuleEvaluator ruleEvaluator = new RuleEvaluator(List.of(new InactiveForThirtyDaysRule()));
        DefaultInactivityEvaluationService inactivityEvaluationService = new DefaultInactivityEvaluationService(
                accountRepository,
                ruleEvaluator,
                accountAuditService
        );

        Account alreadyInactive = new Account(
                UUID.randomUUID(),
                "inactive@example.com",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-03-01T00:00:00Z"),
                true
        );
        accountRepository.save(alreadyInactive);

        int appliedRules = inactivityEvaluationService.evaluate(Instant.parse("2026-05-08T09:00:00Z"));

        assertEquals(0, appliedRules);
        assertTrue(auditRepository.findAllByAccountId(alreadyInactive.getId()).isEmpty());
    }

    private static class InactiveForThirtyDaysRule implements InactivityRule {

        @Override
        public boolean shouldApply(Account account, Instant evaluationTime) {
            if (account.isInactive()) {
                return false;
            }
            long inactiveDays = Duration.between(account.getLastActivityAt(), evaluationTime).toDays();
            return inactiveDays >= 30;
        }

        @Override
        public void apply(Account account, Instant evaluationTime) {
            account.markAsInactive();
        }
    }
}

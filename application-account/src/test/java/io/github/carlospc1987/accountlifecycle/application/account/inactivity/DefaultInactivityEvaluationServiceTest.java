package io.github.carlospc1987.accountlifecycle.application.account.inactivity;

import io.github.carlospc1987.accountlifecycle.application.account.audit.AccountAuditService;
import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import io.github.carlospc1987.accountlifecycle.domain.account.AccountRepository;
import io.github.carlospc1987.accountlifecycle.domain.account.rules.InactivityRule;
import io.github.carlospc1987.accountlifecycle.domain.account.rules.RuleEvaluator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultInactivityEvaluationServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountAuditService accountAuditService;

    @Test
    void givenMatchingRule_whenEvaluate_thenPersistChangesRecordAuditAndReturnAppliedRules() {
        Account activeAccount = account(false);
        RuleEvaluator ruleEvaluator = new RuleEvaluator(List.of(new MarkInactiveRule()));
        DefaultInactivityEvaluationService service = new DefaultInactivityEvaluationService(
                accountRepository,
                ruleEvaluator,
                accountAuditService
        );
        when(accountRepository.findAll()).thenReturn(List.of(activeAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Instant evaluationTime = Instant.parse("2026-04-28T20:00:00Z");

        int appliedRules = service.evaluate(evaluationTime);

        assertEquals(1, appliedRules);
        verify(accountRepository).save(activeAccount);
        verify(accountAuditService).recordEvent(
                activeAccount.getId(),
                "ACCOUNT_MARKED_INACTIVE",
                "Account marked inactive by inactivity evaluation."
        );
    }

    @Test
    void givenNonMatchingRule_whenEvaluate_thenSkipPersistenceAndAuditAndReturnZero() {
        Account activeAccount = account(false);
        RuleEvaluator ruleEvaluator = new RuleEvaluator(List.of(new NeverApplyRule()));
        DefaultInactivityEvaluationService service = new DefaultInactivityEvaluationService(
                accountRepository,
                ruleEvaluator,
                accountAuditService
        );
        when(accountRepository.findAll()).thenReturn(List.of(activeAccount));
        Instant evaluationTime = Instant.parse("2026-04-28T20:00:00Z");

        int appliedRules = service.evaluate(evaluationTime);

        assertEquals(0, appliedRules);
        verify(accountRepository, never()).save(any(Account.class));
        verify(accountAuditService, never()).recordEvent(any(UUID.class), any(String.class), any(String.class));
    }

    @Test
    void givenAlreadyInactiveAccount_whenRuleApplies_thenPersistButDoNotCreateDuplicateInactiveAuditEvent() {
        Account inactiveAccount = account(true);
        RuleEvaluator ruleEvaluator = new RuleEvaluator(List.of(new MarkInactiveRule()));
        DefaultInactivityEvaluationService service = new DefaultInactivityEvaluationService(
                accountRepository,
                ruleEvaluator,
                accountAuditService
        );
        when(accountRepository.findAll()).thenReturn(List.of(inactiveAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Instant evaluationTime = Instant.parse("2026-04-28T20:00:00Z");

        int appliedRules = service.evaluate(evaluationTime);

        assertEquals(1, appliedRules);
        verify(accountRepository).save(inactiveAccount);
        verify(accountAuditService, never()).recordEvent(any(UUID.class), any(String.class), any(String.class));
    }

    @Test
    void givenMultipleAccounts_whenEvaluate_thenAggregateAppliedRulesAcrossAllAccounts() {
        Account activeAccount = account(false);
        Account otherActiveAccount = account(false);
        RuleEvaluator ruleEvaluator = new RuleEvaluator(List.of(new MarkFirstAccountInactiveRule(activeAccount.getId())));
        DefaultInactivityEvaluationService service = new DefaultInactivityEvaluationService(
                accountRepository,
                ruleEvaluator,
                accountAuditService
        );
        when(accountRepository.findAll()).thenReturn(List.of(activeAccount, otherActiveAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Instant evaluationTime = Instant.parse("2026-04-28T20:00:00Z");

        int appliedRules = service.evaluate(evaluationTime);

        assertEquals(1, appliedRules);
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        assertEquals(activeAccount.getId(), accountCaptor.getValue().getId());
    }

    @Test
    void givenNullEvaluationTime_whenEvaluate_thenThrowNullPointerException() {
        RuleEvaluator ruleEvaluator = new RuleEvaluator(List.of(new NeverApplyRule()));
        DefaultInactivityEvaluationService service = new DefaultInactivityEvaluationService(
                accountRepository,
                ruleEvaluator,
                accountAuditService
        );

        assertThrows(NullPointerException.class, () -> service.evaluate(null));
    }

    @Test
    void givenNullAccountRepository_whenCreateService_thenThrowNullPointerException() {
        RuleEvaluator ruleEvaluator = new RuleEvaluator(List.of(new NeverApplyRule()));
        AccountAuditService auditService = accountAuditService;

        assertThrows(
                NullPointerException.class,
                () -> new DefaultInactivityEvaluationService(null, ruleEvaluator, auditService)
        );
    }

    @Test
    void givenNullRuleEvaluator_whenCreateService_thenThrowNullPointerException() {
        AccountRepository repository = accountRepository;
        AccountAuditService auditService = accountAuditService;

        assertThrows(
                NullPointerException.class,
                () -> new DefaultInactivityEvaluationService(repository, null, auditService)
        );
    }

    @Test
    void givenNullAuditService_whenCreateService_thenThrowNullPointerException() {
        AccountRepository repository = accountRepository;
        RuleEvaluator ruleEvaluator = new RuleEvaluator(List.of(new NeverApplyRule()));

        assertThrows(
                NullPointerException.class,
                () -> new DefaultInactivityEvaluationService(repository, ruleEvaluator, null)
        );
    }

    private Account account(boolean inactive) {
        Instant createdAt = Instant.parse("2026-01-01T00:00:00Z");
        Instant lastActivity = Instant.parse("2026-03-01T00:00:00Z");
        return new Account(UUID.randomUUID(), "user@example.com", createdAt, lastActivity, inactive);
    }

    private static class MarkInactiveRule implements InactivityRule {
        @Override
        public boolean shouldApply(Account account, Instant evaluationTime) {
            return true;
        }

        @Override
        public void apply(Account account, Instant evaluationTime) {
            account.markAsInactive();
        }
    }

    private static class NeverApplyRule implements InactivityRule {
        @Override
        public boolean shouldApply(Account account, Instant evaluationTime) {
            return false;
        }

        @Override
        public void apply(Account account, Instant evaluationTime) {
            throw new IllegalStateException("should not be called");
        }
    }

    private static class MarkFirstAccountInactiveRule implements InactivityRule {

        private final UUID targetAccountId;

        private MarkFirstAccountInactiveRule(UUID targetAccountId) {
            this.targetAccountId = targetAccountId;
        }

        @Override
        public boolean shouldApply(Account account, Instant evaluationTime) {
            return targetAccountId.equals(account.getId());
        }

        @Override
        public void apply(Account account, Instant evaluationTime) {
            account.markAsInactive();
        }
    }
}

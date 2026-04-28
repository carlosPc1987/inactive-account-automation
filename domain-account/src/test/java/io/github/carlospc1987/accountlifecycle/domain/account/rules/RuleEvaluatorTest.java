package io.github.carlospc1987.accountlifecycle.domain.account.rules;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuleEvaluatorTest {

    @Test
    void givenMatchingRule_whenEvaluate_thenApplyRuleAndReturnAppliedCount() {
        Account account = account();
        Instant evaluationTime = Instant.parse("2026-04-01T00:00:00Z");
        MarkInactiveRule rule = new MarkInactiveRule();
        RuleEvaluator evaluator = new RuleEvaluator(List.of(rule));

        int appliedRules = evaluator.evaluate(account, evaluationTime);

        assertEquals(1, appliedRules);
        assertTrue(account.isInactive());
    }

    @Test
    void givenNonMatchingRule_whenEvaluate_thenSkipRuleAndReturnZero() {
        Account account = account();
        Instant evaluationTime = Instant.parse("2026-04-01T00:00:00Z");
        NeverApplyRule rule = new NeverApplyRule();
        RuleEvaluator evaluator = new RuleEvaluator(List.of(rule));

        int appliedRules = evaluator.evaluate(account, evaluationTime);

        assertEquals(0, appliedRules);
    }

    @Test
    void givenMultipleMatchingRules_whenEvaluate_thenApplyAllInOrder() {
        Account account = account();
        Instant evaluationTime = Instant.parse("2026-04-01T00:00:00Z");
        RuleEvaluator evaluator = new RuleEvaluator(List.of(new MarkInactiveRule(), new CounterRule()));

        int appliedRules = evaluator.evaluate(account, evaluationTime);

        assertEquals(2, appliedRules);
    }

    @Test
    void givenNullAccount_whenEvaluate_thenThrowNullPointerException() {
        RuleEvaluator evaluator = new RuleEvaluator(List.of(new NeverApplyRule()));
        Instant evaluationTime = Instant.now();

        assertThrows(NullPointerException.class, () -> evaluator.evaluate(null, evaluationTime));
    }

    @Test
    void givenNullEvaluationTime_whenEvaluate_thenThrowNullPointerException() {
        RuleEvaluator evaluator = new RuleEvaluator(List.of(new NeverApplyRule()));
        Account account = account();

        assertThrows(NullPointerException.class, () -> evaluator.evaluate(account, null));
    }

    private Account account() {
        return new Account(
                UUID.randomUUID(),
                "user@example.com",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-03-01T00:00:00Z"),
                false
        );
    }

    private static class MarkInactiveRule implements InactivityRule {
        @Override
        public boolean shouldApply(Account account, Instant evaluationTime) {
            return !account.isInactive();
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

    private static class CounterRule implements InactivityRule {
        @Override
        public boolean shouldApply(Account account, Instant evaluationTime) {
            return true;
        }

        @Override
        public void apply(Account account, Instant evaluationTime) {
            // no-op; existence validates that a second rule can be executed
        }
    }
}

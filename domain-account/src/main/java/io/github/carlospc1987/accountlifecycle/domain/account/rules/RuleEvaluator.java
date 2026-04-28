package io.github.carlospc1987.accountlifecycle.domain.account.rules;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class RuleEvaluator {

    private final List<InactivityRule> rules;

    public RuleEvaluator(List<InactivityRule> rules) {
        this.rules = List.copyOf(Objects.requireNonNull(rules, "rules are required"));
    }

    public int evaluate(Account account, Instant evaluationTime) {
        Objects.requireNonNull(account, "account is required");
        Objects.requireNonNull(evaluationTime, "evaluationTime is required");

        int appliedRules = 0;
        for (InactivityRule rule : rules) {
            if (rule.shouldApply(account, evaluationTime)) {
                rule.apply(account, evaluationTime);
                appliedRules++;
            }
        }
        return appliedRules;
    }
}

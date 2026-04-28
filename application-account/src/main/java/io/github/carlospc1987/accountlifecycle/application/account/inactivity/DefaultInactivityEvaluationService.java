package io.github.carlospc1987.accountlifecycle.application.account.inactivity;

import io.github.carlospc1987.accountlifecycle.application.account.audit.AccountAuditService;
import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import io.github.carlospc1987.accountlifecycle.domain.account.AccountRepository;
import io.github.carlospc1987.accountlifecycle.domain.account.rules.RuleEvaluator;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class DefaultInactivityEvaluationService implements InactivityEvaluationService {

    private static final String ACCOUNT_MARKED_INACTIVE = "ACCOUNT_MARKED_INACTIVE";
    private static final String ACCOUNT_MARKED_INACTIVE_DESCRIPTION = "Account marked inactive by inactivity evaluation.";

    private final AccountRepository accountRepository;
    private final RuleEvaluator ruleEvaluator;
    private final AccountAuditService accountAuditService;

    public DefaultInactivityEvaluationService(
            AccountRepository accountRepository,
            RuleEvaluator ruleEvaluator,
            AccountAuditService accountAuditService
    ) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository is required");
        this.ruleEvaluator = Objects.requireNonNull(ruleEvaluator, "ruleEvaluator is required");
        this.accountAuditService = Objects.requireNonNull(accountAuditService, "accountAuditService is required");
    }

    @Override
    public int evaluate(Instant evaluationTime) {
        Instant validatedEvaluationTime = Objects.requireNonNull(evaluationTime, "evaluationTime is required");
        List<Account> accounts = accountRepository.findAll();

        int appliedRules = 0;
        for (Account account : accounts) {
            boolean initiallyInactive = account.isInactive();
            int accountAppliedRules = ruleEvaluator.evaluate(account, validatedEvaluationTime);
            appliedRules += accountAppliedRules;

            if (accountAppliedRules > 0) {
                accountRepository.save(account);
            }

            if (!initiallyInactive && account.isInactive()) {
                accountAuditService.recordEvent(
                        account.getId(),
                        ACCOUNT_MARKED_INACTIVE,
                        ACCOUNT_MARKED_INACTIVE_DESCRIPTION
                );
            }
        }

        return appliedRules;
    }
}

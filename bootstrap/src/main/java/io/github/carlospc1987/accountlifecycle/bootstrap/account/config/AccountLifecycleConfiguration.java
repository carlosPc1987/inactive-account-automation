package io.github.carlospc1987.accountlifecycle.bootstrap.account.config;

import io.github.carlospc1987.accountlifecycle.application.account.audit.AccountAuditService;
import io.github.carlospc1987.accountlifecycle.application.account.audit.DefaultAccountAuditService;
import io.github.carlospc1987.accountlifecycle.application.account.inactivity.DefaultInactivityEvaluationService;
import io.github.carlospc1987.accountlifecycle.application.account.inactivity.InactivityEvaluationService;
import io.github.carlospc1987.accountlifecycle.domain.account.AccountRepository;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEventRepository;
import io.github.carlospc1987.accountlifecycle.domain.account.rules.InactivityRule;
import io.github.carlospc1987.accountlifecycle.domain.account.rules.RuleEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AccountLifecycleConfiguration {

    @Bean
    AccountAuditService accountAuditService(AccountAuditEventRepository accountAuditEventRepository) {
        return new DefaultAccountAuditService(accountAuditEventRepository);
    }

    @Bean
    RuleEvaluator ruleEvaluator(List<InactivityRule> inactivityRules) {
        return new RuleEvaluator(inactivityRules);
    }

    @Bean
    InactivityEvaluationService inactivityEvaluationService(
            AccountRepository accountRepository,
            RuleEvaluator ruleEvaluator,
            AccountAuditService accountAuditService
    ) {
        return new DefaultInactivityEvaluationService(accountRepository, ruleEvaluator, accountAuditService);
    }
}

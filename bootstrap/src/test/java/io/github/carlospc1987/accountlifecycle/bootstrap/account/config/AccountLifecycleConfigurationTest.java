package io.github.carlospc1987.accountlifecycle.bootstrap.account.config;

import io.github.carlospc1987.accountlifecycle.application.account.audit.AccountAuditService;
import io.github.carlospc1987.accountlifecycle.application.account.inactivity.InactivityEvaluationService;
import io.github.carlospc1987.accountlifecycle.domain.account.AccountRepository;
import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEventRepository;
import io.github.carlospc1987.accountlifecycle.domain.account.rules.InactivityRule;
import io.github.carlospc1987.accountlifecycle.domain.account.rules.RuleEvaluator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountLifecycleConfigurationTest {

    @Test
    void givenAuditEventRepository_whenCreateAccountAuditServiceBean_thenReturnServiceInstance() {
        AccountLifecycleConfiguration configuration = new AccountLifecycleConfiguration();
        AccountAuditEventRepository repository = Mockito.mock(AccountAuditEventRepository.class);

        AccountAuditService service = configuration.accountAuditService(repository);

        assertNotNull(service);
    }

    @Test
    void givenInactivityRules_whenCreateRuleEvaluatorBean_thenReturnEvaluatorInstance() {
        AccountLifecycleConfiguration configuration = new AccountLifecycleConfiguration();
        InactivityRule rule = new InactivityRule() {
            @Override
            public boolean shouldApply(io.github.carlospc1987.accountlifecycle.domain.account.Account account, Instant evaluationTime) {
                return false;
            }

            @Override
            public void apply(io.github.carlospc1987.accountlifecycle.domain.account.Account account, Instant evaluationTime) {
                // no-op
            }
        };

        RuleEvaluator evaluator = configuration.ruleEvaluator(List.of(rule));

        assertNotNull(evaluator);
    }

    @Test
    void givenDependencies_whenCreateInactivityEvaluationServiceBean_thenReturnServiceInstance() {
        AccountLifecycleConfiguration configuration = new AccountLifecycleConfiguration();
        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        RuleEvaluator evaluator = Mockito.mock(RuleEvaluator.class);
        AccountAuditService auditService = Mockito.mock(AccountAuditService.class);

        InactivityEvaluationService service = configuration.inactivityEvaluationService(
                accountRepository,
                evaluator,
                auditService
        );

        assertNotNull(service);
    }
}

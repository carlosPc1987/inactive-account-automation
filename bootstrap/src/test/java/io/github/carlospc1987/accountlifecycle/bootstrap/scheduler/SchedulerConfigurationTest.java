package io.github.carlospc1987.accountlifecycle.bootstrap.scheduler;

import io.github.carlospc1987.accountlifecycle.application.account.inactivity.InactivityEvaluationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SchedulerConfigurationTest {

    @Test
    void givenInactivityEvaluationService_whenCreateSchedulerBean_thenReturnSchedulerInstance() {
        SchedulerConfiguration configuration = new SchedulerConfiguration();
        InactivityEvaluationService evaluationService = Mockito.mock(InactivityEvaluationService.class);

        InactivityEvaluationScheduler scheduler = configuration.inactivityEvaluationScheduler(evaluationService);

        assertNotNull(scheduler);
    }
}

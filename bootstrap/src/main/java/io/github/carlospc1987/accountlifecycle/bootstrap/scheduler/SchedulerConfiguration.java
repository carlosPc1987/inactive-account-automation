package io.github.carlospc1987.accountlifecycle.bootstrap.scheduler;

import io.github.carlospc1987.accountlifecycle.application.account.inactivity.InactivityEvaluationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

    @Bean
    InactivityEvaluationScheduler inactivityEvaluationScheduler(InactivityEvaluationService inactivityEvaluationService) {
        return new InactivityEvaluationScheduler(inactivityEvaluationService);
    }
}

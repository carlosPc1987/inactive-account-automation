package io.github.carlospc1987.accountlifecycle.bootstrap.scheduler;

import io.github.carlospc1987.accountlifecycle.application.account.inactivity.InactivityEvaluationService;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public class InactivityEvaluationScheduler {

    private final InactivityEvaluationService inactivityEvaluationService;
    private final Clock clock;

    public InactivityEvaluationScheduler(InactivityEvaluationService inactivityEvaluationService) {
        this(inactivityEvaluationService, Clock.systemUTC());
    }

    InactivityEvaluationScheduler(InactivityEvaluationService inactivityEvaluationService, Clock clock) {
        this.inactivityEvaluationService = Objects.requireNonNull(inactivityEvaluationService, "inactivityEvaluationService is required");
        this.clock = Objects.requireNonNull(clock, "clock is required");
    }

    @Scheduled(cron = "${scheduler.inactivity.cron:0 0 2 * * *}", zone = "${scheduler.inactivity.zone:UTC}")
    public void runDailyEvaluation() {
        Instant now = Instant.now(clock);
        inactivityEvaluationService.evaluate(now);
    }
}

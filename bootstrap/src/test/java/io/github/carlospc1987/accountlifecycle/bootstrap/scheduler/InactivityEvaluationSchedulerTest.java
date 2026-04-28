package io.github.carlospc1987.accountlifecycle.bootstrap.scheduler;

import io.github.carlospc1987.accountlifecycle.application.account.inactivity.InactivityEvaluationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InactivityEvaluationSchedulerTest {

    @Test
    void shouldTriggerEvaluationWithCurrentTime() {
        InactivityEvaluationService evaluationService = mock(InactivityEvaluationService.class);
        Instant now = Instant.parse("2026-04-28T00:00:00Z");
        Clock fixedClock = Clock.fixed(now, ZoneOffset.UTC);
        InactivityEvaluationScheduler scheduler = new InactivityEvaluationScheduler(evaluationService, fixedClock);
        when(evaluationService.evaluate(now)).thenReturn(5);

        scheduler.runDailyEvaluation();

        ArgumentCaptor<Instant> timeCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(evaluationService).evaluate(timeCaptor.capture());
        assertEquals(now, timeCaptor.getValue());
    }

    @Test
    void shouldUseDefaultClockInPublicConstructor() {
        InactivityEvaluationService evaluationService = mock(InactivityEvaluationService.class);
        InactivityEvaluationScheduler scheduler = new InactivityEvaluationScheduler(evaluationService);

        scheduler.runDailyEvaluation();

        ArgumentCaptor<Instant> timeCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(evaluationService).evaluate(timeCaptor.capture());
        assertNotNull(timeCaptor.getValue());
    }

    @Test
    void shouldFailWhenServiceIsNull() {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-04-28T00:00:00Z"), ZoneOffset.UTC);

        assertThrows(NullPointerException.class, () -> new InactivityEvaluationScheduler(null, fixedClock));
    }

    @Test
    void shouldFailWhenClockIsNull() {
        InactivityEvaluationService evaluationService = mock(InactivityEvaluationService.class);

        assertThrows(NullPointerException.class, () -> new InactivityEvaluationScheduler(evaluationService, null));
    }
}

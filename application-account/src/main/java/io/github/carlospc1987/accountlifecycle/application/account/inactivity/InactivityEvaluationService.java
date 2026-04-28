package io.github.carlospc1987.accountlifecycle.application.account.inactivity;

import java.time.Instant;

public interface InactivityEvaluationService {

    int evaluate(Instant evaluationTime);
}

package io.github.carlospc1987.accountlifecycle.domain.account.rules;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;

import java.time.Instant;

public interface InactivityRule {

    boolean shouldApply(Account account, Instant evaluationTime);

    void apply(Account account, Instant evaluationTime);
}

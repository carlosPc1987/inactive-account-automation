package io.github.carlospc1987.accountlifecycle.application.account.audit;

import io.github.carlospc1987.accountlifecycle.domain.account.audit.AccountAuditEvent;

import java.util.UUID;

public interface AccountAuditService {

    AccountAuditEvent recordEvent(UUID accountId, String eventType, String description);
}

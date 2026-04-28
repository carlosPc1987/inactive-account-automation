package io.github.carlospc1987.accountlifecycle.domain.account.audit;

public interface AccountAuditEventRepository {

    AccountAuditEvent save(AccountAuditEvent accountAuditEvent);
}

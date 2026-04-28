package io.github.carlospc1987.accountlifecycle.domain.account.audit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountAuditEventRepository {

    AccountAuditEvent save(AccountAuditEvent event);

    Optional<AccountAuditEvent> findById(UUID eventId);

    List<AccountAuditEvent> findAllByAccountId(UUID accountId);
}

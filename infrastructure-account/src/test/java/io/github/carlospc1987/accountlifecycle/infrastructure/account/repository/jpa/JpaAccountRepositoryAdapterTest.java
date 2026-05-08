package io.github.carlospc1987.accountlifecycle.infrastructure.account.repository.jpa;

import io.github.carlospc1987.accountlifecycle.domain.account.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaAccountRepositoryAdapterTest {

    @Mock
    private SpringDataAccountJpaRepository repository;

    private JpaAccountRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new JpaAccountRepositoryAdapter(repository);
    }

    @Test
    void givenDomainAccount_whenSave_thenReturnMappedDomainAccount() {
        Account account = account();
        JpaAccountEntity entity = entity(
                account.getId(),
                account.getEmail(),
                account.getCreatedAt(),
                account.getLastActivityAt(),
                account.isInactive()
        );
        when(repository.save(any(JpaAccountEntity.class))).thenReturn(entity);

        Account saved = adapter.save(account);

        assertEquals(account.getId(), saved.getId());
        assertEquals(account.getEmail(), saved.getEmail());
    }

    @Test
    void givenExistingId_whenFindById_thenReturnMappedDomainAccount() {
        Account account = account();
        when(repository.findById(account.getId())).thenReturn(Optional.of(entityFrom(account)));

        Optional<Account> found = adapter.findById(account.getId());

        assertTrue(found.isPresent());
        assertEquals(account.getId(), found.get().getId());
    }

    @Test
    void givenPersistedEntities_whenFindAll_thenReturnMappedDomainAccounts() {
        Account first = account();
        Account second = new Account(
                UUID.randomUUID(),
                "second@example.com",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z"),
                true
        );
        when(repository.findAll()).thenReturn(List.of(entityFrom(first), entityFrom(second)));

        List<Account> result = adapter.findAll();

        assertEquals(2, result.size());
        assertEquals(first.getId(), result.get(0).getId());
        assertEquals(second.getId(), result.get(1).getId());
    }

    @Test
    void givenAccountId_whenDeleteById_thenDelegateToSpringDataRepository() {
        UUID id = UUID.randomUUID();

        adapter.deleteById(id);

        verify(repository).deleteById(id);
    }

    private Account account() {
        return new Account(
                UUID.randomUUID(),
                "user@example.com",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z"),
                false
        );
    }

    private JpaAccountEntity entityFrom(Account account) {
        return entity(
                account.getId(),
                account.getEmail(),
                account.getCreatedAt(),
                account.getLastActivityAt(),
                account.isInactive()
        );
    }

    private JpaAccountEntity entity(UUID id, String email, Instant createdAt, Instant lastActivityAt, boolean inactive) {
        return new JpaAccountEntity(id, email, createdAt, lastActivityAt, inactive);
    }
}

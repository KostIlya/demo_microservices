package ru.t1.demo_t1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.t1.core.model.enums.StatusClientEnum;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.Client;
import ru.t1.demo_t1.model.enums.AccountStatusEnum;
import ru.t1.demo_t1.model.enums.AccountTypeEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT * FROM Account a WHERE a.client_id = :clientId",
            nativeQuery = true)
    Optional<Account> findByClientId(UUID clientId);
    @Query(value = "SELECT * FROM Account a WHERE a.account_id = :accountId",
            nativeQuery = true)
    Optional<Account> findByAccountId(UUID accountId);
    @Query(value = "SELECT * FROM Account a WHERE a.status = :#{#status.name()} LIMIT :limit",
            nativeQuery = true)
    List<Account> findByStatusAndLimit(AccountStatusEnum status, Long limit);
}

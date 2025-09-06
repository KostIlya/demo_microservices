package ru.t1.demo_t1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.t1.core.model.enums.TransactionStatusEnum;
import ru.t1.demo_t1.model.Account;
import ru.t1.demo_t1.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * FROM Transactions t WHERE t.account_id = :accountId",
            nativeQuery = true)
    List<Transaction> findByAccountId(UUID accountId);
    List<Transaction> findByAccountAccountIdAndStatus(UUID accountId, TransactionStatusEnum status);
    @Query(value = "SELECT * FROM Transactions t WHERE t.transaction_id = :transactionId",
            nativeQuery = true)
    Optional<Transaction> findByTransactionId(UUID transactionId);


}

package ru.t1.service2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.t1.service2.model.TransactionAccept;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionAcceptRepository extends JpaRepository<TransactionAccept, Long> {
    @Query(value = "SELECT * FROM transaction_accept WHERE client_id = :clientId AND account_id = :accountId " +
            "AND timestamp BETWEEN :start AND :end",
            nativeQuery = true)
    List<TransactionAccept> findByClientIdAndAccountIdAndPeriod(UUID clientId, UUID accountId,
                                                                          LocalDateTime start, LocalDateTime end);
}

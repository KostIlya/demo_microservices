package ru.t1.service2.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transaction_accept")
public class TransactionAccept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "client_id", nullable = false)
    private UUID clientId;
    @Column(name = "account_id", nullable = false)
    private UUID accountId;
    @Column(name = "transaction_id", nullable = false, unique = true)
    private UUID transactionId;
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
//    @Column(name = "amount", nullable = false)
//    private BigDecimal amount;
//    @Column(name = "balance", nullable = false)
//    private BigDecimal balance;
}

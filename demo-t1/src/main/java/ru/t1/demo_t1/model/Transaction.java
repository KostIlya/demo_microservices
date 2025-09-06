package ru.t1.demo_t1.model;

import jakarta.persistence.*;
import lombok.*;
import ru.t1.core.model.enums.TransactionStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account account;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum status;
    @Column(name = "transaction_id", nullable = false, unique = true)
    private UUID transactionId;
}

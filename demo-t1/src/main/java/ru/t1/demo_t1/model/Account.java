package ru.t1.demo_t1.model;

import jakarta.persistence.*;
import ru.t1.demo_t1.model.enums.AccountStatusEnum;
import ru.t1.demo_t1.model.enums.AccountTypeEnum;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id", nullable = false)
    private Client client;
    @Enumerated(EnumType.STRING)
    @Column(name = "type_account", nullable = false)
    private AccountTypeEnum type;
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatusEnum status;
    @Column(name = "account_id", nullable = false, unique = true)
    private UUID accountId;
    @Column(name = "frozen_amount")
    private BigDecimal frozenAmount;
}

package ru.t1.demo_t1.mapper;

import org.springframework.stereotype.Component;
import ru.t1.demo_t1.model.Transaction;
import ru.t1.core.model.event.TransactionAcceptEvent;
import ru.t1.core.model.event.TransactionResultEvent;

@Component
public class TransactionEventMapper {
    public TransactionAcceptEvent toTransactionAccept(Transaction transaction) {
        return TransactionAcceptEvent.builder()
                .transactionId(transaction.getTransactionId())
                .timestamp(transaction.getTimestamp())
                .amount(transaction.getAmount())
                .accountId(transaction.getAccount().getAccountId())
                .balance(transaction.getAccount().getBalance())
                .clientId(transaction.getAccount().getClient().getClientId())
                .build();
    }

    public TransactionResultEvent toTransactionResultWithoutStatus(TransactionAcceptEvent transactionAcceptEvent) {
        return TransactionResultEvent.builder()
                .transactionId(transactionAcceptEvent.getTransactionId())
                .accountId(transactionAcceptEvent.getAccountId())
                .build();
    }
}

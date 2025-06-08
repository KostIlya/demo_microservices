package ru.t1.service2.util;

import org.springframework.stereotype.Component;
import ru.t1.core.model.event.TransactionAcceptEvent;
import ru.t1.service2.model.TransactionAccept;

@Component
public class TransactionAcceptMapper {
    public TransactionAccept toEntity(TransactionAcceptEvent transactionAcceptEvent) {
        return TransactionAccept.builder()
                .transactionId(transactionAcceptEvent.getTransactionId())
                .timestamp(transactionAcceptEvent.getTimestamp())
                .clientId(transactionAcceptEvent.getClientId())
                .accountId(transactionAcceptEvent.getAccountId())
                .build();
    }

}

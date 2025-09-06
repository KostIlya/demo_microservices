package ru.t1.service2.kafka;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.core.model.event.TransactionAcceptEvent;
import ru.t1.service2.service.TransactionAcceptService;

@Component
@Slf4j
public class TransactionConsumer {

    @Autowired
    private TransactionAcceptService transactionAcceptService;

    @KafkaListener(topics = "t1_demo_transaction_accept")
    @Transactional
    public void handleTransactionInput(TransactionAcceptEvent transactionAcceptEvent) {
        log.info("t1_demo_transactions_accept: received transaction: {}", transactionAcceptEvent.getTransactionId());

        transactionAcceptService.getTransactionResult(transactionAcceptEvent);

    }
}

package ru.t1.demo_t1.kafka;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.t1.demo_t1.model.Transaction;
import ru.t1.core.model.event.TransactionAcceptEvent;
import ru.t1.demo_t1.mapper.TransactionEventMapper;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionProducer {
    private final KafkaTemplate<String, TransactionAcceptEvent> kafkaTemplate;

    @Autowired
    private TransactionEventMapper transactionEventMapper;
    @Transactional
    public String sendAccept(Transaction transaction) {
        log.info("sendAccept: beginning send to {}", "t1_demo_transaction_accept");
        String messageId = UUID.randomUUID().toString();
        TransactionAcceptEvent transactionAcceptEvent = transactionEventMapper.toTransactionAccept(transaction);

        CompletableFuture<SendResult<String, TransactionAcceptEvent>> future = kafkaTemplate
                .send("t1_demo_transaction_accept", messageId, transactionAcceptEvent);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Failed to send message to kafka. ");
                throw new IllegalStateException(exception);
            } else {
                log.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });

        return messageId;
    }

}

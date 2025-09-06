package ru.t1.service2.kafka;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.t1.core.model.event.TransactionResultEvent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionProducer {
    private final KafkaTemplate<String, TransactionResultEvent> kafkaTemplate;
    @Value("${spring.kafka.topic.demo-transaction-result}")
    private String topicSendResult;
    @Transactional
    public String sendResult(TransactionResultEvent transactionResultEvent) {
        log.info("sendResult: beginning send to {}", topicSendResult);
        String messageId = UUID.randomUUID().toString();

        CompletableFuture<SendResult<String, TransactionResultEvent>> future = kafkaTemplate
                .send(topicSendResult, messageId, transactionResultEvent);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Failed to send message to kafka");
                throw new IllegalStateException(exception);
            } else {
                log.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });

        return messageId;
    }

}

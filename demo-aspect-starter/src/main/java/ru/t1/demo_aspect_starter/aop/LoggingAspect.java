package ru.t1.demo_aspect_starter.aop;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.t1.demo_aspect_starter.mapper.DataSourceErrorLogMapper;
import ru.t1.demo_aspect_starter.model.DataSourceErrorLog;
import ru.t1.demo_aspect_starter.model.dto.DataSourceErrorLogDTO;
import ru.t1.demo_aspect_starter.repository.DataSourceErrorLogRepository;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {
    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

    private final DataSourceErrorLogMapper dataSourceErrorLogMapper;

    private final KafkaTemplate<String, DataSourceErrorLogDTO> kafkaTemplate;

    @AfterThrowing(pointcut = "@annotation(ru.t1.demo_aspect_starter.aop.annotation.LogDataSourceError)", throwing = "e")
    public void handleException(JoinPoint joinPoint, Exception e) {
        StringBuilder stackTrace = new StringBuilder();
        log.error("AFTER THROWING: " + joinPoint.getSignature().toShortString());
        for (var el : e.getStackTrace()) {
            stackTrace.append(el);
        }

        DataSourceErrorLogDTO dataSourceErrorLogDTO = DataSourceErrorLogDTO.builder()
                .message(e.getMessage())
                .methodSignature(joinPoint.getSignature().toString())
                .stacktrace(stackTrace.toString())
                .build();

        CompletableFuture<SendResult<String, DataSourceErrorLogDTO>> future = kafkaTemplate.send("t1_demo_metrics", "DATA_SOURCE", dataSourceErrorLogDTO);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                DataSourceErrorLog dataSourceErrorLog = dataSourceErrorLogMapper.toEntity(dataSourceErrorLogDTO);
                dataSourceErrorLogRepository.save(dataSourceErrorLog);
                log.error("Failed to send message to kafka. Saved in the database");
            } else {
                log.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });

    }
    @PostConstruct
    public void init() {
        log.info("LoggingAspect initialized {}", this);
    }
}

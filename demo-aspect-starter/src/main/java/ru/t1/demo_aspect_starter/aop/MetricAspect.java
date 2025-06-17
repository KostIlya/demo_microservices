package ru.t1.demo_aspect_starter.aop;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import ru.t1.demo_aspect_starter.config.MetricProperties;
import ru.t1.demo_aspect_starter.mapper.TimeLimitExceedLogMapper;
import ru.t1.demo_aspect_starter.model.TimeLimitExceedLog;
import ru.t1.demo_aspect_starter.model.dto.TimeLimitExceedLogDTO;
import ru.t1.demo_aspect_starter.repository.TimeLimitExceedLogRepository;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class MetricAspect {

    private KafkaTemplate<String, TimeLimitExceedLogDTO> kafkaTemplate;
    @Autowired
    private final TimeLimitExceedLogRepository timeLimitExceedLogRepository;
    @Autowired
    private final TimeLimitExceedLogMapper timeLimitExceedLogMapper;
    @Autowired
    private final MetricProperties metricConfig;
    @Around("@annotation(ru.t1.demo_aspect_starter.aop.annotation.Metric)")
    public Object timeRunningMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("timeRunningMethod(): running");
        long start = System.currentTimeMillis();

        Object proceed = proceedingJoinPoint.proceed();

        long end = System.currentTimeMillis();

        long executionTime = end - start;

        if (executionTime > metricConfig.getTimeLimit()) {
            TimeLimitExceedLogDTO timeLimitExceedLogDTO = TimeLimitExceedLogDTO.builder()
                    .executionTime(executionTime)
                    .limitTime(metricConfig.getTimeLimit())
                    .dateTime(LocalDateTime.now())
                    .methodSignature(proceedingJoinPoint.getSignature().toString())
                    .build();

            CompletableFuture<SendResult<String, TimeLimitExceedLogDTO>> future = kafkaTemplate
                    .send("t1_demo_metrics", "METRICS", timeLimitExceedLogDTO);

            future.whenComplete((result, exception) -> {
                if (exception != null) {
                    TimeLimitExceedLog timeLimitExceedLog = timeLimitExceedLogMapper.toEntity(timeLimitExceedLogDTO);
                    timeLimitExceedLogRepository.save(timeLimitExceedLog);
                    log.error("Failed to send message to kafka. Saved in the database");
                } else {
                    log.info("Message sent successfully: {}", result.getRecordMetadata());
                }
            });
        }
        log.info("timeRunningMethod(): completed. Method execution time {} ms", executionTime);
        return proceed;
    }
    @PostConstruct
    public void init() {
        log.info("MetricAspect initialized {}", this);
    }
}

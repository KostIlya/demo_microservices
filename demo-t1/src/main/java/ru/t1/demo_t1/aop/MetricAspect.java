package ru.t1.demo_t1.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.t1.demo_t1.config.MetricConfig;
import ru.t1.demo_t1.model.TimeLimitExceedLog;
import ru.t1.demo_t1.model.dto.TimeLimitExceedLogDTO;
import ru.t1.demo_t1.repository.TimeLimitExceedLogRepository;
import ru.t1.demo_t1.mapper.TimeLimitExceedLogMapper;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class MetricAspect {

    private KafkaTemplate<String, TimeLimitExceedLogDTO> kafkaTemplate;
    @Autowired
    private final TimeLimitExceedLogRepository timeLimitExceedLogRepository;
    @Autowired
    private final TimeLimitExceedLogMapper timeLimitExceedLogMapper;
    @Autowired
    private final MetricConfig metricConfig;
    @Around("@annotation(ru.t1.demo_t1.aop.annotation.Metric)")
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
}

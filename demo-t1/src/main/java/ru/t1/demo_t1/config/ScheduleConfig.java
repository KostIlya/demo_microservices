package ru.t1.demo_t1.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Data
@Slf4j
@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = "custom.schedule")
public class ScheduleConfig {
    private Long time;
    private Long amountClient;
    private Long amountAccount;
    @PostConstruct
    public void init() {
        log.info("ScheduleConfig: time - {}", time);
        log.info("ScheduleConfig: amountClient - {}", amountClient);
        log.info("ScheduleConfig: amountAccount - {}", amountAccount);
    }
}

package ru.t1.demo_aspect_starter.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "custom.metric")
public class MetricProperties {
    private Long timeLimit;

    @PostConstruct
    public void init() {
        log.info("Metric Properties initialized {}", this);
    }
}

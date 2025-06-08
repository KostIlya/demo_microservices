package ru.t1.demo_t1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "custom.metric")
public class MetricConfig {
    private Long timeLimit;
}

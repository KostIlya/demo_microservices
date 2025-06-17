package ru.t1.demo_aspect_starter.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.kafka.core.KafkaTemplate;
import ru.t1.demo_aspect_starter.aop.LoggingAspect;
import ru.t1.demo_aspect_starter.aop.MetricAspect;
import ru.t1.demo_aspect_starter.mapper.DataSourceErrorLogMapper;
import ru.t1.demo_aspect_starter.mapper.TimeLimitExceedLogMapper;
import ru.t1.demo_aspect_starter.model.dto.DataSourceErrorLogDTO;
import ru.t1.demo_aspect_starter.repository.DataSourceErrorLogRepository;
import ru.t1.demo_aspect_starter.repository.TimeLimitExceedLogRepository;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableConfigurationProperties(MetricProperties.class)
@ConditionalOnClass({MetricProperties.class, DataSource.class, JpaRepository.class})
public class AspectAutoConfiguration {

    @Bean
    public DataSourceErrorLogMapper dataSourceErrorLogMapper() {
        return new DataSourceErrorLogMapper();
    }
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DataSourceErrorLogRepository.class)
    public LoggingAspect loggingAspect(DataSourceErrorLogRepository dataSourceErrorLogRepository,
                                       DataSourceErrorLogMapper dataSourceErrorLogMapper,
                                       KafkaTemplate<String, DataSourceErrorLogDTO> kafkaTemplate) {
        return new LoggingAspect(dataSourceErrorLogRepository, dataSourceErrorLogMapper, kafkaTemplate);
    }

    @Bean
    public TimeLimitExceedLogMapper timeLimitExceedLogMapper() {
        return new TimeLimitExceedLogMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(TimeLimitExceedLogRepository.class)
    public MetricAspect metricAspect(TimeLimitExceedLogRepository timeLimitExceedLogRepository,
                                     TimeLimitExceedLogMapper timeLimitExceedLogMapper,
                                     MetricProperties metricProperties) {
        return new MetricAspect(timeLimitExceedLogRepository, timeLimitExceedLogMapper, metricProperties);
    }

    @PostConstruct
    public void init() {
        log.info("Aspect Auto Configuration initialized {}", this);
    }
}

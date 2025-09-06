package ru.t1.demo_aspect_starter.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.t1.demo_aspect_starter.model.dto.DataSourceErrorLogDTO;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableKafka
public class KafkaStarterConfig {
    @Autowired
    private Environment environment;
    @Value("spring.kafka.topic.demo-metrics")
    private String topicDemoMetric;

//    @Bean
//    ConsumerFactory<String, Object> consumerStarterFactory() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
//
//        config.put(JsonDeserializer.TRUSTED_PACKAGES,
//                environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"));
//
//        config.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.consumer.group-id"));
//        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, environment.getProperty("spring.kafka.consumer.properties.spring.json.value.default.type"));
//
//        return new DefaultKafkaConsumerFactory<>(config);
//    }
//
//    @Bean
//    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaStarterListenerContainerFactory
//            (ConsumerFactory<String, Object> consumerStarterFactory,
//             KafkaTemplate<String, Object> kafkaStarterTemplate) {
//        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(3000, 3));
//        errorHandler.addNotRetryableExceptions(IllegalStateException.class);
//
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerStarterFactory);
//        factory.setCommonErrorHandler(errorHandler);
//        return factory;
//    }
//
    @Bean
    KafkaTemplate<String, DataSourceErrorLogDTO> kafkaStarterTemplate(ProducerFactory<String, DataSourceErrorLogDTO> producerStarterFactory) {
        return new KafkaTemplate<>(producerStarterFactory);
    }

    @Bean
    ProducerFactory<String, DataSourceErrorLogDTO> producerStarterFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }


    @Bean
    NewTopic createStarterT1DemoMetrics() {
        return TopicBuilder.name(topicDemoMetric)
                .build();
    }
    @PostConstruct
    public void init() {
        log.info("Kafka Config initialized {}", this);
    }
}

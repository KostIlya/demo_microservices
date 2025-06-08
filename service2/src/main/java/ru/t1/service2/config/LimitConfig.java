package ru.t1.service2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LimitConfig {
    @Value("${custom.transaction.limit.T}")
    public Long limitTime;
    @Value("${custom.transaction.limit.N}")
    public Integer maxNumberTransactionsPerLimitTime;
}

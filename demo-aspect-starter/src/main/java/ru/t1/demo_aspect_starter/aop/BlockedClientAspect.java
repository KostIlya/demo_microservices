package ru.t1.demo_aspect_starter.aop;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.t1.demo_aspect_starter.aop.annotation.CountArrestedAccount;
import ru.t1.demo_aspect_starter.aop.annotation.CountBlockedClient;


@Slf4j
@Aspect
@Component
public class BlockedClientAspect {
    private static int quantity = 0;
    @After("@annotation(ru.t1.demo_aspect_starter.aop.annotation.CountBlockedClient)")
    public void countBlockedClient(JoinPoint joinPoint) {
        log.info("countBlockedClient: begin");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CountBlockedClient annotation = signature.getMethod()
                .getAnnotation(CountBlockedClient.class);
        boolean decrease = annotation.decrease();
        if (decrease) {
            quantity--;
            if (quantity < 0) {
                log.error("BlockedClientAspect: quantity < 0");
                throw new RuntimeException("BlockedClientAspect: quantity < 0");
            }
        } else {
            quantity++;
        }
        log.info("Quantity of blocked clients: {}", quantity);
    }
    @PostConstruct
    public void init() {
        log.info("BlockedClientAspect initialized {}", this);
    }
}

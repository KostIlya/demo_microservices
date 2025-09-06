package ru.t1.demo_aspect_starter.aop;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import ru.t1.demo_aspect_starter.aop.annotation.CountArrestedAccount;

@Slf4j
@Aspect
public class ArrestedAccountAspect {
    private static int quantity = 0;
    @After("@annotation(countArrestedAccount)")
    public void countArrestedAccount(JoinPoint joinPoint, CountArrestedAccount countArrestedAccount) {
        log.info("countArrestedAccount: begin");
        boolean decrease = countArrestedAccount.decrease();
        if (decrease) {
            quantity--;
            if (quantity < 0) {
                log.error("ArrestedAccountAspect: quantity < 0");
                throw new RuntimeException("ArrestedAccountAspect: quantity < 0");
            }
        } else {
            quantity++;
        }
        log.info("Quantity of arrested accounts: {}", quantity);
    }
    @PostConstruct
    public void init() {
        log.info("ArrestedAccountAspect initialized {}", this);
    }
}

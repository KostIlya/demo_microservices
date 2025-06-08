package ru.t1.demo_t1.aop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.t1.demo_t1.aop.annotation.Cached;
import ru.t1.demo_t1.config.CachedConfig;

import java.lang.reflect.Method;
import java.util.HashMap;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(10)
public class CachedAspect {
    @Autowired
    private final CachedConfig cachedConfig;
    private final HashMap<String, HashMap<String, CachedClass>> cacheMap;

    @Around("@annotation(ru.t1.demo_t1.aop.annotation.Cached)")
    public Object caching(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("cachedMethod: running");
        Method method = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();
        Cached cachedAnnotation = method.getAnnotation(Cached.class);

        String value = cachedAnnotation.value();
        String key = cachedAnnotation.key();
        key = extractKeyFromArguments(key, proceedingJoinPoint);
        if (value.equals("default")) {
            value = method.getName();
        }
        long currentTime = System.currentTimeMillis();

        if (cacheMap.containsKey(value) && cacheMap.get(value).containsKey(key)) {
            CachedClass cachedValue = cacheMap.get(value).get(key);
            long elapsedTime = currentTime - cachedValue.getCreatedTime();
            if (elapsedTime <= cachedConfig.getStorageTime()) {
                log.info("cachedMethod(): taken from the cache");
                return cacheMap.get(value).get(key).cachedObject;
            } else {
                cacheMap.get(value).remove(key);
            }
        }

        Object proceed = proceedingJoinPoint.proceed();
        cacheMap.putIfAbsent(value, new HashMap<>());
        cacheMap.get(value).putIfAbsent(key, new CachedClass(currentTime, proceed));
        log.info("cachedMethod(): the result is cached");
        return proceed;
    }

    private String extractKeyFromArguments(String key, ProceedingJoinPoint proceedingJoinPoint) {
        Object[] args = proceedingJoinPoint.getArgs();
        String[] argsNames = ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterNames();

        int indexArg = 0;
        if (args.length != 0) {
            while (!argsNames[indexArg].equals(key)) {
                indexArg++;
            }
            key = args[indexArg].toString();
        }

        return key;
    }

    @AllArgsConstructor
    @Getter
    private class CachedClass {
        private final long createdTime;
        private final Object cachedObject;
    }
}

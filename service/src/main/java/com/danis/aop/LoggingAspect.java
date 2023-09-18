package com.danis.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LoggingAspect {

    @Around("within(com.danis.service.*Service)")
    public Object addLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("Incoming parameters - {} method: {}",
                    Arrays.stream(joinPoint.getArgs()).toList().toString(),
                    joinPoint.getSignature()
            );
            Object result = joinPoint.proceed();
            log.info("Returning value {} method: {}",
                    result != null ? result.toString() : null,
                    joinPoint.getSignature()
            );
            return result;
        } catch (Throwable ex) {
            throw ex;
        } finally {
        }
    }
}

package com.avdei.spring1app.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class Executor {

    @AfterReturning(value = "Searcher.getPersonPointCut()",
    returning = "o")
    public void findPersonAdvice(JoinPoint joinPoint, Object o){
        log.info("Method {} is called", joinPoint.getSignature());
        log.info("Person is {}", o);
    }

    @Around("Searcher.authControllerPointCut()")
    public Object authControllerAdvice(ProceedingJoinPoint point){
        log.info("Method {} called", point.toShortString());
        try {
            Object result = point.proceed();
            log.info("Method {} ended successfully", point.getSignature());

            return result;
        } catch (Throwable e) {
            log.error("Error in method {}", point.getSignature());
            throw new RuntimeException(e);
        } finally {
            log.info("Method {} ended", point.toLongString());

        }
    }
}

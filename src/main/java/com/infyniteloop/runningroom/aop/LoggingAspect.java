package com.infyniteloop.runningroom.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.infyniteloop.runningroom.service..*(..))") // adjust the package as needed
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info(">> Entering {} with args={}", methodName, args);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed(); // execute the actual method
            long duration = System.currentTimeMillis() - start;

            log.info("<< Exiting {} with result={} | Execution time={} ms",
                    methodName, result, duration);

            return result;
        } catch (Throwable t) {
            long duration = System.currentTimeMillis() - start;
            log.error("Exception in {} after {} ms", methodName, duration, t);
            throw t;
        }
    }
}
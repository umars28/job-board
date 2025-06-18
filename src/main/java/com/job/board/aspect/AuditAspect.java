package com.job.board.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");

    @Before("@annotation(com.job.board.annotation.AuditLog)")
    public void logBefore(JoinPoint joinPoint) {
        auditLogger.info("AUDIT - Before executing: {} with args: {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    @AfterReturning(value = "@annotation(com.job.board.annotation.AuditLog)", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        auditLogger.info("AUDIT - After executing: {} with result: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }
}

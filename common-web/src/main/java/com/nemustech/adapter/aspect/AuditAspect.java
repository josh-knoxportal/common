package com.nemustech.adapter.aspect;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AuditAspect {
	protected static Log log = LogFactory.getLog(AuditAspect.class);

	private long startEventTime;
	private long endEventTime;

	@Before("AuditPointcuts.auditOperation()")
	public void logBefore(JoinPoint joinPoint) {
		log.info(Arrays.toString(joinPoint.getArgs()) + "(으)로 " + joinPoint.getSignature().getName() + "() 메서드 시작");
		startEventTime = System.currentTimeMillis();
	}

	@AfterReturning(pointcut = "AuditPointcuts.auditOperation()", returning = "result")
	public void logAfterReturing(JoinPoint joinPoint, Object result) {
		log.info(result + " 와 함께 " + joinPoint.getSignature().getName() + "() 메서드 종료");
		endEventTime = System.currentTimeMillis();
		log.info("Legacy Processed Time: " + (endEventTime - startEventTime));
	}

	@AfterThrowing(pointcut = "AuditPointcuts.auditOperation()", throwing = "e")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
		log.info(joinPoint.getSignature().getName() + "()에서 " + e + "예외 발생", e);
	}
}

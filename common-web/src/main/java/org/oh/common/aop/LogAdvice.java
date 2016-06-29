package org.oh.common.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.oh.common.util.Utils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 로깅 AOP
 * 
 * @author skoh
 */
public class LogAdvice {
	protected Log log = LogFactory.getLog(getClass());

	public void before(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		printLine(signature);
		log.debug(format("START", "[" + toString(signature) + "]"));

		if (signature instanceof MethodSignature) {
			Method method = ((MethodSignature) signature).getMethod();
			Annotation anno = AnnotationUtils.findAnnotation(method, RequestMapping.class);
			if (anno != null) {
				log.debug(format("REQUEST", anno.toString()));
			}
		}

		log.debug(format("INPUT",
				"[" + toShortString(signature) + "] " + ReflectionToStringBuilder.toString(joinPoint.getArgs())));
	}

	public void afterReturning(JoinPoint joinPoint, Object result) {
		Signature signature = joinPoint.getSignature();
		log.trace(format("OUTPUT", "[" + toShortString(signature) + "] " + ReflectionToStringBuilder.toString(result)));
	}

	public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
		Signature signature = joinPoint.getSignature();
		log.error(format("ERROR", "[" + toShortString(signature) + "]"), ex);
	}

	public void after(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		log.debug(format("END", "[" + toString(signature) + "]"));
		printLine(signature);
	}

	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature signature = joinPoint.getSignature();
		before(joinPoint);

		long start = System.currentTimeMillis();
		Object result = null;
		try {
			result = joinPoint.proceed();
			afterReturning(joinPoint, result);
		} catch (Throwable ex) {
			// 에러 로그는 Controller에 모아서 한번만 출력
			Annotation anno = AnnotationUtils.findAnnotation(signature.getDeclaringType(), Controller.class);
			if (anno != null) {
				afterThrowing(joinPoint, ex);
			}
			throw ex;
		} finally {
			log.debug(format("END", "[" + signature.toLongString() + "] Response Time : "
					+ (System.currentTimeMillis() - start) + " ms"));
			printLine(signature);
		}

		return result;
	}

	protected void printLine(Signature signature) {
		Annotation anno = AnnotationUtils.findAnnotation(signature.getDeclaringType(), Controller.class);
		if (anno == null) {
			log.debug("--------------------------------------------------");
		} else {
			log.debug("==================================================");
		}
	}

	protected String toString(Signature signature) {
		return signature.toString();
	}

	protected String toShortString(Signature signature) {
		return signature.toShortString();
	}

	protected String format(String mode, String message) {
		return String.format("%-7.7s %s", mode, message);
	}
}

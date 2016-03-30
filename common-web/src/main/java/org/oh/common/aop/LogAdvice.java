package org.oh.common.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.oh.common.util.Utils;

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
		log.info("START  [" + toString(signature) + "]");
		log.info("INPUT  [" + toShortString(signature) + "] " + Utils.toString(joinPoint.getArgs()));
	}

	public void afterReturning(JoinPoint joinPoint, Object result) {
		Signature signature = joinPoint.getSignature();
		log.debug("OUTPUT [" + toShortString(signature) + "] " + Utils.toString(result));
	}

	public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
		Signature signature = joinPoint.getSignature();
		log.error("ERROR  [" + toShortString(signature) + "]", ex);
	}

	public void after(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		log.info("END    [" + toString(signature) + "]");
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
			if (signature.getDeclaringTypeName().endsWith("Controller"))
				afterThrowing(joinPoint, ex);
			throw ex;
		} finally {
			log.info("END    [" + signature.toLongString() + "] Response Time : " + (System.currentTimeMillis() - start)
					+ " ms");
			printLine(signature);
		}

		return result;
	}

	protected void printLine(Signature signature) {
		if (signature.getDeclaringTypeName().endsWith("Controller"))
			log.info("==================================================");
		else
			log.info("--------------------------------------------------");
	}

	protected String toString(Signature signature) {
		return signature.toString();
	}

	protected String toShortString(Signature signature) {
		return signature.toShortString();
	}
}

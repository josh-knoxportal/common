package com.nemustech.common.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import com.nemustech.common.util.StringUtil;

/**
 * 로깅 AOP
 * 
 * @author skoh
 */
public class LogAdvice {
	protected Log log = LogFactory.getLog(getClass());

	public void before(JoinPoint joinPoint) {
		if (!log.isDebugEnabled())
			return;

		Signature signature = joinPoint.getSignature();

		printLine(signature);
		log.debug(format("START", "[" + toString(signature) + "]"));

		log.debug(format("INPUT",
				"[" + toShortString(signature) + "] " + StringUtil.toStringRecursive(joinPoint.getArgs())));
	}

	public void afterReturning(JoinPoint joinPoint, Object result) {
		if (!log.isTraceEnabled())
			return;

		Signature signature = joinPoint.getSignature();

		log.trace(format("OUTPUT", "[" + toShortString(signature) + "] " + StringUtil.toStringRecursive(result)));
	}

	public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
		if (!log.isDebugEnabled())
			return;

		Signature signature = joinPoint.getSignature();

		log.error(format("ERROR", "[" + toShortString(signature) + "]"), ex);
	}

	public void after(JoinPoint joinPoint) {
		if (!log.isDebugEnabled())
			return;

		Signature signature = joinPoint.getSignature();

		log.debug(format("END", "[" + toString(signature) + "]"));
		printLine(signature);
	}

	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		if (!log.isDebugEnabled())
			return joinPoint.proceed();

		Signature signature = joinPoint.getSignature();

		before(joinPoint);

		long start = System.currentTimeMillis();
		Object result = null;
		try {
			result = joinPoint.proceed();
			afterReturning(joinPoint, result);
		} catch (Throwable ex) {
			afterThrowing(joinPoint, ex);

			throw ex;
		} finally {
			log.debug(format("END", "[" + signature.toLongString() + "] Response Time: "
					+ (System.currentTimeMillis() - start) + " ms"));
			printLine(signature);
		}

		return result;
	}

	protected void printLine(Signature signature) {
		if (!log.isDebugEnabled())
			return;

		log.debug("--------------------------------------------------");
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

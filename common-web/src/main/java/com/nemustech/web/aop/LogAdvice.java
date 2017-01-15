package com.nemustech.web.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nemustech.common.util.StringUtil;
import com.nemustech.web.util.WebUtil;

/**
 * 로깅 AOP Web
 * 
 * @author skoh
 */
public class LogAdvice extends com.nemustech.common.aop.LogAdvice {
	/**
	 * Parameter, @RequestBody, HttpServletRequest, HttpSession 출력
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	protected static String toString(Method method, Object[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append(WebUtil.toJsonParameter());

		Annotation[][] paramAnnoss = method.getParameterAnnotations();
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null)
				continue;

			if (args[i] instanceof HttpServletRequest || args[i] instanceof HttpSession) {
				sb.append(", " + WebUtil.toJson(args[i]));
				continue;
			}

			for (Annotation paramAnno : paramAnnoss[i]) {
				sb.append("{\"" + args[i].getClass().getSimpleName() + "\":");
				if (RequestBody.class.isInstance(paramAnno)) {
					sb.append(StringUtil.toStringRecursiveJson(args[i], "conditionObj"));
				} else {
					sb.append(StringUtil.toString(args[i]));
				}
				sb.append("} ");
			}
		}

		return sb.toString();
	}

	@Override
	public void before(JoinPoint joinPoint) {
		if (!log.isDebugEnabled())
			return;

		Signature signature = joinPoint.getSignature();

		printLine(signature);
		log.debug(format("START", "[" + toString(signature) + "]"));

		Annotation anno = AnnotationUtils.findAnnotation(signature.getDeclaringType(), Controller.class);
		if (anno == null) {
			log.debug(
					format("INPUT", "[" + toShortString(signature) + "] " + StringUtil.toString2(joinPoint.getArgs())));
		} else {
			if (signature instanceof MethodSignature) {
				Method method = ((MethodSignature) signature).getMethod();
				anno = AnnotationUtils.findAnnotation(method, RequestMapping.class);
				if (anno != null) {
					log.debug(format("REQUEST", anno.toString()));
					log.debug(format("INPUT",
							"[" + toShortString(signature) + "] " + toString(method, joinPoint.getArgs())));
				}
			}
		}
	}

	@Override
	public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
		if (!log.isDebugEnabled())
			return;

		Signature signature = joinPoint.getSignature();

		// 에러 로그는 Controller에 모아서 한번만 출력
		Annotation anno = AnnotationUtils.findAnnotation(signature.getDeclaringType(), Controller.class);
		if (anno == null)
			return;

		super.afterThrowing(joinPoint, ex);
	}

	@Override
	protected void printLine(Signature signature) {
		if (!log.isDebugEnabled())
			return;

		Annotation anno = AnnotationUtils.findAnnotation(signature.getDeclaringType(), Controller.class);
		if (anno == null) {
			super.printLine(signature);
		} else {
			log.debug("==================================================");
		}
	}
}

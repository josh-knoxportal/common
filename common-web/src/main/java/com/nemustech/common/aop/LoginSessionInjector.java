package com.nemustech.common.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginSessionInjector {
	private static final Log log = LogFactory.getLog(LoginSessionInjector.class);

	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature signature = (MethodSignature) pjp.getSignature();

		@SuppressWarnings("rawtypes")
		Class[] paramTypes = signature.getParameterTypes();
		Object[] args = pjp.getArgs();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		log.debug(String.format("Method %s.%s gets called.", pjp.getTarget(), signature.getName()));

		for (int i = 0; i < paramTypes.length; ++i) {
			String paramClass = paramTypes[i].getName();

			if (paramClass.indexOf("HscLoginInfo") >= 0 || paramClass.indexOf("PcmLoginInfo") >= 0
					|| paramClass.indexOf("AdminInfo") >= 0) {
				if (auth != null && auth.getDetails() != null
						&& paramClass.equals(auth.getDetails().getClass().getName())) {
					args[i] = auth.getDetails();
					log.debug(
							String.format("Set details %s to parameter %d", auth.getDetails().getClass().getName(), i));
					break;
				}
			}
		}

		return pjp.proceed(args);
	}
}

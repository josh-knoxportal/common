package com.nemustech.common.aop;

import org.aspectj.lang.Signature;

/**
 * Spring 2.x 버전용
 * 
 * <pre>
 * - Spring 2.x 버전
 * signature.getClass() : class org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint$MethodSignatureImpl
 * signature.getDeclaringType() : class com.nemustech.common.service.impl.CommonServiceImpl
 * signature.getDeclaringTypeName() : com.nemustech.common.service.impl.CommonServiceImpl
 * signature.getModifiers() : 1
 * signature.getName() : getUser
 * signature.toLongString() : public com.nemustech.common.dto.UserInfoDTO com.nemustech.common.service.impl.CommonServiceImpl.getUser(org.springframework.ui.ModelMap)
 * signature.toShortString() : getUser
 * signature.toString() : org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint$MethodSignatureImpl@7133b288
 * 
 * - Spring 3.x 버전
 * signature.toShortString() : CommonServiceImpl.getUser(..)
 * signature.toString() : ModelAndView com.nemustech.common.service.impl.CommonServiceImpl.getUser(ModelMap)
 * </pre>
 */
public class LogAdvice2 extends LogAdvice {
	@Override
	protected String toShortString(Signature signature) {
		return signature.getDeclaringTypeName() + "." + signature.getName();
	}

	@Override
	protected String toString(Signature signature) {
		return signature.toLongString();
	}
}

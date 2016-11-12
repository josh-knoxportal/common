package com.nemustech.adapter.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AuditPointcuts {

	@Pointcut("@annotation(com.nemustech.adapter.aspect.AuditRequired)")
	public void auditOperation() {
	}
}

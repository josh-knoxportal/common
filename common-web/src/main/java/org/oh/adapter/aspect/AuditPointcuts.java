package org.oh.adapter.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AuditPointcuts {

	@Pointcut("@annotation(org.oh.adapter.aspect.AuditRequired)")
	public void auditOperation() {
	}
}

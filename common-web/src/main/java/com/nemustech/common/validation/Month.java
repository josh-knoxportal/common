package com.nemustech.common.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MonthConstraintValidator.class)
@Documented
public @interface Month {
	boolean allowEmpty() default false;

	boolean allowZeroPad() default false;

	String message() default "Month should be between 1 and 12";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

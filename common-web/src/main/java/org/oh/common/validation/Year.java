package org.oh.common.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearConstraintValidator.class)
@Documented
public @interface Year {
	boolean allowEmpty() default false;

	String message() default "Year should consist of digit characters.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

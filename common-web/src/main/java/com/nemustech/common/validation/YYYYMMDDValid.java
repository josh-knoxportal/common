package com.nemustech.common.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YYYYMMDDValidConstraintValidator.class)
@Documented
public @interface YYYYMMDDValid {
	boolean allowEmpty() default false;

	boolean allowZeroPadded() default false;

	String yearField();

	String monthField();

	String dayField();

	String message() default "{YYYYMMDDValid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		YYYYMMDDValid[] value();
	}
}

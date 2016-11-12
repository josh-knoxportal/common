package com.nemustech.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class YearConstraintValidator implements ConstraintValidator<Year, String> {
	private boolean allowEmpty = false;

	@Override
	public void initialize(Year year) {
		this.allowEmpty = year.allowEmpty();
	}

	@Override
	public boolean isValid(String field, ConstraintValidatorContext ctx) {
		if (StringUtils.isEmpty(field) && allowEmpty)
			return true;
		return field.matches("^[1-9]{1}[0-9]*$");
	}
}

package com.nemustech.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class MonthConstraintValidator implements ConstraintValidator<Month, String> {
	private boolean allowEmpty = false;
	private boolean allowZeroPad = false;

	@Override
	public void initialize(Month month) {
		this.allowEmpty = month.allowEmpty();
		this.allowZeroPad = month.allowZeroPad();
	}

	@Override
	public boolean isValid(String field, ConstraintValidatorContext ctx) {
		if (StringUtils.isEmpty(field) && allowEmpty)
			return true;

		String regex = null;

		if (allowZeroPad)
			regex = "(?=^1)^1[0-2]{0,1}$|((?=^0)^0[1-9]{1}|^[1-9]{1})$";
		else
			regex = "(?=^1)^1[0-2]{0,1}$|^[1-9]{1}$";

		return field.matches(regex);
	}
}

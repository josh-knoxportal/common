package com.nemustech.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import com.nemustech.common.exception.CommonException;

public class YYYYMMDDValidConstraintValidator implements ConstraintValidator<YYYYMMDDValid, Object> {
	final Integer[] daysPerMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private boolean allowEmpty = false;
	private boolean allowZeroPadded = false;
	private String yearField;
	private String monthField;
	private String dayField;

	@Override
	public void initialize(YYYYMMDDValid valid) {
		this.allowEmpty = valid.allowEmpty();
		this.allowZeroPadded = valid.allowZeroPadded();
		this.yearField = valid.yearField();
		this.monthField = valid.monthField();
		this.dayField = valid.dayField();
	}

	@Override
	public boolean isValid(Object cls, ConstraintValidatorContext cxt) {
		String year, month, day;

		try {
			year = BeanUtils.getProperty(cls, yearField);
			month = BeanUtils.getProperty(cls, monthField);
			day = BeanUtils.getProperty(cls, dayField);

			if (StringUtils.isNotEmpty(year)) {
				String yearRegex = "^[1-9]{1}[0-9]*$";

				if (allowZeroPadded)
					yearRegex = "^[0-9]{1}[0-9]*$";

				if (year.matches(yearRegex) == false)
					return false;

				if (StringUtils.isNotEmpty(month)) {
					String monthRegex = null;

					if (allowZeroPadded)
						monthRegex = "(?=^1)^1[0-2]{0,1}$|^[0]?[1-9]{1}$";
					else
						monthRegex = "(?=^1)^1[0-2]{0,1}$|^[1-9]{1}$";

					if (month.matches(monthRegex) == false)
						return false;

					if (StringUtils.isNotEmpty(day)) {
						int dayInt = Integer.parseInt(day);
						int lastDay;

						if (dayInt <= 0 || dayInt > 31)
							return false;

						lastDay = getLastDay(Integer.parseInt(year), Integer.parseInt(month));
						if (dayInt > lastDay)
							return false;

						return true;
					} else {
						return allowEmpty;
					}
				} else {
					return allowEmpty;
				}
			}
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR, e.getMessage(), e);
		}
		return allowEmpty;
	}

	private boolean isLeapYear(int year) {
		if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
			return true;
		return false;
	}

	private int getLastDay(int year, int month) {
		if (isLeapYear(year) && month == 2)
			return daysPerMonth[month - 1] + 1;
		return daysPerMonth[month - 1];
	}
}

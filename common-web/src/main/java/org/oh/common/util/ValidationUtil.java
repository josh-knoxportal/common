package org.oh.common.util;

import java.util.ArrayList;
import java.util.List;

import org.oh.web.common.Response;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;

public abstract class ValidationUtil extends ValidationUtils {
	public static <T> Response<T> getResponse(BindingResult errors) {
		String error_code = HttpStatus.BAD_REQUEST + " " + HttpStatus.BAD_REQUEST.getReasonPhrase();

		List<String> maessage = new ArrayList<String>();
		for (FieldError fieldError : errors.getFieldErrors()) {
			maessage.add(fieldError.getField() + " = " + fieldError.getDefaultMessage());
		}

		return Response.getFailResponse(error_code, maessage.toString());
	}
}

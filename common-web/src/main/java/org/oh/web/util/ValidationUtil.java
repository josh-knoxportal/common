package org.oh.web.util;

import java.util.ArrayList;
import java.util.List;

import org.oh.web.common.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;

/**
 * 유효성 체크 유틸
 * 
 * <pre>
 * 1. 에러코드 규격
 * - 에러구분(1) + "-" + 에러종류(4) + "-" + 에러순번(3) = 총 10 자리
 * - 에러구분 : W(Warning), E(Error), F(Fatal)
 * - 예) E-HTTP-001
 * </pre>
 */
public abstract class ValidationUtil extends ValidationUtils {
	public static final String CODE_SEPARATOR = "-";

	public static final String WARNING_CODE = "W";
	public static final String ERROR_CODE = "E";
	public static final String FATAL_CODE = "F";

	public static final String HTTP_CODE = "HTTP";

	public static <T> ResponseEntity<Response<T>> getResponseEntity(HttpStatus httpStatus) {
		String error_code = ERROR_CODE + CODE_SEPARATOR + HTTP_CODE + CODE_SEPARATOR + httpStatus.toString();
		String error_maessage = httpStatus.toString() + " " + httpStatus.getReasonPhrase();

		Response<T> response = Response.getFailResponse(error_code, error_maessage);

		return new ResponseEntity<Response<T>>(response, httpStatus);
	}

	public static <T> Response<T> getResponse(BindingResult errors) {
		String error_code = ERROR_CODE + CODE_SEPARATOR + HTTP_CODE + CODE_SEPARATOR
				+ HttpStatus.BAD_REQUEST.toString();
		String error_maessage = HttpStatus.BAD_REQUEST.toString() + " " + HttpStatus.BAD_REQUEST.getReasonPhrase();

		List<String> maessage = new ArrayList<String>();
		for (FieldError fieldError : errors.getFieldErrors()) {
			maessage.add(fieldError.getField() + " = " + fieldError.getDefaultMessage());
		}

		error_maessage += " " + maessage.toString();

		return Response.getFailResponse(error_code, error_maessage);
	}
}

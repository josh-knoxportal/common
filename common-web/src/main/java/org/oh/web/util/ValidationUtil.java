package org.oh.web.util;

import java.util.ArrayList;
import java.util.List;

import org.oh.common.util.Utils;
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
	public static final String MAESSAGE_SEPARATOR = "=>";

	public static final String WARNING_CODE = "W";
	public static final String ERROR_CODE = "E";
	public static final String FATAL_CODE = "F";

	public static final String HTTP_CODE = "HTTP";

	public static <T> ResponseEntity<Response<T>> getResponseEntity(HttpStatus httpStatus) {
		Response<T> response = Response.getFailResponse(getHttpErrorCode(httpStatus), getHttpErrorMaessage(httpStatus));

		return new ResponseEntity<Response<T>>(response, httpStatus);
	}

	public static <T> Response<T> getResponse(BindingResult errors) {
		List<String> maessage = new ArrayList<String>();
		for (FieldError fieldError : errors.getFieldErrors()) {
			maessage.add(fieldError.getField() + " = " + fieldError.getDefaultMessage());
		}

		return Response.getFailResponse(getHttpErrorCode(HttpStatus.BAD_REQUEST),
				getHttpErrorMaessage(HttpStatus.BAD_REQUEST, maessage.toString()));
	}

	public static String getHttpErrorCodeMaessage(HttpStatus httpStatus) {
		return getHttpErrorCodeMaessage(httpStatus, null);
	}

	/**
	 * ex) [E-HTTP-400] 400 Bad Request => [table = This parameter can not be used.]
	 * 
	 * @param httpStatus
	 * @param maessage
	 * @return
	 */
	public static String getHttpErrorCodeMaessage(HttpStatus httpStatus, String maessage) {
		return "[" + getHttpErrorCode(httpStatus) + "] " + getHttpErrorMaessage(httpStatus, maessage);
	}

	/**
	 * ex) E-HTTP-400
	 * 
	 * @param httpStatus
	 * @return
	 */
	public static String getHttpErrorCode(HttpStatus httpStatus) {
		return ERROR_CODE + CODE_SEPARATOR + HTTP_CODE + CODE_SEPARATOR + httpStatus.toString();
	}

	public static String getHttpErrorMaessage(HttpStatus httpStatus) {
		return getHttpErrorMaessage(httpStatus, null);
	}

	/**
	 * ex) 400 Bad Request => [table = This parameter can not be used.]
	 * 
	 * @param httpStatus
	 * @param maessage
	 * @return
	 */
	public static String getHttpErrorMaessage(HttpStatus httpStatus, String maessage) {
		String httpMaessage = httpStatus.toString() + " " + httpStatus.getReasonPhrase();
		httpMaessage += (Utils.isValidate(maessage)) ? " " + MAESSAGE_SEPARATOR + " " + maessage : "";

		return httpMaessage;
	}
}

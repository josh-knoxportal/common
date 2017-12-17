package com.nemustech.web.exception;

import org.springframework.http.HttpStatus;

import com.nemustech.common.exception.CommonException;
import com.nemustech.web.util.ValidationUtil;

/**
 * 웹 에러
 */
public class WebException extends CommonException {
	public WebException(HttpStatus httpStatus) {
		this(httpStatus, null);
	}

	public WebException(HttpStatus httpStatus, String errorMessage) {
		this(httpStatus, errorMessage, null);
	}

	public WebException(Throwable cause) {
		this(null, cause);
	}

	public WebException(String errorMessage, Throwable cause) {
		this(null, errorMessage, cause);
	}

	public WebException(HttpStatus httpStatus, String errorMessage, Throwable cause) {
		super(ValidationUtil.getHttpErrorCode(httpStatus, false),
				ValidationUtil.getHttpErrorMaessage(httpStatus, errorMessage), cause);
	}

	public static void main(String[] args) {
		new WebException(HttpStatus.BAD_REQUEST, "errorMessage1",
				new CommonException("code2", "errorMessage2", new CommonException("code3", "errorMessage3")))
						.printStackTrace();
	}
}

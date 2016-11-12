package com.nemustech.web.exception;

import com.nemustech.common.exception.CommonException;

/**
 * 웹 에러
 */
public class WebException extends CommonException {
	public WebException(String errorMessage) {
		super(errorMessage);
	}

	public WebException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public WebException(Throwable cause) {
		super(cause);
	}

	public WebException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
	}

	public WebException(String errorCode, String errorMessage, Throwable cause) {
		super(errorCode, errorMessage, cause);
	}
}

package com.nemustech.common.exception;

import com.nemustech.common.util.Utils;

/**
 * 공통 에러
 */
public class CommonException extends RuntimeException {
	public static final String PREFIX_USER = "U";
	public static final String PREFIX_SYSTEM = "S";
	public static final String ERROR = "ERROR";

	protected String errorCode = "";

	public CommonException(String errorMessage) {
		this("", errorMessage);
	}

	public CommonException(String errorCode, String errorMessage) {
		this(errorCode, errorMessage, null);
	}

	public CommonException(Throwable cause) {
		this(cause.getMessage(), cause);
	}

	public CommonException(String errorMessage, Throwable cause) {
		this("", errorMessage, cause);
	}

	/**
	 * @param errorCode 에러코드
	 * @param errorMessage 에러메시지
	 * @param cause 에러상세
	 */
	public CommonException(String errorCode, String errorMessage, Throwable cause) {
		super((Utils.isValidate(errorCode) ? "[" + errorCode + "]" : "") + errorMessage, cause);

		setErrorCode(errorCode);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
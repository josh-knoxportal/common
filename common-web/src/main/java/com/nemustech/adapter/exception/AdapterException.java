package com.nemustech.adapter.exception;

import com.nemustech.common.exception.CommonException;

/**
 * 어댑터 에러
 */
public class AdapterException extends CommonException {
	public AdapterException(String errorMessage) {
		super(errorMessage);
	}

	public AdapterException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public AdapterException(Throwable cause) {
		super(cause);
	}

	public AdapterException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
	}

	public AdapterException(String errorCode, String errorMessage, Throwable cause) {
		super(errorCode, errorMessage, cause);
	}
}

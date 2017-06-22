package com.nemustech.platform.lbs.common.exception;

import org.springframework.http.HttpStatus;


public class ApiResponseException extends Exception {
	private static final long serialVersionUID = -3338553096733187640L;
	private HttpStatus code = HttpStatus.OK;
	public ApiResponseException (HttpStatus code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public HttpStatus getCode() {
		return this.code;
	}
}

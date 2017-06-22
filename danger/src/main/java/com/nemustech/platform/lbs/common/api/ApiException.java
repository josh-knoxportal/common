package com.nemustech.platform.lbs.common.api;

@SuppressWarnings("serial")
public class ApiException extends Exception{
	@SuppressWarnings("unused")
	private int code;
	public ApiException (int code, String msg) {
		super(msg);
		this.code = code;
	}
}

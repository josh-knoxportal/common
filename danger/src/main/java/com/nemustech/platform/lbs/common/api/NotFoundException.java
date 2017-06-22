package com.nemustech.platform.lbs.common.api;

@SuppressWarnings("serial")
public class NotFoundException extends ApiException {
	@SuppressWarnings("unused")
	private int code;
	public NotFoundException (int code, String msg) {
		super(code, msg);
		this.code = code;
	}
}

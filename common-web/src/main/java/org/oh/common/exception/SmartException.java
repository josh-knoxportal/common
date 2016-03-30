package org.oh.common.exception;

@SuppressWarnings("serial")
public class SmartException extends Exception {
	/** 에러코드 */
	private String errorCode;

	public SmartException() {
		super();
	}

	public SmartException(String message) {
		super(message);
	}

	public SmartException(Throwable cause) {
		super(cause);
	}

	public SmartException(String message, Throwable cause) {
		super(message, cause);
	}

	public SmartException(String message, String errorCode) {
		super(message);

		this.errorCode = errorCode;
	}

	public SmartException(Throwable cause, String errorCode) {
		super(cause);

		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}

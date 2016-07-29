package org.oh.agent;

public class AgentException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String errorCode = "";

	public AgentException(String errorMessage) {
		this("", errorMessage);
	}

	public AgentException(String errorCode, String errorMessage) {
		this(errorCode, errorMessage, null);
	}

	public AgentException(Throwable cause) {
		this(cause.getMessage(), cause);
	}

	public AgentException(String errorMessage, Throwable cause) {
		this("", errorMessage, cause);
	}

	public AgentException(String errorCode, String errorMessage, Throwable cause) {
		super(errorMessage, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "[" + errorCode + "]" + super.toString();
	}
}
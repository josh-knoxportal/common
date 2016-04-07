package org.oh.web.common;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 응답 결과
 * 
 * @author skoh
 */
public class Response<T> {
	/**
	 * 헤더
	 */
	protected Header header;

	/**
	 * 바디
	 */
	protected T body;

	public static <T> Response<T> getSuccessResponse(T body) {
		return new Response<T>(new Header(true, "", ""), body);
	}

	public static <T> Response<T> getFailResponse(String error_code, String error_message) {
		return new Response<T>(new Header(false, error_code, error_message));
	}

	public Response() {
	}

	public Response(Header header) {
		this.header = header;
	}

	public Response(T body) {
		this.body = body;
	}

	public Response(Header header, T body) {
		this.header = header;
		this.body = body;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
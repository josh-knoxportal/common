package org.oh.web.common;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 응답 결과
 * 
 * @author skoh
 */
public class Response<T> implements Serializable {
	/**
	 * 헤더
	 */
	protected Header header;

	/**
	 * 바디
	 */
	protected T body;

	/**
	 * 실패 응답을 반환한다.
	 * 
	 * @param body
	 * @return
	 */
	public static <T> Response<T> getSuccessResponse(T body) {
		return new Response<T>(new Header(true, "", ""), body);
	}

	/**
	 * 성공 응답을 반환한다.
	 * 
	 * @param error_code
	 * @param error_message
	 * @return
	 */
	public static <T> Response<T> getFailResponse(String error_code, String error_message) {
		return new Response<T>(new Header(false, error_code, error_message));
	}

	/**
	 * 응답 결과를 변경한다.
	 * 
	 * @param response
	 * @param body
	 * @return
	 */
	public static <T1, T2> Response<T2> getResponse(Response<T1> response, T2 body) {
		Response<T2> result = null;

		Header header = response.getHeader();
		if (header.getSuccess_yn() == true) {
			result = getSuccessResponse(body);
		} else {
			result = getFailResponse(header.getError_code(), header.getError_message());
		}

		return result;
	}

	public Response() {
	}

	public Response(Header header) {
		this(header, null);
	}

	public Response(T body) {
		this(null, body);
	}

	public Response(Header header, T body) {
		setHeader(header);
		setBody(body);
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
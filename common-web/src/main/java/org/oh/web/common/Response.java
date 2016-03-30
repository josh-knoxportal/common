package org.oh.web.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 응답 결과
 * 
 * @author skoh
 */
public class Response {
	/**
	 * 헤더
	 */
	protected Header header = new Header(true, "", "");

	/**
	 * 바디
	 */
	protected Map<String, Object> body = new HashMap<String, Object>();

	public Response() {
	}

	public Response(Header header) {
		this.header = header;
	}

	public Response(Map<String, Object> body) {
		this.body = body;
	}

	public Response(Header header, Map<String, Object> body) {
		this.header = header;
		this.body = body;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Map<String, Object> getBody() {
		return body;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

	public void addObject(String key, Object value) {
		body.put(key, value);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
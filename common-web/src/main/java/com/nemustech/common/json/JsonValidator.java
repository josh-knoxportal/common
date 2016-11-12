package com.nemustech.common.json;

import java.util.List;

/**
 * Json Schema 검증 서비스를 위한 interface
 * 
 * 
 * @version 1.0.0
 * 
 */
public interface JsonValidator {
	/**
	 * Schema를 검증한다.
	 * 
	 * @param text 검증할 Json 문자열
	 * @param schema 검증에 사용할 Json의 Schema 문자열
	 * @return 검증 결과. <code>true</code>이면, Schema에 맞는 Json 문자열이다.
	 */
	abstract public boolean validate(String text, String schema);

	/**
	 * Schema 검증에 실패 시에 상세 이유를 돌려 준다.
	 * 
	 * @return 상세 이유를 문자열로 가진 {@code List<String>} 객체
	 */
	public List<String> errors();
}

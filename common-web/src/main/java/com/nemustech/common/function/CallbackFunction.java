package com.nemustech.common.function;

/**
 * 펑션 콜백 : 자바 스크립트의 콜백 펑션 기능
 * 
 * @param <T> 파라미터 타입
 * @param <R> 응답 타입
 * 
 * @author skoh
 */
public interface CallbackFunction<T, R> {
	/**
	 * 템플릿 실행
	 * 
	 * @param params 파라미터
	 * @return
	 * @throws Exception
	 */
	R executeTemplate(T params) throws Exception;
}
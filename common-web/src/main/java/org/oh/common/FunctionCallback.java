package org.oh.common;

/**
 * 펑션 콜백 : 자바 스크립트의 콜백 펑션 기능
 * 
 * @param <T1> 파라미터 타입
 * @param <T2> 응답 타입
 * 
 * @author skoh
 */
public interface FunctionCallback<T1, T2> {
	/**
	 * 템플릿 실행
	 * 
	 * @param params 파라미터
	 * @return
	 * @throws Exception
	 */
	public T2 executeTemplate(T1 params) throws Exception;
}
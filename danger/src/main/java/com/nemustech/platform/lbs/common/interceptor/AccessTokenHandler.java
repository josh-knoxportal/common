package com.nemustech.platform.lbs.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : AccessTokenHandler
 * 설   명 : App, Web  요청에 따른 Handler
 * 작 성 자 : 박진언(wwweojin@gmail.com)
 * 작성일자 : 2016-05-04
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2016-05-04    박진        최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public interface AccessTokenHandler {
	public String getRequestType();
	public boolean handle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception;
	

}

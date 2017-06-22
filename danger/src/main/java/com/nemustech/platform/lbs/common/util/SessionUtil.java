package com.nemustech.platform.lbs.common.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;

public class SessionUtil {

	/**
	 * attribute 값을 가져 오기 위한 method
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static Object getAttribute(String name) throws Exception {
		return RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * attribute 설정 method
	 * 
	 * @param name
	 * @param object
	 * @throws Exception
	 */
	public static void setAttribute(String name, Object object) throws Exception {
		RequestContextHolder.getRequestAttributes().setAttribute(name, object, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * 설정한 attribute 삭제
	 * 
	 * @param name
	 * @throws Exception
	 */
	public static void removeAttribute(String name) throws Exception {
		RequestContextHolder.getRequestAttributes().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getSessionId() throws Exception {
		return RequestContextHolder.getRequestAttributes().getSessionId();
	}
	
	
	
	/**
	 * account 정보 가져오기
	 * @return
	 * @throws Exception
	 */
	public static ResponseLoginVo getAccountInfo() throws Exception{		
		return  (ResponseLoginVo)getAttribute(DefaultController.SESSION_ATTR_KEY_ACCOUNT);
		
	}
	
	
	/**
	 * account 정보 설정하기
	 * @param responseLoginVo
	 * @throws Exception
	 */
	public static void setAccountInfo(ResponseLoginVo responseLoginVo) throws Exception{
		setAttribute(DefaultController.SESSION_ATTR_KEY_ACCOUNT,responseLoginVo);
	}
	

    /**
     * HttpSession에 로그인한 여부를 가져 온다.
     *
     * @return boolean - 로그인한 여부
     */
    public static boolean isLogin() throws Exception {
    	return getAccountInfo() == null ? false : true;
    }

}

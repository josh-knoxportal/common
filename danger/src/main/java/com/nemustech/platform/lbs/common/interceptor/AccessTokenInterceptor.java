/*
 * AccessTokenInterceptor.java
 * 
 * Copyright (c) 2015, Nemustech. All rights reserved.
 */
package com.nemustech.platform.lbs.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nemustech.platform.lbs.common.util.StringUtil;

public class AccessTokenInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AccessTokenInterceptor.class);

	@Autowired
	private AccessTokenHandlerManager handlerManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("Access {} from {}", request.getRequestURI(), request.getRemoteAddr());
		logger.info("request param = {}", request.getHeader("request_type"));
		String accessToken = request.getHeader("access_token");
		String requestType = request.getHeader("request_type");

		String uri = request.getRequestURI();
		if (uri.indexOf("/v1/wwms") != -1) {
			if (StringUtil.isEmpty(accessToken) && "1".equals(requestType)) {
				throwExceptionAuthError(request);
			}
		}

		AccessTokenHandler accessTokenHandler = handlerManager.get(requestType);
		if (accessTokenHandler == null) {
			throwExceptionAuthError(request);
		}

		accessTokenHandler.handle(request, response, handler);
		return super.preHandle(request, response, handler);
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
		logger.debug("Complete {} from {}", request.getRequestURI(), request.getRemoteAddr());
	}

	private void throwExceptionAuthError(HttpServletRequest request) throws ModelAndViewDefiningException {
		throw new ModelAndViewDefiningException(
				new ModelAndView("redirect:" + getURLWithContextPath(request) + "/auth_error.do"));
	}

	private String getURLWithContextPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}
}

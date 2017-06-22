package com.nemustech.platform.lbs.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.nemustech.platform.lbs.aams.service.AuthService;
import com.nemustech.platform.lbs.common.model.Response;

@Component
public class AppAccessTokenHandler implements AccessTokenHandler {

	private static final Logger logger = LoggerFactory.getLogger(AppAccessTokenHandler.class);
	@Autowired
	AuthService authService;

	@Override
	public String getRequestType() {
		return "1";
	}

	@Override
	public boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String uri = request.getRequestURI();
		logger.debug("======= AppAccessTokenHandler uri: " + uri);
		if (uri.indexOf("/auth_error.do") != -1 || uri.indexOf("/login.do") != -1
				|| uri.indexOf("/device_information.do") != -1 || uri.indexOf("/ngms/fcm_token_modify_event.do") != -1
				|| uri.indexOf("code_organization.do") != -1 || uri.indexOf("app_worker_register.do") != -1
				|| uri.indexOf("ecgi_and_beacon.do") != -1 || uri.indexOf("server_config/setting_list.do") != -1) {
			logger.debug("pass===");
			return true;
		}

		String system_type = "1";
		String access_token = request.getHeader("access_token");
		String device_no = request.getHeader("device_no");

		logger.info("request.getHeader('device_no')" + device_no);
		logger.info("request.getHeader('access_token')" + access_token);

		int result = authService.checkDeviceToken(system_type, device_no, access_token);
		logger.debug("======= AppAccessTokenHandler result: " + result);
		if (result == Response.OK) {
			return true;
		} else {
			throw new ModelAndViewDefiningException(new ModelAndView("redirect:" + "/auth_error.do"));
		}

	}

}

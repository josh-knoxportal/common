package com.nemustech.platform.lbs.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.nemustech.platform.lbs.aams.service.AuthService;
import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.util.SessionUtil;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;
import com.nemustech.platform.lbs.common.vo.SystemType;

@Component
public class WebAccessTokenHandler implements AccessTokenHandler {

	private static final Logger logger = LoggerFactory.getLogger(WebAccessTokenHandler.class);

	@Autowired
	AuthService authService;

	@Override
	public String getRequestType() {
		return "2";
	}

	@Override
	public boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// logger.debug("======= WebAccessTokenHandler" +
		// request.getRequestURI());
		String uri = request.getRequestURI();
		if (uri.indexOf("/auth_403.do") != -1 || uri.indexOf("/auth_error.do") != -1 || uri.indexOf("/login.do") != -1
				|| uri.indexOf("map_work_status.do") != -1 || uri.indexOf("restricted_check.do") != -1
				|| uri.indexOf("/searchId.do") != -1 || uri.indexOf("/resetPassword.do") != -1
				|| uri.indexOf("logout.do") != -1 || uri.indexOf("admincheck.do") != -1
				|| uri.indexOf("map_worker_status") != -1 || uri.indexOf("portalLogin.do") != -1
				|| uri.indexOf("mapper.do") != -1) {
			return true;
		}

		/*
		 * system_type 과 권한에 따라 접근 가능한 url 인지 확읺다.
		 **/
		if (!SessionUtil.isLogin()) {
			String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			logger.debug("Request Controller mapping::" + restOfTheUrl + ":: Start Time=" + System.currentTimeMillis());
			throw new ModelAndViewDefiningException(
					new ModelAndView("redirect:" + getURLWithContextPath(request) + "/admin/login.do"));
		}

		String cur_url = request.getHeader("cur_url");
		if (cur_url != null) {
			ResponseLoginVo responseLoginVo = SessionUtil.getAccountInfo();
			if (responseLoginVo == null)
				throw new NotFoundException(404, "");

			String sType = "" + SystemType.DANGER.getValue();
			if (sType.equals(responseLoginVo.getSystem_type())) {

				if (cur_url.indexOf("/sadmin") != -1) {
					if (responseLoginVo.getIs_admin() != 1) {
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
						return false;
					}
				}
				if (cur_url.indexOf("/admin") != -1) {
					if (responseLoginVo.getIs_admin() != 0) {
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
						return false;
					}

				}

			}

		}

		/*
		 * system_type 과 권한에 따라 접근 가능한 url 인지 확읺다.
		 **/
		ResponseLoginVo account = SessionUtil.getAccountInfo();
		String systemType = account.getSystem_type();

		if (uri.indexOf("/danger") != -1) {
			if (!systemType.equals("1")) {
				dangerAccessAuthException(request);
			}

			int is_admin = account.getIs_admin();
			if (uri.indexOf("/danger/sadmin") != -1 && is_admin == 0) {
				dangerAccessAuthException(request);
			}

			if (uri.indexOf("/danger/admin") != -1 && is_admin == 1) {
				dangerAccessAuthException(request);
			}
		} else {
			dangerAccessAuthException(request);
		}

		return true;
	}

	private void dangerAccessAuthException(HttpServletRequest request) throws ModelAndViewDefiningException {
		throw new ModelAndViewDefiningException(
				new ModelAndView("redirect:" + getURLWithContextPath(request) + "/auth_403.do"));
	}

	// private String getSystemPath(String url) {
	// return "";
	// }

	private String getURLWithContextPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}

}

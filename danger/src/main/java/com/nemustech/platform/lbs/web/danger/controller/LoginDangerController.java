/**
 * 
 */
package com.nemustech.platform.lbs.web.danger.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nemustech.platform.lbs.common.aop.PageTitle;
import com.nemustech.platform.lbs.common.controller.DefaultController;

/**
 * Handles requests for the application home page.
 *
 */
@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_ROOT, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class LoginDangerController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(LoginDangerController.class);

	@PageTitle("로그인")
	@RequestMapping(value = "/admin/login.do", method = RequestMethod.GET)
	public String login(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "danger/login/login";
	}

	@PageTitle("로그인")
	@RequestMapping(value = "/sadmin/login.do", method = RequestMethod.GET)
	public String slogin(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);
		return "danger/login/login";
	}

}

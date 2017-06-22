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
@Controller("DangerNoticeController")
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_ROOT+"/admin/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class NoticeController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

	@PageTitle("긴급 알림 전송")
	@RequestMapping(value = "notice.do", method = RequestMethod.GET)
	public String notice(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "danger/admin/notice/notice";
	}
	
}

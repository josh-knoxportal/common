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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nemustech.platform.lbs.common.aop.PageTitle;
import com.nemustech.platform.lbs.common.controller.DefaultController;

/**
 * Handles requests for the application home page.
 * 
 */
@Controller("DangerWorkController")
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_ROOT+"{admin}/", produces = MediaType.APPLICATION_JSON_VALUE
		+ ";charset=utf-8;")
public class WorkController extends DefaultController {

	private static final Logger logger = LoggerFactory
			.getLogger(WorkController.class);

	@PageTitle("작업자별 현황")
	@RequestMapping(value = "workStatus.do", method = RequestMethod.GET)
	public String workStatus(HttpServletRequest request, Model model)
			throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "danger/admin/work/workStatus";
	}

	@PageTitle("출입감지 현황")
	@RequestMapping(value = "workIssue.do", method = RequestMethod.GET)
	public String workIssue(HttpServletRequest request, Model model)
			throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "danger/admin/work/workIssue";
	}

	@PageTitle("작업자 관리")
	@RequestMapping(value = "worker.do", method = RequestMethod.GET)
	public String workerMnt(HttpServletRequest request, Model model,
			@PathVariable(value = "admin") String admin) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("admin: " + admin);

		if ("admin".equals(admin)) {
			return "danger/admin/work/workerMnt";
		}
		return "danger/sadmin/work/workerMnt";

	}

	@PageTitle("작업 관리")
	@RequestMapping(value = "work.do", method = RequestMethod.GET)
	public String workMnt(HttpServletRequest request, Model model,
			@PathVariable(value = "admin") String admin) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("admin: " + admin);

		if ("admin".equals(admin)) {
			return "danger/admin/work/workMnt";
		}
		return "danger/sadmin/work/workMnt";

	}
}

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
 */
@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_ROOT, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class MapDangerController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(MapDangerController.class);

	@PageTitle("위험지역관리")
	@RequestMapping(value = "/sadmin/restrict_area.do", method = RequestMethod.GET)
	public String restrictArea(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "danger/sadmin/map/RestrictArea";
	}

	@PageTitle("작업자 현장투입현황")
	@RequestMapping(value = "/admin/worker_status_map.do", method = RequestMethod.GET)
	public String workerStatusMap(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "danger/admin/map/WorkerStatusMap";
	}

	@PageTitle("작업 등록현황")
	@RequestMapping(value = "/admin/work_status_map.do", method = RequestMethod.GET)
	public String workStatusMap(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "danger/admin/map/WorkStatusMap";
	}
}

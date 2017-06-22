package com.nemustech.platform.lbs.web.vehicle.controller;

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
@RequestMapping(value = DefaultController.REQUESTMAPPING_VEHICLE_ROOT+"/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class AdminDeviceController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(AdminDeviceController.class);

	@PageTitle("장비등록")
	@RequestMapping(value = "admin/deviceInfo.do", method = RequestMethod.GET)
	public String accountInfo(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		// model.addAttribute("add_js", "ok");
		return "vehicle/admin/device/deviceList";
	}

	@PageTitle("등록 차량 관리")
	@RequestMapping(value = "admin/device.do", method = RequestMethod.GET)
	public String device(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "vehicle/admin/device/deviceList";
	}

	@PageTitle("등록 차량 관리")
	@RequestMapping(value = "sadmin/device.do", method = RequestMethod.GET)
	public String sdevice(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		return "vehicle/sadmin/device/deviceList";
	}
}

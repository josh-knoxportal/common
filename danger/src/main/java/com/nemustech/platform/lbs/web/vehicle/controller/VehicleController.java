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
public class VehicleController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

	@PageTitle("차량등록")
	@RequestMapping(value = "admin/vehicle.do", method = RequestMethod.GET)
	public String vehicle(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		model.addAttribute("add_datatables", "ok");
		return "vehicle/admin/vehicle/vehicleRegList";
	}
	
	@PageTitle("차량등록")
	@RequestMapping(value = "sadmin/vehicle.do", method = RequestMethod.GET)
	public String svehicle(HttpServletRequest request, Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: " + model);

		model.addAttribute("add_datatables", "ok");
		return "vehicle/sadmin/vehicle/vehicleRegList";
	}

}

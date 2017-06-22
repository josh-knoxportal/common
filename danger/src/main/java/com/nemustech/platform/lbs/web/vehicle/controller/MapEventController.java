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
@RequestMapping(value = DefaultController.REQUESTMAPPING_VEHICLE_ROOT+"/", produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8;")
public class MapEventController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(MapEventController.class);
	
	/***************************************************************************/
	@PageTitle("차량운행 현황")
	@RequestMapping(value = "admin/vehicle_driving.do", method = RequestMethod.GET)
	public String vehicleDriving(HttpServletRequest request,
			Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: "+model);
		
		return "vehicle/admin/map/vehicleDriving";
	}
	
	@PageTitle("차량운행 현황")
	@RequestMapping(value = "sadmin/vehicle_driving.do", method = RequestMethod.GET)
	public String svehicleDriving(HttpServletRequest request,
			Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: "+model);
		
		return "vehicle/sadmin/map/vehicleDriving";
	}
	
	/***************************************************************************/
	@PageTitle("차량운행이력 조회")
	@RequestMapping(value = "admin/history_vehicles.do", method = RequestMethod.GET)
	public String historyVehicles(HttpServletRequest request,
			Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: "+model);
		
		return "vehicle/admin/map/historyVehicleMap";
	}
	
	@PageTitle("차량운행이력 조회")
	@RequestMapping(value = "sadmin/history_vehicles.do", method = RequestMethod.GET)
	public String shistoryVehicles(HttpServletRequest request,
			Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: "+model);
		
		return "vehicle/admin/map/historyVehicleMap";
	}
	
	/***************************************************************************/
	@PageTitle("출입제한도로 설정")
	@RequestMapping(value = "admin/driving_rule.do", method = RequestMethod.GET)
	public String drivingRule(HttpServletRequest request,
			Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: "+model);
		
		return "vehicle/admin/map/drivingRule";
	}
	
	
	@PageTitle("출입제한도로 설정")
	@RequestMapping(value = "sadmin/driving_rule.do", method = RequestMethod.GET)
	public String sdrivingRule(HttpServletRequest request,
			Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: "+model);
		
		return "vehicle/sadmin/map/drivingRule";
	}
	
	/***************************************************************************/
	@PageTitle("제한속도 설정")
	@RequestMapping(value = "admin/speed_limit.do", method = RequestMethod.GET)
	public String speedLimit(HttpServletRequest request,
			Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: "+model);

		
		return "vehicle/admin/map/speedLimitMap";
	}
	
	@PageTitle("제한속도 설정")
	@RequestMapping(value = "sadmin/speed_limit.do", method = RequestMethod.GET)
	public String sspeedLimit(HttpServletRequest request,
			Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: "+model);

		
		return "vehicle/admin/map/speedLimitMap";
	}
	
	/***************************************************************************/
	/*
	 * [for Testing] 차량운행 정보에서 점 단위로 이력을 표시
	 * 정상서비스가 되면 삭제를 권고(디버깅용으로 두어도 무방)
	 */
	@PageTitle("차량운행 정보 for Testing")
	@RequestMapping(value = "admin/vehicle_driving_for_testing.do", method = RequestMethod.GET)
	public String vehicleDrivingForTesting(HttpServletRequest request,
			Model model) throws Exception {
		logger.debug("{} {}", request.getMethod(), request.getRequestURI());
		logger.debug("model: "+model);
		
		return "vehicle/admin/map/historyVehicleMapForTesting";
	}	
}

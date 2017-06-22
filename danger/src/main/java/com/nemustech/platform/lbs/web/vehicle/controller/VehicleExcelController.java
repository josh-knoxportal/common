package com.nemustech.platform.lbs.web.vehicle.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.ngms.service.MapEventApiService;
import com.nemustech.platform.lbs.ngms.vo.DrivingEventExcelVo;
import com.nemustech.platform.lbs.ngms.vo.VehicleStatusRequestVo;

import io.swagger.annotations.ApiParam;

@Controller("VehicleExcleController")
@RequestMapping(value = "/vehicle/excel/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class VehicleExcelController extends DefaultController {

	@Autowired
	MapEventApiService mapEventApiService;

	@RequestMapping(value = DefaultController.REQUESTMAPPING_VEHICLE_ROOT+"/", method = RequestMethod.GET)
	public String workerMntExcelDownload(
			@RequestParam(value = "target", required = false, defaultValue = "") String target,
			@ApiParam(value = "검색시작일", required = false) @RequestParam(value = "from", required = false, defaultValue = "0") String from,
			@ApiParam(value = "검색종료일", required = false) @RequestParam(value = "to", required = false, defaultValue = "0") String to,
			@ApiParam(value = "검색을 위한 위반유형", required = false) @RequestParam(value = "violation_type", required = false, defaultValue = "0") int violation_type,
			@ApiParam(value = "검색을 위한 차량번호", required = false) @RequestParam(value = "vehicle_no", required = false, defaultValue = "") String vehicle_no,
			Model ModelMap) throws NotFoundException {

		List<DrivingEventExcelVo> excelList = null;

		VehicleStatusRequestVo requestVo = new VehicleStatusRequestVo();
		requestVo.setFrom(from);
		requestVo.setTo(to);
		requestVo.setViolation_type(violation_type);
		requestVo.setVehicle_no(vehicle_no);
		try {
			excelList = mapEventApiService.drivingEventExcelList(requestVo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < excelList.size(); i++) {
			if (excelList.get(i).getDevice_no() != null) {
				String phone_num = excelList.get(i).getDevice_no();
				StringBuffer bf = new StringBuffer(phone_num);
				String num = bf.replace(phone_num.length() - 4, phone_num.length(), "****").toString();

				excelList.get(i).setDevice_no(num);
			}
		}

		ModelMap.addAttribute("excelList", excelList);
		ModelMap.addAttribute("target", target);

		return "excelDownloadService";
	}

}

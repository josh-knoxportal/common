package com.nemustech.platform.lbs.ngms.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.vo.SearchVo;
import com.nemustech.platform.lbs.ngms.model.VehicleReg;
import com.nemustech.platform.lbs.ngms.model.VehicleRegList;
import com.nemustech.platform.lbs.ngms.service.VehicleApiService;
import com.nemustech.platform.lbs.ngms.vo.VehicleRegVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = "/v1/ngms", produces = "application/json;charset=utf8;")
@Api(value = "/v1/ngms", description = "the v1 API")
public class VehicleApiController extends DefaultController {
	private static final Logger logger = LoggerFactory.getLogger(VehicleApiController.class);

	@Autowired
	VehicleApiService vehicleApiService;

	@ApiOperation(value = "", notes = "차량등록 정보를 가져온다.", response = VehicleRegList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "차량등록 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/vehicle_reg.do", method = RequestMethod.GET)
	public ResponseEntity<VehicleRegList> v1NgmsVehicleRegGetList(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "current page index (default_value is 0)", allowableValues = "{}") @RequestParam(value = "page_index", required = false, defaultValue = "0") int page_index,
			@ApiParam(value = "count per page (-1일 경우 모든 차량등록정보 가져오기) (default_value is 10)", allowableValues = "{}") @RequestParam(value = "record_count_per_page", required = false, defaultValue = "20") int record_count_per_page,
			@ApiParam(value = "단말기번호 검색 (default_value is \"\")") @RequestParam(value = "search_filter", required = false, defaultValue = "") String search_filter)
			throws NotFoundException {

		List<VehicleRegVo> dataList = null;
		int totalCnt = 0;
		try {
			SearchVo searchVo = new SearchVo();
			searchVo.setLimit_count(record_count_per_page);
			searchVo.setLimit_offset(page_index);
			searchVo.setSearch_filter(search_filter);
			dataList = vehicleApiService.vehicleRegList(searchVo);
			logger.info("dataList::" + dataList);
			totalCnt = vehicleApiService.vehicleRegListTotalCnt(searchVo);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<VehicleRegList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<VehicleRegList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<VehicleRegList>(HttpStatus.NO_CONTENT);
		} else {
			VehicleRegList vo = new VehicleRegList();
			vo.setMsg("SUCCESS");
			vo.setVehicleRegList(dataList);
			vo.setTotalCnt(totalCnt);
			return new ResponseEntity<VehicleRegList>(vo, HttpStatus.OK);
		}

	}

	@ApiOperation(value = "", notes = "차량등록 정보를 가져온다.", response = VehicleReg.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "차량등록 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/vehicle_reg/{device_uid}.do", method = RequestMethod.GET)
	public ResponseEntity<VehicleReg> v1NgmsVehicleRegGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "UID of Device", required = true, allowableValues = "{}") @PathVariable(value = "device_uid") String device_uid

	) throws NotFoundException {

		VehicleRegVo vehicleRegVo = new VehicleRegVo();
		;

		try {
			// logger.info("device_uid:"+device_uid);

			vehicleRegVo = new VehicleRegVo();
			vehicleRegVo.setDevice_uid(device_uid);
			vehicleRegVo = vehicleApiService.selectVehicleReg(vehicleRegVo);
			// logger.info("getDevice_uid"+vehicleRegVo.getDevice_uid());

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<VehicleReg>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<VehicleReg>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (vehicleRegVo == null) {
			return new ResponseEntity<VehicleReg>(HttpStatus.NO_CONTENT);
		} else {
			VehicleReg vo = new VehicleReg();
			vo.setMsg("SUCCESS");
			vo.setVehicleReg(vehicleRegVo);

			return new ResponseEntity<VehicleReg>(vo, HttpStatus.OK);
		}

	}

	@ApiOperation(value = "", notes = "차량정보를 등록/수정한다.", response = VehicleReg.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "단말기 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/vehicle_reg.do", method = RequestMethod.POST)
	public ResponseEntity<VehicleReg> v1NgmsVehiclePost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device info", required = true) @RequestBody VehicleRegVo vehicleReg)
			throws NotFoundException {
		VehicleReg vo = null;
		int result = 0;
		String msg = "SUCCESS"; // Return_Message.SUCCESS.getMessage();
		try {
			logger.info("VehicleRegVo:" + vehicleReg);
			result = vehicleApiService.updateVehicleReg(vehicleReg);
			// 증복오류
			// Return_Message.SUCCESS.getMessage();
			if (result == -999) {
				msg = "이미 등록된 차량입니다.";
			} else if (result == -888) {
				msg = "이미 운행중인 단말기 입니다.";
			} else if (result == -998) {
				msg = "차량번호를 입력하세요";
			}

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<VehicleReg>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<VehicleReg>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (vehicleReg == null) {
			return new ResponseEntity<VehicleReg>(HttpStatus.NO_CONTENT);
		} else {
			vo = new VehicleReg();
			vo.setCheckCode(result);
			vo.setMsg(msg);
			vo.setVehicleReg(vehicleReg);
			vo.setCheckCode(result);
			return new ResponseEntity<VehicleReg>(vo, HttpStatus.OK);
		}

	}

}

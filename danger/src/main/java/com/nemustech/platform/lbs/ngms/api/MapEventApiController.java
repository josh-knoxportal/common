package com.nemustech.platform.lbs.ngms.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.util.StringUtil;
import com.nemustech.platform.lbs.ngms.model.DrivingEventList;
import com.nemustech.platform.lbs.ngms.model.DrivingSectionList;
import com.nemustech.platform.lbs.ngms.model.RoadSectionCud;
import com.nemustech.platform.lbs.ngms.model.RoadSectionList;
import com.nemustech.platform.lbs.ngms.model.VehicleStatusList;
import com.nemustech.platform.lbs.ngms.model.ViolationHistoryList;
import com.nemustech.platform.lbs.ngms.service.MapEventApiService;
import com.nemustech.platform.lbs.ngms.vo.DrivingEventVo;
import com.nemustech.platform.lbs.ngms.vo.DrivingSectionVo;
import com.nemustech.platform.lbs.ngms.vo.RestrictAreaRequestVo;
import com.nemustech.platform.lbs.ngms.vo.RoadSectionListVo;
import com.nemustech.platform.lbs.ngms.vo.RoadSectionVo;
import com.nemustech.platform.lbs.ngms.vo.SpeedLimitRequestVo;
import com.nemustech.platform.lbs.ngms.vo.VehicleStatusRequestVo;
import com.nemustech.platform.lbs.ngms.vo.VehicleStatusVo;
import com.nemustech.platform.lbs.ngms.vo.ViolationHistoryVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = "/v1/ngms", produces = "application/json;charset=utf8;")
@Api(value = "/v1/ngms", description = "the v1 API")
public class MapEventApiController extends DefaultController {
	/*
	 * private static final Logger logger =
	 * LoggerFactory.getLogger(MapEventApiController.class);
	 */

	@Autowired
	MapEventApiService mapEventApiService;

	@ApiOperation(value = "", notes = "차량운행상태 정보를 가져온다.", response = VehicleStatusList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/vehicle_status.do", method = RequestMethod.GET)
	public ResponseEntity<VehicleStatusList> v1NgmsVehicleStatusListPost(
			@ApiParam(value = "현재운행중 리스트인지 조건 검색인지 여부", required = false) @RequestParam(value = "is_current", required = false, defaultValue = "0") int is_current,
			@ApiParam(value = "검색시작일", required = false) @RequestParam(value = "from", required = false, defaultValue = "0") String from,
			@ApiParam(value = "검색종료일", required = false) @RequestParam(value = "to", required = false, defaultValue = "0") String to,
			@ApiParam(value = "검색을 위한 위반유형", required = false) @RequestParam(value = "violation_type", required = false, defaultValue = "0") int violation_type,
			@ApiParam(value = "검색을 위한 차량번호", required = false) @RequestParam(value = "vehicle_no", required = false, defaultValue = "") String vehicle_no)
			throws NotFoundException {
		List<VehicleStatusVo> dataList = null;
		try {
			VehicleStatusRequestVo requestVo = new VehicleStatusRequestVo();
			requestVo.setIs_current(is_current);
			requestVo.setFrom(from);
			requestVo.setTo(to);
			requestVo.setViolation_type(violation_type);
			requestVo.setVehicle_no(vehicle_no);

			if (!requestVo.isValid()) {
				return new ResponseEntity<VehicleStatusList>(HttpStatus.BAD_REQUEST);
			}

			dataList = mapEventApiService.vehicleStatusList(requestVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<VehicleStatusList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<VehicleStatusList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<VehicleStatusList>(HttpStatus.NO_CONTENT);
		} else {
			VehicleStatusList vo = new VehicleStatusList();
			vo.setMsg("SUCCESS");
			vo.setVehicleStatusList(dataList);
			return new ResponseEntity<VehicleStatusList>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "차량의 위반이력을 가져온다.", response = ViolationHistoryList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/violation/{vehicle_uid}.do", method = RequestMethod.GET)
	public ResponseEntity<ViolationHistoryList> v1NgmsViolationHistoryListGet(
			@ApiParam(value = "자동차 식별자", required = true) @PathVariable(value = "vehicle_uid") String vehicle_uid)
			throws NotFoundException {
		List<ViolationHistoryVo> dataList = null;
		try {
			dataList = mapEventApiService.violationHistoryList(vehicle_uid);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ViolationHistoryList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<ViolationHistoryList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<ViolationHistoryList>(HttpStatus.NO_CONTENT);
		} else {
			ViolationHistoryList vo = new ViolationHistoryList();
			vo.setMsg("SUCCESS");
			vo.setViolationHistoryList(dataList);
			return new ResponseEntity<ViolationHistoryList>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "차량의 운행이력을 가져온다.", response = DrivingEventList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/driving_event/{vehicle_uid}.do", method = RequestMethod.GET)
	public ResponseEntity<DrivingEventList> v1NgmsDrivingEventListGet(
			@ApiParam(value = "자동차 식별자", required = true) @PathVariable(value = "vehicle_uid") String vehicle_uid)
			throws NotFoundException {
		List<DrivingEventVo> dataList = null;
		try {
			dataList = mapEventApiService.drivingEventList(vehicle_uid);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<DrivingEventList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<DrivingEventList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<DrivingEventList>(HttpStatus.NO_CONTENT);
		} else {
			DrivingEventList vo = new DrivingEventList();
			vo.setMsg("SUCCESS");
			vo.setDrivingEventList(dataList);
			return new ResponseEntity<DrivingEventList>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "도로 기준 차량의 운행이력을 가져온다.", response = DrivingSectionList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/driving_section/{vehicle_uid}.do", method = RequestMethod.GET)
	public ResponseEntity<DrivingSectionList> v1NgmsDrivingSectionListGet(
			@ApiParam(value = "자동차 식별자", required = true) @PathVariable(value = "vehicle_uid") String vehicle_uid)
			throws NotFoundException {
		List<DrivingSectionVo> dataList = null;
		try {
			dataList = mapEventApiService.drivingSectionList(vehicle_uid);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<DrivingSectionList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<DrivingSectionList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<DrivingSectionList>(HttpStatus.NO_CONTENT);
		} else {
			DrivingSectionList vo = new DrivingSectionList();
			vo.setMsg("SUCCESS");
			vo.setDrivingSectionList(dataList);
			return new ResponseEntity<DrivingSectionList>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "도로정보(좌표계,속성)를 가져온다.", response = RoadSectionList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/road_map.do", method = RequestMethod.GET)
	public ResponseEntity<RoadSectionList> v1NgmsRoadSectionListGet(
			@ApiParam(value = "도로정보요청시 제한도록구분 포함(optional)", required = false) @RequestParam(value = "vtype", required = false, defaultValue = "") String vehicle_type)
			throws NotFoundException {
		List<RoadSectionVo> dataList = null;

		try {
			RoadSectionListVo requestVo = new RoadSectionListVo();
			if (!StringUtil.isEmpty(vehicle_type)) {
				requestVo.setVehicle_type(vehicle_type);
			}
			dataList = mapEventApiService.roadSectionList(requestVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<RoadSectionList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<RoadSectionList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<RoadSectionList>(HttpStatus.NO_CONTENT);
		} else {
			RoadSectionList vo = new RoadSectionList();
			vo.setMsg("SUCCESS");
			vo.setRoadSectionList(dataList);
			return new ResponseEntity<RoadSectionList>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "제한속도를 설정한다.", response = RoadSectionCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/speed_limit.do", method = RequestMethod.POST)
	public ResponseEntity<RoadSectionCud> v1NgmsSpeedLimitPost(
			@ApiParam(value = "제한속도 변경 목록", required = true) @RequestBody SpeedLimitRequestVo requestVo)
			throws NotFoundException {
		try {
			mapEventApiService.updateSpeedLimit(requestVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<RoadSectionCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<RoadSectionCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		RoadSectionCud vo = new RoadSectionCud();
		vo.setMsg("SUCCESS");
		return new ResponseEntity<RoadSectionCud>(vo, HttpStatus.OK);
	}

	@ApiOperation(value = "", notes = "제한구역을 설정한다.", response = RoadSectionCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/restrict_area.do", method = RequestMethod.POST)
	public ResponseEntity<RoadSectionCud> v1NgmsRestrictAreaPost(
			@ApiParam(value = "제한도로구역 목록", required = true) @RequestBody RestrictAreaRequestVo requestVo)
			throws NotFoundException {
		try {
			mapEventApiService.updateRestrictArea(requestVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<RoadSectionCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<RoadSectionCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		RoadSectionCud vo = new RoadSectionCud();
		vo.setMsg("SUCCESS");
		return new ResponseEntity<RoadSectionCud>(vo, HttpStatus.OK);
	}

}

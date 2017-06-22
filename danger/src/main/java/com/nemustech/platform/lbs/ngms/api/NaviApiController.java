package com.nemustech.platform.lbs.ngms.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nemustech.platform.lbs.aams.service.AuthService;
import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.common.service.MapService;
import com.nemustech.platform.lbs.common.service.PropertyService;
import com.nemustech.platform.lbs.common.util.StringUtil;
import com.nemustech.platform.lbs.common.vo.MapVo;
import com.nemustech.platform.lbs.ngms.model.NaviDevice;
import com.nemustech.platform.lbs.ngms.model.NaviExtra;
import com.nemustech.platform.lbs.ngms.model.NaviMap;
import com.nemustech.platform.lbs.ngms.model.NaviVehicle;
import com.nemustech.platform.lbs.ngms.model.RoadLinkedList;
import com.nemustech.platform.lbs.ngms.model.RoadSectionList;
import com.nemustech.platform.lbs.ngms.service.MapEventApiService;
import com.nemustech.platform.lbs.ngms.service.NaviApiService;
import com.nemustech.platform.lbs.ngms.vo.AppDeviceInfoVo;
import com.nemustech.platform.lbs.ngms.vo.AppDrivingEventVo;
import com.nemustech.platform.lbs.ngms.vo.AppGpsEventVo;
import com.nemustech.platform.lbs.ngms.vo.AppVehicleRegVo;
import com.nemustech.platform.lbs.ngms.vo.AppViolationEventVo;
import com.nemustech.platform.lbs.ngms.vo.LineThresholdVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceVo;
import com.nemustech.platform.lbs.ngms.vo.RoadLinkedVo;
import com.nemustech.platform.lbs.ngms.vo.RoadSectionListVo;
import com.nemustech.platform.lbs.ngms.vo.RoadSectionVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = "/v1/ngms", produces = "application/json;charset=utf8;")
@Api(value = "/v1/ngms")
public class NaviApiController extends DefaultController {
	private static final Logger logger = LoggerFactory.getLogger(NaviApiController.class);

	@Autowired
	NaviApiService naviApiService;

	@Autowired
	MapService mapService;

	@Autowired
	MapEventApiService mapEventApiService;

	@Autowired
	AuthService authService;

	@Autowired
	PropertyService propertyService;

	@ApiOperation(value = "", notes = "네이게이션맵 정보를 가져온다.", response = NaviMap.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "네비게이션맵 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/map.do", method = RequestMethod.GET)
	public ResponseEntity<NaviMap> v1NgmsMapGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since,
			@ApiParam(value = "map uid") @RequestParam(value = "map_uid", required = false, defaultValue = "1") String map_uid)
			throws NotFoundException {
		long start = System.currentTimeMillis();

		HttpHeaders responseHeaders = new HttpHeaders();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		responseHeaders.add("Last-Modified", sdFormat.format(new Date()));

		HttpStatus statusCode = HttpStatus.OK;

		NaviMap resData = new NaviMap();
		ResponseEntity<NaviMap> response = null;
		try {
			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (mapService.getIsLastModifiedMap(map_uid, if_modified_since) == false) {
					statusCode = HttpStatus.NOT_MODIFIED;
					resData.setMsg("Not Modified");
					return new ResponseEntity<NaviMap>(resData, statusCode);
				}
			}

			MapVo naviMapVo = mapService.selectMap(map_uid);
			if (naviMapVo != null) {
				resData.setMsg(Return_Message.SUCCESS.getMessage());
				resData.setVersion(120);
				resData.setPolygon(4);
				resData.setCenter(naviMapVo.getCenter());
				resData.setPoints(naviMapVo.getPoints());
			} else {
				resData.setMsg(Return_Message.FAIL.getMessage() + "|조회된 데이터가 없습니다.");
				statusCode = HttpStatus.I_AM_A_TEAPOT;
			}
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<NaviMap>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			resData.setMsg(Return_Message.FAIL.getMessage());
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		response = new ResponseEntity<NaviMap>(resData, responseHeaders, statusCode);
		long end = System.currentTimeMillis();
		logger.info("get map end time:{}", (end - start) / 1000.0);

		return response;

	}

	// [안전운행 02] 부가 정보 조회
	@ApiOperation(value = "", notes = "부가정보를 가져온다.", response = NaviExtra.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "부가정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/extra.do", method = RequestMethod.GET)
	public ResponseEntity<NaviExtra> v1NgmsExtraGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since)
			throws NotFoundException {

		long start = System.currentTimeMillis();

		HttpHeaders responseHeaders = new HttpHeaders();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		responseHeaders.add("Last-Modified", sdFormat.format(new Date()));
		// 날짜
		logger.info(sdFormat.format(new Date()));

		HttpStatus statusCode = HttpStatus.OK;

		NaviExtra resData = new NaviExtra();
		ResponseEntity<NaviExtra> response = null;
		try {
			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (naviApiService.getIsLastModifiedExtra(if_modified_since) == false) {
					statusCode = HttpStatus.NOT_MODIFIED;
					resData.setMsg("Not Modified");
					return new ResponseEntity<NaviExtra>(resData, statusCode);
				}
			}

			List<LineThresholdVo> lineThresholdList = naviApiService.selectLineThresholdList();
			if (lineThresholdList != null) {
				resData.setMsg(Return_Message.SUCCESS.getMessage());
				resData.setLine_size_threshold(lineThresholdList);
			} else {
				resData.setMsg(Return_Message.FAIL.getMessage() + "|조회된 데이터가 없습니다.");
				statusCode = HttpStatus.I_AM_A_TEAPOT;
			}
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<NaviExtra>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			Return_Message.FAIL.getMessage();
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		response = new ResponseEntity<NaviExtra>(resData, responseHeaders, statusCode);
		long end = System.currentTimeMillis();
		logger.debug("get map end time:{}", (end - start) / 1000.0);

		return response;
	}

	@ApiOperation(value = "", notes = "도로정보를 가져온다.", response = RoadSectionList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "도로임계치 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "road_section.do", method = RequestMethod.GET)
	public ResponseEntity<RoadSectionList> v1NgmsRoadSectionListGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since)
			throws NotFoundException {

		long start = System.currentTimeMillis();

		HttpHeaders responseHeaders = new HttpHeaders();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		responseHeaders.add("Last-Modified", sdFormat.format(new Date()));
		// 날짜
		logger.info(sdFormat.format(new Date()));

		HttpStatus statusCode = HttpStatus.OK;

		RoadSectionList resData = new RoadSectionList();
		ResponseEntity<RoadSectionList> response = null;
		try {
			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (naviApiService.getIsLastModifiedLoadSection(if_modified_since) == false) {
					statusCode = HttpStatus.NOT_MODIFIED;
					resData.setMsg("Not Modified");
					return new ResponseEntity<RoadSectionList>(resData, statusCode);
				}
			}

			/*
			 * [2016/08/18] added 'new RoadSectionListVo()' by capsy 본 소스에서는
			 * default값으로 전달하면 된다. is_restrict_area문제로 인해서 vehicle_type이 있고 없고에
			 * 따라 is_restrict_area의 결과값이 영향을 받는다.
			 * 
			 * default(vehicle_type값이 없는 경우) : is_restrict_area는 해당 도로에 제한설정이
			 * 하나이상이면 1 vehicle_type값이 있는 경우는 해당 vehicle_type에 따라 결정
			 */
			List<RoadSectionVo> roadSectionList = mapEventApiService.roadSectionList(new RoadSectionListVo());
			if (roadSectionList != null) {
				resData.setMsg(Return_Message.SUCCESS.getMessage());
				resData.setRoadSectionList(roadSectionList);
			} else {
				resData.setMsg(Return_Message.FAIL.getMessage() + "|조회된 데이터가 없습니다.");
				statusCode = HttpStatus.I_AM_A_TEAPOT;
			}
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<RoadSectionList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			resData.setMsg(Return_Message.FAIL.getMessage());
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		response = new ResponseEntity<RoadSectionList>(resData, responseHeaders, statusCode);
		long end = System.currentTimeMillis();
		logger.debug("get map end time:{}", (end - start) / 1000.0);

		return response;
	}

	@ApiOperation(value = "", notes = "도로 연결정보를 가져온다.", response = RoadLinkedList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "도로임계치 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "road_linked_list.do", method = RequestMethod.GET)
	public ResponseEntity<RoadLinkedList> v1NgmsRoadLinkedListGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since)
			throws NotFoundException {

		long start = System.currentTimeMillis();

		HttpHeaders responseHeaders = new HttpHeaders();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		responseHeaders.add("Last-Modified", sdFormat.format(new Date()));
		// 날짜
		logger.info(sdFormat.format(new Date()));

		HttpStatus statusCode = HttpStatus.OK;

		RoadLinkedList resData = new RoadLinkedList();
		ResponseEntity<RoadLinkedList> response = null;
		try {
			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (naviApiService.getIsLastModifiedCrossSection(if_modified_since) == false) {
					statusCode = HttpStatus.NOT_MODIFIED;
					resData.setMsg("Not Modified");
					resData.setMsg(Return_Message.FAIL.getMessage());
					return new ResponseEntity<RoadLinkedList>(resData, statusCode);
				}
			}

			List<RoadLinkedVo> roadLinkedList = naviApiService.selectRoadLinkedList();
			if (roadLinkedList != null) {
				resData.setMsg(Return_Message.SUCCESS.getMessage());
				resData.setConnections(roadLinkedList);
			} else {
				resData.setMsg(Return_Message.FAIL.getMessage() + "|조회된 데이터가 없습니다.");
				statusCode = HttpStatus.I_AM_A_TEAPOT;
			}
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<RoadLinkedList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			resData.setMsg(Return_Message.FAIL.getMessage());
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		response = new ResponseEntity<RoadLinkedList>(resData, responseHeaders, statusCode);
		long end = System.currentTimeMillis();
		logger.debug("get map end time:{}", (end - start) / 1000.0);

		return response;
	}

	@ApiOperation(value = "", notes = "운행이벤트를 등록한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "운행이벤트 등록 성공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/drive_event.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1NgmsDrivingEventPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "vehicle uid", required = true) @RequestHeader(value = "vehicle_uid", required = true) String vehicle_uid,
			@ApiParam(value = "device phone number", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "driving event info", required = true) @RequestBody AppDrivingEventVo appDrivingEventVo)
			throws NotFoundException {
		ResponseData vo = new ResponseData();
		int result = 0;
		try {
			logger.info("v1NgmsDrivingEventPost vehicle_uid:" + vehicle_uid);
			logger.info("device_no:" + device_no);

			if (naviApiService.cntGoOutVehicle(vehicle_uid) > 0) {
				vo.setMsg(Return_Message.FAIL.getMessage() + "|이미 등록해제된 차량입니다.");
				return new ResponseEntity<ResponseData>(HttpStatus.I_AM_A_TEAPOT);
			}

			appDrivingEventVo.setVehicle_uid(vehicle_uid);
			appDrivingEventVo.setDevice_no(device_no);

			result = naviApiService.insertAppDrivingEvent(appDrivingEventVo);
			logger.info("naviApiService.insertAppDrivingEvent result : " + result);
			vo.setMsg("SUCCESS");

			if (result == 0) {
				// update가 안된 경우
				logger.info("appDrivingEventVo:" + appDrivingEventVo);
				vo.setMsg("FAIL|DB에 업데이트가 되지 않았습니다.");
				return new ResponseEntity<ResponseData>(HttpStatus.I_AM_A_TEAPOT);
			}
			return new ResponseEntity<ResponseData>(vo, HttpStatus.OK);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value = "", notes = "운행위반이벤트를 등록한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "운행위한이벤트 등록 성공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/violation_event.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1NgmsViolationEventPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "vehicle uid", required = true) @RequestHeader(value = "vehicle_uid", required = true) String vehicle_uid,
			@ApiParam(value = "device phone number", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "device info", required = true) @RequestBody AppViolationEventVo appViolationEventVo)
			throws NotFoundException {
		ResponseData vo = new ResponseData();
		try {
			logger.info("v1NgmsViolationEventPost vehicle_uid:" + vehicle_uid);
			logger.info("device_no:" + device_no);
			appViolationEventVo.setVehicle_uid(vehicle_uid);

			if (naviApiService.cntGoOutVehicle(vehicle_uid) > 0) {
				vo.setMsg(Return_Message.FAIL.getMessage() + "|이미 등록해제된 차량입니다.");
				return new ResponseEntity<ResponseData>(HttpStatus.I_AM_A_TEAPOT);
			}

			naviApiService.appViolationEvent(appViolationEventVo);
			vo.setMsg("SUCCESS");
			return new ResponseEntity<ResponseData>(vo, HttpStatus.OK);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value = "", notes = "GPS이벤트를 등록한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "GPS이벤트 등록성공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/gps_event.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1NgmsGpsEventPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "vehicle uid", required = true) @RequestHeader(value = "vehicle_uid", required = true) String vehicle_uid,
			@ApiParam(value = "device phone number", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "device info", required = true) @RequestBody AppGpsEventVo appGpsEventVo)
			throws NotFoundException {
		ResponseData vo = new ResponseData();
		int result = 0;
		try {
			logger.info("v1NgmsDrivingEventPost vehicle_uid:" + vehicle_uid);
			logger.info("device_no:" + device_no);

			appGpsEventVo.setVehicle_uid(vehicle_uid);

			result = naviApiService.updateGpsEvent(appGpsEventVo);
			vo.setMsg("SUCCESS");
			if (result < 1) {
				logger.info("appGpsEventVo:" + appGpsEventVo);
				vo.setMsg(Return_Message.FAIL.getMessage() + "|DB에 업데이트가 되지 않았습니다.");
				return new ResponseEntity<ResponseData>(HttpStatus.I_AM_A_TEAPOT);
			}

			return new ResponseEntity<ResponseData>(vo, HttpStatus.OK);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// [안전운행 04] 차량 운행 등록(시작)/
	@ApiOperation(value = "", notes = "차량을 등록(운행)한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "차량 등록(운행)성공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/register.do", method = RequestMethod.POST)
	public ResponseEntity<NaviVehicle> v1NgmsVehicleRegPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "vehicle info", required = true) @RequestBody AppVehicleRegVo appVehicleRegVo)
			throws NotFoundException {
		NaviVehicle vo = new NaviVehicle();
		int result = 0;
		String msg = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;
		try {

			// 차량등록 시나리오
			// 1. tbvc_device 테이블에 차량이 할당되어 있는지 확인한다. (이미 웹에서 등록되어 확인하는 경우, 앱에서
			// 차량번호까지 입력하는 경우)
			// device=01012341234, car=12345
			// 1.1 할당되지 않는 차량이라면 에러리턴 - 현재단말기는 번호랑 매핑이 되어 있습니다. 아니면 할당된 정보가
			// 없습니다.
			// --> // 일단 앱에서 할당된 내역을 확인하는지 앱에서 바로 등록하는 assigned 필드 추가.

			// 2. 할당이 되어 있으면 vehicle table에 insert
			// 2.1 이미 vehicle table에 데이터가 등록이 되어 있으면 (device테이블에 이미 폰과 차량이 할당이
			// 되어 있음) .. 이것도오류임.
			// 3. device_table 에 last_vehicle_id , push_id update
			// 4 성공리턴

			appVehicleRegVo.setDevice_no(device_no);

			logger.info("param appVehicleRegVo" + appVehicleRegVo);

			NaviDeviceVo naviDeviceVo = naviApiService.selectDeviceInfo(appVehicleRegVo.getDevice_no());
			if (naviDeviceVo == null) {
				statusCode = HttpStatus.I_AM_A_TEAPOT;
				msg = "시스템에 등록되지 않은 단말기입니다.";
				vo.setMsg(msg);
				vo.setVehicle_uid(appVehicleRegVo.getVehicle_uid());
				return new ResponseEntity<NaviVehicle>(vo, statusCode);

			}

			result = naviApiService.appRegVehicle(appVehicleRegVo);
			if (result < 0)
				statusCode = HttpStatus.I_AM_A_TEAPOT;

			if (result == -901)
				msg = "할당된 차량정보가 없습니다.";
			else if (result == -902)
				msg = "할당된 챠량정보가 일치하지 않습니다.";
			else if (result == -903)
				msg = "이미 할당된 단말기 입니다";

			vo.setMsg(msg);
			vo.setVehicle_uid(appVehicleRegVo.getVehicle_uid());

			logger.info("create vehicle_uid ..." + appVehicleRegVo.getVehicle_uid());

			return new ResponseEntity<NaviVehicle>(vo, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<NaviVehicle>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<NaviVehicle>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value = "", notes = "차량을 해제(종료)한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "차량 해제(종료)성공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/unregister.do", method = RequestMethod.POST)
	public ResponseEntity<NaviVehicle> v1NgmsVehicleReleasePost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "vehicle_uid", required = true) @RequestHeader(value = "vehicle_uid", required = false) String vehicle_uid,
			@ApiParam(value = "vehicle info", required = true) @RequestBody AppVehicleRegVo appVehicleRegVo)
			throws NotFoundException {
		NaviVehicle vo = new NaviVehicle();
		int result = 0;
		String msg = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;
		try {

			// 차량해제 시나리오
			// 1. tbvc_device 테이블에 차량이 할당되어 있는지 확인한다.
			// 1.1 할당되지 않는 차량이라면 에러리턴 - 현재단말기는 번호랑 매핑이 되어 있습니다. 아니면 할당된 정보가
			// 없습니다.
			// 2. vehicle 에 데이터가 있는지 확인한다.
			// 2.1 만약 데이터가 없다면 할당만 하고 등록은 안된 상태로..패스??
			// 2.2 데이터가 존재한다면 is_out = 1, go_out_date = now() 로 업데이트 한다.
			if (vehicle_uid != null && !"".equals(vehicle_uid))
				appVehicleRegVo.setVehicle_uid(vehicle_uid);
			if (device_no != null && !"".equals(device_no))
				appVehicleRegVo.setDevice_no(device_no);
			logger.info("v1NgmsVehicleReleasePost : " + appVehicleRegVo);
			result = naviApiService.appUnRegVehicle(appVehicleRegVo, request_type);
			if (result < 0)
				statusCode = HttpStatus.I_AM_A_TEAPOT;

			if (result == -901)
				msg = "할당된 차량정보가 없습니다.";
			else if (result == -902)
				msg = "할당된 챠량정보가 일치하지 않습니다.";

			vo.setMsg(msg);
			return new ResponseEntity<NaviVehicle>(vo, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<NaviVehicle>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<NaviVehicle>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// [안전운행 01] 단말기 정보 조회
	@ApiOperation(value = "", notes = "단말기정보를 전달한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "단말기정보 제공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/device_information.do", method = RequestMethod.POST)
	public ResponseEntity<NaviDevice> v1NgmsDeviceInfoGet(
			@ApiParam(value = "device_no", required = false) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device info", required = true) @RequestBody AppDeviceInfoVo appDeviceInfoVo)
			throws NotFoundException {
		NaviDevice vo = new NaviDevice();
		String msg = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;
		try {

			appDeviceInfoVo.setDevice_no(device_no);
			String apiKey = propertyService.getString("smartplant.vehicle.apikey");
			String packageName = propertyService.getString("smartplant.vehicle.package");

			// logger.info("packageName property val :"+packageName);

			if (StringUtil.isEmpty(appDeviceInfoVo.getPackage_name())
					|| !packageName.equals(appDeviceInfoVo.getPackage_name())) {
				statusCode = HttpStatus.FORBIDDEN;
				vo.setMsg(Return_Message.FAIL.getMessage() + "|패기지명이 없거나 값이 일치하지 않습니다.");
				return new ResponseEntity<NaviDevice>(vo, statusCode);
			}

			if (StringUtil.isEmpty(appDeviceInfoVo.getApp_key()) || !apiKey.equals(appDeviceInfoVo.getApp_key())) {
				statusCode = HttpStatus.FORBIDDEN;
				vo.setMsg(Return_Message.FAIL.getMessage() + "|app_key가 없거나 값이 일치하지 않습니다.");
				return new ResponseEntity<NaviDevice>(vo, statusCode);
			}

			NaviDeviceVo naviDeviceVo = naviApiService.selectDeviceInfo(appDeviceInfoVo.getDevice_no());
			if (naviDeviceVo == null) {
				statusCode = HttpStatus.UNAUTHORIZED;
				msg = "시스템에 등록되지 않은 단말기입니다.";
			}
			// [160818] 단말에서 해당 경우 처리, 서버쪽 제외 By Jung
			// else if ("".equals(naviDeviceVo.getAssigned_vehicle_no())
			// || naviDeviceVo.getAssigned_vehicle_no() == null) {
			// // statusCode = HttpStatus.I_AM_A_TEAPOT;
			// msg = "해당 단말기에 할당된 차량이 없습니다.";
			// String access_token = authService.generationDeviceToken("2",
			// appDeviceInfoVo.getDevice_no());
			// naviDeviceVo.setAccess_token(access_token);
			// vo.setNaviDevice(naviDeviceVo);
			//
			// }
			else {
				String access_token = authService.generationDeviceToken("2", appDeviceInfoVo.getDevice_no());
				naviDeviceVo.setAccess_token(access_token);
				vo.setNaviDevice(naviDeviceVo);
			}

			vo.setMsg(msg);
			return new ResponseEntity<NaviDevice>(vo, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<NaviDevice>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<NaviDevice>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

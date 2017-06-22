package com.nemustech.platform.lbs.wwms.api;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.model.DeviceEcgiList;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.common.service.PropertyService;
import com.nemustech.platform.lbs.common.util.StringUtil;
import com.nemustech.platform.lbs.common.vo.DeviceBeaconVo;
import com.nemustech.platform.lbs.common.vo.DeviceEcgiVo;
import com.nemustech.platform.lbs.ngms.model.MetaZoneList;
import com.nemustech.platform.lbs.ngms.model.NaviMap;
import com.nemustech.platform.lbs.ngms.service.VehicleAppApiService;
import com.nemustech.platform.lbs.ngms.vo.MetaZoneVo;
import com.nemustech.platform.lbs.wwms.model.CodeOrganiztionList;
import com.nemustech.platform.lbs.wwms.service.CommonCodeApiService;
import com.nemustech.platform.lbs.wwms.service.DangerApiService;
import com.nemustech.platform.lbs.wwms.service.DangerAppApiService;
import com.nemustech.platform.lbs.wwms.vo.AppAliveReportEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppConfigInfoEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppPwdEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppServerAccessEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppWorkerRegEventVo;
import com.nemustech.platform.lbs.wwms.vo.CodeOrganizationVo;
import com.nemustech.platform.lbs.wwms.vo.ServerConfigVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/*
 * 160725 [개인폰] [신규] 단말 연동 API
 */
@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_V1_ROOT, produces = "application/json;charset=utf8;")
@Api(value = DefaultController.REQUESTMAPPING_DANGER_V1_ROOT)
public class DangerAppApiController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(DangerAppApiController.class);

	@Autowired
	DangerApiService dangerApiService;

	@Autowired
	DangerAppApiService dangerAppApiService;

	@Autowired
	private VehicleAppApiService vehicleAppApiService;

	@Autowired
	PropertyService propertyService;

	@Autowired
	private CommonCodeApiService commonCodeApiService;

	// 160725 [개인폰] [신규] [출입금지 11] 단말 설정 정보 조회 API
	@ApiOperation(value = "", notes = "단말 설정정보를 전달한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "단말 설정정보 제공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/app_rule_property.do", method = RequestMethod.GET)
	public ResponseEntity<AppConfigInfoEventVo> v1WwmsAppRulePropertyInfoGet(
			@ApiParam(value = "device_no", required = false) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since)
			throws NotFoundException {

		AppConfigInfoEventVo responseVo = new AppConfigInfoEventVo();

		try {

			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (commonCodeApiService.getIsLastModifiedAppRuleProperty(if_modified_since) == false) {
					responseVo.setMsg("Not Modified");
					return new ResponseEntity<AppConfigInfoEventVo>(responseVo, HttpStatus.NOT_MODIFIED);
				}
			}

			ServerConfigVo serverConfig = commonCodeApiService.getServerConfig();
			if (StringUtils.isEmpty(serverConfig)) {
				return new ResponseEntity<AppConfigInfoEventVo>(HttpStatus.NO_CONTENT);
			}

			// then
			String aliveReportCycle = serverConfig.getAliveReportCycle(); // propertyService.getString("smartplant.app.config.alive_report_cycle");
			String locationMonitoringRule = serverConfig.getLocationMonitoringRule(); // propertyService.getString("smartplant.app.config.location_monitoring_rule");
			logger.debug("aliveReportCycle {{}] locationMonitoringRule [{}]", aliveReportCycle, locationMonitoringRule);

			// return
			responseVo.setMsg(Return_Message.SUCCESS.getMessage());
			responseVo.setAliveReportCycle(aliveReportCycle);
			responseVo.setLocationMonitoringRule(locationMonitoringRule);
			logger.debug("responseVo", responseVo.toString());
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<AppConfigInfoEventVo>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<AppConfigInfoEventVo>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<AppConfigInfoEventVo>(responseVo, HttpStatus.OK);
	}

	// 160803 [개인폰] [신규] [출입금지 12] alvie report event
	@ApiOperation(value = "", notes = "Alive Report 등록한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "live Report 등록한다."),
			@ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 418, message = "Data Error"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/app_alive_report.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwmsAppAliveReportPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "worker_uid", required = true) @RequestHeader(value = "worker_uid", required = false) String worker_uid,

			@ApiParam(value = "alive report info", required = true) @RequestBody AppAliveReportEventVo appAliveReportEventVo)
			throws NotFoundException {

		ResponseData vo = new ResponseData();
		int result = 0;
		String msg = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;

		logger.info("appAliveReportEventVo :" + appAliveReportEventVo.toString());

		try {
			// init
			appAliveReportEventVo.setDevice_no(device_no);

			if (StringUtil.isEmpty(worker_uid) == false)
				appAliveReportEventVo.setWorker_uid(worker_uid);

			// register
			result = dangerAppApiService.insertAppAliveReportInfo(appAliveReportEventVo);
			if (result < 0)
				statusCode = HttpStatus.I_AM_A_TEAPOT;

			// return
			vo.setMsg(msg);
			logger.info("vo:" + vo);

			return new ResponseEntity<ResponseData>(vo, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 160804 [개인폰] [신규] [출입금지 13] 부서/협력업체 정보
	@ApiOperation(value = "", notes = "부서/협력업체 리스트를 가져온다.", response = CodeOrganiztionList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/code_organization.do", method = RequestMethod.GET)
	public ResponseEntity<CodeOrganiztionList> v1WwmsCodeOrganizationListGet(
			@ApiParam(value = "device_no", required = false) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since)
			throws NotFoundException {
		List<CodeOrganizationVo> dataList = null;

		try {
			// check modified time
			// if (request_type.equals("1") == true &&
			// if_modified_since.length() > 0) {
			// if
			// (commonCodeApiService.getIsLastModifiedCodeOrganizationList(if_modified_since)
			// == false) {
			// CodeOrganiztionList responseVo = new CodeOrganiztionList();
			// responseVo.setMsg("Not Modified");
			// return new ResponseEntity<CodeOrganiztionList>(responseVo,
			// HttpStatus.NOT_MODIFIED);
			// }
			// }

			dataList = commonCodeApiService.getOrganizationList();
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<CodeOrganiztionList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CodeOrganiztionList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<CodeOrganiztionList>(HttpStatus.NO_CONTENT);
		} else {
			CodeOrganiztionList vo = new CodeOrganiztionList();
			vo.setMsg(Return_Message.SUCCESS.getMessage());
			vo.setOrganization(dataList);
			vo.setTotalCnt(dataList.size());
			return new ResponseEntity<CodeOrganiztionList>(vo, HttpStatus.OK);
		}
	}

	// 160804 [개인폰] [신규] [출입금지 14] 작업자 등록
	@ApiOperation(value = "", notes = "작업자를 등록한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업자 등록 성공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/app_worker_register.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwmsAppWorkerRegisterEventPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,

			@ApiParam(value = "AppWorkerRegEventVo info", required = true) @RequestBody AppWorkerRegEventVo appWorkerRegEventVo)
			throws NotFoundException {
		ResponseData response = new ResponseData();
		int result = 0;
		String returnMessage = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;

		try {

			// init
			appWorkerRegEventVo.setDevice_no(device_no);
			logger.debug("app worker vo [{}]", appWorkerRegEventVo.toString());

			// then
			result = dangerAppApiService.insertAppWorker(appWorkerRegEventVo);
			logger.info("result:" + result);

			if (result < 0)
				statusCode = HttpStatus.I_AM_A_TEAPOT;

			// return
			response.setMsg(returnMessage);
			return new ResponseEntity<ResponseData>(response, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 160804 [개인폰] [신규] [출입금지 16] FCM_token 갱신
	@ApiOperation(value = "", notes = "FCM_token 갱신", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "FCM_token 갱신"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/fcm_token_modify_event.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwmsFcmTokenModifyEventPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "passwd info", required = true) @RequestBody AppPwdEventVo fcmTokenEventVo)
			throws NotFoundException {
		ResponseData response = new ResponseData();
		int result = 0;
		String returnMessage = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;

		try {

			// init
			fcmTokenEventVo.setDevice_no(device_no);
			logger.debug("fcm token vo [{}]", fcmTokenEventVo.toString());

			// then
			result = dangerApiService.updatePushId(fcmTokenEventVo);
			logger.info("result:" + result);

			if (result < 0)
				statusCode = HttpStatus.I_AM_A_TEAPOT;

			// return
			response.setMsg(returnMessage);
			return new ResponseEntity<ResponseData>(response, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 160804 [개인폰] [신규] [출입금지 17] 단말 서버 연결 체크
	@ApiOperation(value = "", notes = "FCM_token 갱신", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "FCM_token 갱신"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/app_server_access_report.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwmAppServerAccessReportPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "passwd info", required = true) @RequestBody AppServerAccessEventVo appServerAccessEventVo)
			throws NotFoundException {
		ResponseData response = new ResponseData();
		int result = 0;
		String returnMessage = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;

		try {

			// init
			appServerAccessEventVo.setDevice_no(device_no);
			logger.debug("App Server Access Event Vo [{}]", appServerAccessEventVo.toString());

			// then
			result = dangerAppApiService.updateAppServerAccessTime(appServerAccessEventVo);
			logger.info("result:" + result);

			if (result < 0)
				statusCode = HttpStatus.I_AM_A_TEAPOT;

			// return
			response.setMsg(returnMessage);
			return new ResponseEntity<ResponseData>(response, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 160804 [개인폰] [신규] [안전운행12] 단말 ECGI 정보 조회
	@ApiOperation(value = "", notes = "단말 ECGI 정보 조회 리스트를 가져온다.", response = CodeOrganiztionList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/ecgi_and_beacon.do", method = RequestMethod.GET)
	public ResponseEntity<DeviceEcgiList> v1WwmsDeviceEcgiGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no)
			throws NotFoundException {
		List<DeviceEcgiVo> deviceEcgis = null;
		List<DeviceBeaconVo> deviceBeacons = null;

		try {
			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (commonCodeApiService.getIsLastModifiedEcgi(if_modified_since) == false) {
					DeviceEcgiList response = new DeviceEcgiList();
					response.setMsg("Not Modified");
					return new ResponseEntity<DeviceEcgiList>(response, HttpStatus.NOT_MODIFIED);
				}
			}

			deviceEcgis = commonCodeApiService.selectDeviceEcgiList();
			deviceBeacons = commonCodeApiService.selectDeviceBeaconList();

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<DeviceEcgiList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<DeviceEcgiList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (deviceEcgis == null) {
			return new ResponseEntity<DeviceEcgiList>(HttpStatus.NO_CONTENT);
		} else {
			DeviceEcgiList response = new DeviceEcgiList();
			response.setMsg(Return_Message.SUCCESS.getMessage());
			response.setEcgi(deviceEcgis);
			response.setBeacon(deviceBeacons);
			return new ResponseEntity<DeviceEcgiList>(response, HttpStatus.OK);
		}
	}

	// 160804 [개인폰] [신규] [안전운행 16] Meta Zone 정보 조회
	@ApiOperation(value = "", notes = "Meta Zone 정보 조회 가져온다.", response = NaviMap.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/meta_zone.do", method = RequestMethod.GET)
	public ResponseEntity<MetaZoneList> v1NgmsMetaZoneGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no)
			throws NotFoundException {
		long start = System.currentTimeMillis();

		logger.info("v1NgmsMetaZoneGet ..");

		HttpHeaders responseHeaders = new HttpHeaders();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		responseHeaders.add("Last-Modified", sdFormat.format(new Date()));

		// 날짜
		logger.info(sdFormat.format(new Date()));

		HttpStatus statusCode = HttpStatus.OK;

		MetaZoneList responseData = new MetaZoneList();
		ResponseEntity<MetaZoneList> response = null;

		try {

			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (vehicleAppApiService.getIsLastModifiedMetaZone(if_modified_since) == false) {
					statusCode = HttpStatus.NOT_MODIFIED;
					responseData.setMsg("Not Modified");
					return new ResponseEntity<MetaZoneList>(responseData, statusCode);
				}
			}

			List<MetaZoneVo> list = vehicleAppApiService.selectMetaZoneList();

			if (list != null) {
				responseData.setList(list);
				responseData.setMsg(Return_Message.SUCCESS.getMessage());
			} else {
				responseData.setMsg(Return_Message.FAIL.getMessage());
				statusCode = HttpStatus.BAD_REQUEST;
			}
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<MetaZoneList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMsg(Return_Message.FAIL.getMessage());
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		response = new ResponseEntity<MetaZoneList>(responseData, responseHeaders, statusCode);

		long end = System.currentTimeMillis();
		logger.info("get gps_zone end time:{}", (end - start) / 1000.0);

		return response;
	}
}

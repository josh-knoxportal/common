package com.nemustech.platform.lbs.wwms.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.nemustech.platform.lbs.common.service.PushService;
import com.nemustech.platform.lbs.common.util.StringUtil;
import com.nemustech.platform.lbs.common.vo.MapVo;
import com.nemustech.platform.lbs.common.vo.PushDeviceNoSendVo;
import com.nemustech.platform.lbs.ngms.model.NaviMap;
import com.nemustech.platform.lbs.ngms.vo.AppDeviceInfoVo;
import com.nemustech.platform.lbs.wwms.model.BeaconZone;
import com.nemustech.platform.lbs.wwms.model.DangerDevice;
import com.nemustech.platform.lbs.wwms.model.GpsZone;
import com.nemustech.platform.lbs.wwms.model.RestrictedMap;
import com.nemustech.platform.lbs.wwms.model.WorkReg;
import com.nemustech.platform.lbs.wwms.service.DangerApiService;
import com.nemustech.platform.lbs.wwms.service.DeviceDangerApiService;
import com.nemustech.platform.lbs.wwms.vo.AppGpsBleEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppPwdEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppWorkRegEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppWorkUnRegEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppZoneEventVo;
import com.nemustech.platform.lbs.wwms.vo.BeaconZoneVo;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerVo;
import com.nemustech.platform.lbs.wwms.vo.GpsZoneVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_V1_ROOT, produces = "application/json;charset=utf8;")
@Api(value = DefaultController.REQUESTMAPPING_DANGER_V1_ROOT, description = "the v1 API")
public class DangerApiController extends DefaultController {
	private static final Logger logger = LoggerFactory.getLogger(DangerApiController.class);

	@Autowired
	DangerApiService dangerApiService;

	@Autowired
	DeviceDangerApiService deviceDangerApiService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	MapService mapService;

	@Autowired
	PushService pushService;

	@Autowired
	AuthService authService;

	@Autowired
	PropertyService propertyService;

	// private static Map<String, String> restrictedMap = new HashMap<String,
	// String>();
	private static List<AppZoneEventVo> restrictedList = new ArrayList<AppZoneEventVo>();
	@SuppressWarnings("unused")
	private static boolean bInit = true;
	@SuppressWarnings("unused")
	private static int staticCnt = 0;

	@ApiOperation(value = "", notes = "위험지역맵 정보를 가져온다.", response = NaviMap.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "위험지역맵 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/map.do", method = RequestMethod.GET)
	public ResponseEntity<NaviMap> v1WwmsMapGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "map uid") @RequestParam(value = "map_uid", required = false, defaultValue = "2") String map_uid)
			throws NotFoundException {
		long start = System.currentTimeMillis();

		logger.info("v1WwmsMapGet  map_uid :" + map_uid);

		HttpHeaders responseHeaders = new HttpHeaders();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		responseHeaders.add("Last-Modified", sdFormat.format(new Date()));
		// 날짜
		logger.info(sdFormat.format(new Date()));

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
				statusCode = HttpStatus.BAD_REQUEST;
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

	@ApiOperation(value = "", notes = "위험지역 GPS Zone 정보를 가져온다.", response = NaviMap.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "GPS Zone정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/gps_zone.do", method = RequestMethod.GET)
	public ResponseEntity<GpsZone> v1WwmsGpsZoneGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no)
			throws NotFoundException {
		long start = System.currentTimeMillis();

		logger.info("v1WwmsGpsZoneGet ..");

		HttpHeaders responseHeaders = new HttpHeaders();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		responseHeaders.add("Last-Modified", sdFormat.format(new Date()));
		// 날짜
		logger.info(sdFormat.format(new Date()));

		HttpStatus statusCode = HttpStatus.OK;

		GpsZone resData = new GpsZone();
		ResponseEntity<GpsZone> response = null;
		try {
			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (dangerApiService.getIsLastModifiedGpsZone(if_modified_since) == false) {
					statusCode = HttpStatus.NOT_MODIFIED;
					resData.setMsg("Not Modified");
					return new ResponseEntity<GpsZone>(resData, statusCode);
				}
			}

			List<GpsZoneVo> list = dangerApiService.selectGpsZoneList();

			if (list != null) {
				resData.setList(list);
				resData.setMsg(Return_Message.SUCCESS.getMessage());

			} else {
				resData.setMsg(Return_Message.FAIL.getMessage());
				statusCode = HttpStatus.BAD_REQUEST;
			}
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<GpsZone>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			resData.setMsg(Return_Message.FAIL.getMessage());
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		response = new ResponseEntity<GpsZone>(resData, responseHeaders, statusCode);

		long end = System.currentTimeMillis();

		logger.info("get gps_zone end time:{}", (end - start) / 1000.0);

		return response;
	}

	@ApiOperation(value = "", notes = "위험지역 Beacon Zone 정보를 가져온다.", response = NaviMap.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "GPS Zone정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/beacon_zone.do", method = RequestMethod.GET)
	public ResponseEntity<BeaconZone> v1WwmsBeaconZoneGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no)
			throws NotFoundException {
		long start = System.currentTimeMillis();

		logger.info("v1WwmsBeaconZoneGet ..");

		HttpHeaders responseHeaders = new HttpHeaders();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		responseHeaders.add("Last-Modified", sdFormat.format(new Date()));
		// 날짜
		logger.info(sdFormat.format(new Date()));

		HttpStatus statusCode = HttpStatus.OK;

		BeaconZone resData = new BeaconZone();
		ResponseEntity<BeaconZone> response = null;
		try {
			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (dangerApiService.getIsLastModifiedBeaconZone(if_modified_since) == false) {
					statusCode = HttpStatus.NOT_MODIFIED;
					resData.setMsg("Not Modified");
					return new ResponseEntity<BeaconZone>(resData, statusCode);
				}
			}

			List<BeaconZoneVo> list = dangerApiService.selectBeaconZoneList();

			if (list != null) {
				resData.setList(list);
				resData.setMsg(Return_Message.SUCCESS.getMessage());
			} else {
				resData.setMsg(Return_Message.FAIL.getMessage());
				statusCode = HttpStatus.BAD_REQUEST;
			}
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<BeaconZone>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			resData.setMsg(Return_Message.FAIL.getMessage());
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		response = new ResponseEntity<BeaconZone>(resData, responseHeaders, statusCode);
		long end = System.currentTimeMillis();
		logger.info("get gps_zone end time:{}", (end - start) / 1000.0);
		return response;
	}

	// [단말API] [출입감지 08] 작업자 출입 이벤트
	@ApiOperation(value = "", notes = "작업자출입이벤트를 등록한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업자출입이벤트를 등록 성공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/enter_exit_event.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwmsZoneEventPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "work uid", required = true) @RequestHeader(value = "work_uid", required = true) String work_uid,
			@ApiParam(value = "AppZoneEventVo info", required = true) @RequestBody AppZoneEventVo appZoneEventVo)
			throws NotFoundException {
		ResponseData vo = new ResponseData();
		int result = 0;
		try {

			// [개인폰] [수정] 단말번호 추가
			appZoneEventVo.setDevice_no(device_no);
			appZoneEventVo.setWork_uid(work_uid);
			appZoneEventVo.setEvent_enter_type(1);
			dangerApiService.setAppZoneEventVo(appZoneEventVo);
			logger.info("v1WwmsZoneEventPost AppZoneEventVo:" + appZoneEventVo);

			// 현재 작업중인지 아닌지 체크
			if (dangerApiService.cntWorking(appZoneEventVo.getWork_uid()) == 0) {
				vo.setMsg("FAIL| 현재 작업중이 아닙니다.");
				return new ResponseEntity<ResponseData>(vo, HttpStatus.I_AM_A_TEAPOT);
			}

			// 작업이 할당된 경우 type =1
			result = dangerApiService.insertAppZoneEvent(appZoneEventVo, 1);

			vo.setMsg("SUCCESS");
			logger.info("result:" + result);
			return new ResponseEntity<ResponseData>(vo, HttpStatus.OK);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// 160804 [개인폰] [신규] [출입금지 15] 작업 미할당 출입 이벤트 --> 확인 필요
	@ApiOperation(value = "", notes = "작업 미할당 출입 이벤트 등록한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업 미할당 출입 이벤트 등록 성공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/work_unassigned_enter_exit_event.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwmsworkUnassignedEnterExitEventPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "worker uid", required = true) @RequestHeader(value = "worker_uid", required = true) String device_uid,
			@ApiParam(value = "AppZoneEventVo info", required = true) @RequestBody AppZoneEventVo appZoneEventVo)
			throws NotFoundException {
		ResponseData response = new ResponseData();
		int result = 0;
		String returnMessage = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;
		try {

			// [개인폰] [수정] 단말번호 추가
			appZoneEventVo.setDevice_no(device_no);
			appZoneEventVo.setDevice_uid(device_uid);
			logger.debug("--------------------------------------------------");
			appZoneEventVo.setWork_uid(device_uid);
			logger.debug("-------------------------------------------------- [{}]", appZoneEventVo.getWork_uid());
			appZoneEventVo.setEvent_enter_type(0); // 작업할당 없이 진출입
			dangerApiService.setAppZoneEventVo(appZoneEventVo);
			logger.info("v1WwmsworkUnassignedEnterExitEventPost AppZoneEventVo:" + appZoneEventVo);

			// 작업이 할당이 않 경우 typw = 0
			result = dangerApiService.insertAppZoneEvent(appZoneEventVo, 0);

			response.setMsg(returnMessage);
			logger.info("result:" + result);
			return new ResponseEntity<ResponseData>(response, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "", notes = "GPS,BLE on/off 이벤트를 등록한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "GPS이벤트 등록성공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/gps_beacon_status_event.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwmsGpsBLeEventPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "work uid", required = true) @RequestHeader(value = "work_uid", required = true) String work_uid,
			@ApiParam(value = "AppGpsBleEventVo info", required = true) @RequestBody AppGpsBleEventVo appGpsBleEventVo)
			throws NotFoundException {
		ResponseData vo = new ResponseData();
		int result = 0;
		try {

			appGpsBleEventVo.setWork_uid(work_uid);
			logger.info("v1WwmsGpsBLeEventPost :" + appGpsBleEventVo);
			result = dangerApiService.updateGpsEvent(appGpsBleEventVo);
			vo.setMsg("SUCCESS");
			logger.info("result:" + result);
			return new ResponseEntity<ResponseData>(vo, HttpStatus.OK);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value = "", notes = "패스워드 확인.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "패스워드 확인."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/work_check_password.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwmsPassCheckEventPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "passwd info", required = true) @RequestBody AppPwdEventVo appPwdEventVo)
			throws NotFoundException {

		ResponseData vo = new ResponseData();
		String rawPassword = null;
		byte[] decodedPassword = null;
		try {
			appPwdEventVo.setDevice_no(device_no);

			if (!StringUtils.isEmpty(appPwdEventVo.getIsenc())) {
				if ("1".equals(appPwdEventVo.getIsenc())) {
					decodedPassword = Base64.decodeBase64(appPwdEventVo.getPassword());
					rawPassword = new String(decodedPassword);
					logger.debug("rawPassword passwd [{}]", rawPassword);
				} else {
					rawPassword = appPwdEventVo.getPassword();
				}
			} else {
				rawPassword = appPwdEventVo.getPassword();
			}

			String password = deviceDangerApiService.selectDevicePasswordDanger();
			logger.debug("parameter passwd[{}]", password);

			if (passwordEncoder.matches(rawPassword, password)) {
				vo.setMsg("SUCCESS");
				dangerApiService.updatePushId(appPwdEventVo);
			} else {
				vo.setMsg("FAIL|비밀번호가 일치하지 않습니다.");
				return new ResponseEntity<ResponseData>(vo, HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<ResponseData>(vo, HttpStatus.OK);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// [출입금지 07] 작업을 등록(시작)
	@ApiOperation(value = "", notes = "작업을 등록(시작)한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업을 등록(시작)한다."),
			@ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 418, message = "Data Error"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/work_register_configure.do", method = RequestMethod.POST)
	public ResponseEntity<WorkReg> v1WwmsWorkRegPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "work_uid", required = true) @RequestHeader(value = "work_uid", required = false) String work_uid,
			@ApiParam(value = "work configure info", required = true) @RequestBody AppWorkRegEventVo appWorkRegEventVo)
			throws NotFoundException {
		WorkReg vo = new WorkReg();
		int result = 0;
		String msg = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;
		try {

			if (StringUtil.isEmpty(work_uid) == false)
				appWorkRegEventVo.setWork_uid(work_uid);

			appWorkRegEventVo.setDevice_no(device_no);
			logger.info("v1WwmsWorkRegPost :" + appWorkRegEventVo);
			result = dangerApiService.appRegWork(appWorkRegEventVo, request_type);
			logger.info("appWorkRegEventVo===" + appWorkRegEventVo);

			if (result < 0)
				statusCode = HttpStatus.I_AM_A_TEAPOT;

			if (result == -901)
				msg = "FAIL|존재하지 않거나 이미 완료된 작업번호입니다.";
			else if (result == -902)
				msg = "FAIL|이미 할당된 작업이 있는 단말기 입니다.";

			vo.setMsg(msg);
			vo.setWork_uid(appWorkRegEventVo.getWork_uid());
			vo.setZone_type(appWorkRegEventVo.getZone_type());

			if ("beacon".equals(appWorkRegEventVo.getZone_type())) {
				vo.setZone_id(appWorkRegEventVo.getZone_id());
			} else {
				vo.setZone_id(appWorkRegEventVo.getFactory_uid());
			}

			logger.info("vo:" + vo);
			return new ResponseEntity<WorkReg>(vo, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkReg>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkReg>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// [출입감지 10] 작업 해제(종료)
	@ApiOperation(value = "", notes = "작업을 해제(종료)한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업을 해제(종료)성공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/work_unregister.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwsWorkReleasePost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "work uid", required = true) @RequestHeader(value = "work_uid", required = true) String work_uid,
			@ApiParam(value = "work info", required = true) @RequestBody AppWorkUnRegEventVo appWorkUnRegEventVo)
			throws NotFoundException {
		ResponseData vo = new ResponseData();
		int result = 0;
		String msg = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;
		try {

			appWorkUnRegEventVo.setDevice_no(device_no);
			appWorkUnRegEventVo.setWork_uid(work_uid);

			logger.info("v1WwsWorkReleasePost :" + appWorkUnRegEventVo);

			result = dangerApiService.appUnRegWork(appWorkUnRegEventVo, request_type);

			if (result < 0)
				statusCode = HttpStatus.I_AM_A_TEAPOT;

			if (result == -901)
				msg = "FAIL|할당된 작업정보가 없습니다.";
			else if (result == -902)
				msg = "FAIL|할당된 작업정보가 일치하지 않습니다.";

			vo.setMsg(msg);
			logger.info("statusCode:" + statusCode);
			// removeRestrictedVo(work_uid); 0909
			// restrictedMap.remove(work_uid);

			return new ResponseEntity<ResponseData>(vo, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/*
	 * 160910 5초 주기로 받을때 호출하여 리턴한다. 기존 입/출입 이벤트에서는 추가를 뺀다.
	 */
	@ApiOperation(value = "", notes = "비인가자 출입한정보가 있는지 확인.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "비인가자 출입정보"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/restricted_check.do", method = RequestMethod.GET)
	public ResponseEntity<RestrictedMap> v1WwmsRestrictedCheckGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type)
			throws NotFoundException {

		RestrictedMap vo = new RestrictedMap();

		try {
			// 1. 기존 리스트 지운다.
			restrictedList.clear();

			// 2. select array
			List<AppZoneEventVo> appZoneEventVos = dangerApiService.selectRestrictedList();
			for (AppZoneEventVo inputRestictedVo : appZoneEventVos) {
				addRestrictedVo(inputRestictedVo);
			}

			logger.debug("restrictedList.size : " + restrictedList.size());

			vo.setMsg("SUCCESS");
			vo.setRestrictedList(restrictedList);
			return new ResponseEntity<RestrictedMap>(vo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<RestrictedMap>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	private void addRestrictedVo(AppZoneEventVo appZoneEventVo) {
		logger.debug("restrictedList Add appZoneEventVo:" + appZoneEventVo.toString());
		restrictedList.add(appZoneEventVo);
	}

	@ApiOperation(value = "", notes = "push 보내기.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "비인가자 출입정보"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/restricted_push.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwmsRestrictedPushPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "work info", required = true) @RequestBody PushDeviceNoSendVo pushDeviceNoSendVo

	) throws NotFoundException {
		ResponseData vo = new ResponseData();
		try {

			logger.info("v1WwmsRestrictedPushPost pushDeviceNoSendVo : " + pushDeviceNoSendVo);
			vo.setMsg("SUCCESS");

			pushService.pushSend("2", "emergency", pushDeviceNoSendVo.getPhoneList(), pushDeviceNoSendVo.getMsg(),
					pushDeviceNoSendVo.getUser_id());

			return new ResponseEntity<ResponseData>(vo, HttpStatus.OK);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value = "", notes = "단말기정보를 전달한다.", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "단말기정보 제공."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/device_information.do", method = RequestMethod.POST)
	public ResponseEntity<DangerDevice> v1WwmsDeviceInfoGet(
			@ApiParam(value = "device_no", required = false) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device info", required = true) @RequestBody AppDeviceInfoVo appDeviceInfoVo)
			throws NotFoundException {
		DangerDevice vo = new DangerDevice();
		String msg = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;
		try {

			appDeviceInfoVo.setDevice_no(device_no);
			String apiKey = propertyService.getString("smartplant.danger.apikey");
			String packageName = propertyService.getString("smartplant.danger.package");

			if (StringUtil.isEmpty(appDeviceInfoVo.getPackage_name())
					|| !packageName.equals(appDeviceInfoVo.getPackage_name())) {
				statusCode = HttpStatus.FORBIDDEN;
				vo.setMsg(Return_Message.FAIL.getMessage() + "|패기지명이 없거나 값이 일치하지 않습니다.");
				return new ResponseEntity<DangerDevice>(vo, statusCode);
			}

			if (StringUtil.isEmpty(appDeviceInfoVo.getApp_key()) || !apiKey.equals(appDeviceInfoVo.getApp_key())) {
				statusCode = HttpStatus.FORBIDDEN;
				vo.setMsg(Return_Message.FAIL.getMessage() + "|app_key가 없거나 값이 일치하지 않습니다.");
				return new ResponseEntity<DangerDevice>(vo, statusCode);
			}

			DeviceDangerVo deviceDangerVo = dangerApiService.selectDeviceInfo(device_no);
			if (deviceDangerVo == null) {
				statusCode = HttpStatus.UNAUTHORIZED;
				msg = "시스템에 등록되지 않은 단말기입니다.";
				// 이미 작업중인 단말기인지 확인이 필요함
			} else {

				if (!StringUtils.isEmpty(appDeviceInfoVo.getDevice_network_type())) {
					logger.debug("Device Information Network Type Update [{}]",
							appDeviceInfoVo.getDevice_network_type());
					dangerApiService.updateDeviceNetworkType(appDeviceInfoVo);
					logger.debug("Device Information Network Type Update End");
				}

				String access_token = authService.generationDeviceToken("1", device_no);
				deviceDangerVo.setAccess_token(access_token);
				vo.setDeviceDangerVo(deviceDangerVo);

				WorkReg workReg = new WorkReg();

				logger.info("deviceDangerVo.getLast_work_uid():" + deviceDangerVo.getLast_work_uid());
				if (deviceDangerVo.getLast_work_uid() != null) {

					AppWorkRegEventVo workVo = dangerApiService.selectWorkInfo(deviceDangerVo.getLast_work_uid());
					logger.info("AppWorkRegEventVo :" + workVo);
					if (workVo != null) {
						workReg.setWork_uid(workVo.getWork_uid());
						workReg.setZone_type(workVo.getZone_type());
						workReg.setZone_id(workVo.getZone_id());
					}
				}
				vo.setWorkReg(workReg);
			}

			vo.setMsg(msg);
			return new ResponseEntity<DangerDevice>(vo, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<DangerDevice>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<DangerDevice>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

package com.nemustech.platform.lbs.ngms.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
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

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.model.DeviceEcgiList;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.common.util.Const;
import com.nemustech.platform.lbs.common.vo.DeviceBeaconVo;
import com.nemustech.platform.lbs.common.vo.DeviceEcgiVo;
import com.nemustech.platform.lbs.ngms.model.AppPersonalDeviceRegReturn;
import com.nemustech.platform.lbs.ngms.model.CodeDestinationList;
import com.nemustech.platform.lbs.ngms.model.MetaZoneList;
import com.nemustech.platform.lbs.ngms.model.NaviMap;
import com.nemustech.platform.lbs.ngms.model.VehicleMapResourceList;
import com.nemustech.platform.lbs.ngms.model.VehicleTypeList;
import com.nemustech.platform.lbs.ngms.service.NaviApiService;
import com.nemustech.platform.lbs.ngms.service.VehicleAppApiService;
import com.nemustech.platform.lbs.ngms.vo.AppPersonDeviceVehicleUnRegVo;
import com.nemustech.platform.lbs.ngms.vo.AppResourceFileInfoVo;
import com.nemustech.platform.lbs.ngms.vo.AppVehicleRegVo;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleDestinationTypeVo;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleDestinationVo;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleTypeVo;
import com.nemustech.platform.lbs.ngms.vo.MetaZoneVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceVo;
import com.nemustech.platform.lbs.wwms.model.CodeOrganiztionList;
import com.nemustech.platform.lbs.wwms.model.GpsZone;
import com.nemustech.platform.lbs.wwms.service.CommonCodeApiService;
import com.nemustech.platform.lbs.wwms.service.DangerApiService;
import com.nemustech.platform.lbs.wwms.vo.AppPwdEventVo;
import com.nemustech.platform.lbs.wwms.vo.GpsZoneVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/*
 * 160725 [개인폰] [신규] 단말 연동 API
 */
@Controller
@RequestMapping(value = "/v1/ngms", produces = "application/json;charset=utf8;")
@Api(value = "/v1/ngms")
public class VehicleAppApiController extends DefaultController {
	private static final Logger logger = LoggerFactory.getLogger(VehicleAppApiController.class);

	@Autowired
	NaviApiService naviApiService;

	@Autowired
	private VehicleAppApiService vehicleAppApiService;

	@Autowired
	private DangerApiService dangerApiService;

	@Autowired
	private CommonCodeApiService commonCodeApiService;

	// 160804 [개인폰] [신규] [안전운행9] MAP GPS Zone 정보 조회
	@ApiOperation(value = "", notes = "위험지역 GPS Zone 정보를 가져온다.", response = NaviMap.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/gps_zone.do", method = RequestMethod.GET)
	public ResponseEntity<GpsZone> v1NgmsGpsZoneGet(
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

	// 160804 [개인폰] [신규] [안전운행10] 도로 지도 리소스 정보 조회
	@ApiOperation(value = "", notes = "도로 지도 리소스 정보 조회 리스트를 가져온다.", response = CodeOrganiztionList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/map_resources.do", method = RequestMethod.GET)
	public ResponseEntity<VehicleMapResourceList> v1NgmsMapResourcesGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no)
			throws NotFoundException {
		List<AppResourceFileInfoVo> dataList = null;
		String resource_path = "resources/img/";
		try {
			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (vehicleAppApiService.getIsLastModifiedResource(if_modified_since) == false) {
					VehicleMapResourceList response = new VehicleMapResourceList();
					response.setMsg("Not Modified");
					return new ResponseEntity<VehicleMapResourceList>(response, HttpStatus.NOT_MODIFIED);
				}
			}

			dataList = vehicleAppApiService.getAppResourceList();
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<VehicleMapResourceList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<VehicleMapResourceList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<VehicleMapResourceList>(HttpStatus.NO_CONTENT);
		} else {
			VehicleMapResourceList response = new VehicleMapResourceList();
			response.setMsg(Return_Message.SUCCESS.getMessage());
			response.setAppResourceFileInfoVo(dataList);
			response.setResource_path(resource_path);
			return new ResponseEntity<VehicleMapResourceList>(response, HttpStatus.OK);
		}
	}

	// 160804 [개인폰] [신규] [안전운행11] 개인 단말 차량 등록
	@ApiOperation(value = "", notes = "개인 단말 차량 등록", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/vehicle_register.do", method = RequestMethod.POST)
	public ResponseEntity<AppPersonalDeviceRegReturn> v1WwmsVehicleRegisterEventPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "device info", required = true) @RequestBody AppVehicleRegVo appVehicleRegVo)
			throws NotFoundException {

		AppPersonalDeviceRegReturn response = null;
		int result = 0;
		String returnMessage = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;

		String device_uid = null;
		NaviDeviceVo naviDeviceVo = null;

		try {

			// init
			appVehicleRegVo.setDevice_no(device_no);
			logger.info("appVehicleRegVo : " + appVehicleRegVo);

			// then

			result = vehicleAppApiService.createPersonDeviceRegister(appVehicleRegVo);
			if (result < 0) {
				logger.info("createPersonDeviceRegister fail result [{}]", result);
				statusCode = HttpStatus.I_AM_A_TEAPOT;
				returnMessage = Const.returnMessage(result);
			} else {
				naviDeviceVo = naviApiService.selectDeviceInfo(device_no);
				device_uid = naviDeviceVo.getDevice_uid();
				logger.info("createPersonDeviceRegister return device_uid [{}]", device_uid);
			}

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<AppPersonalDeviceRegReturn>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<AppPersonalDeviceRegReturn>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// return
		response = new AppPersonalDeviceRegReturn();
		response.setMsg(returnMessage);
		response.setDevice_uid(device_uid);
		response.setNaviDeviceVo(naviDeviceVo);
		logger.info("AppPersonalDeviceRegReturn return [{}]", response.toString());

		return new ResponseEntity<AppPersonalDeviceRegReturn>(response, statusCode);

	}

	// 160804 [개인폰] [신규] [안전운행12] 차량 유형 정보 조회
	@ApiOperation(value = "", notes = "차량 유형 정보 조회 리스트를 가져온다.", response = CodeOrganiztionList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/vehicle_type.do", method = RequestMethod.GET)
	public ResponseEntity<VehicleTypeList> v1NgmsVehicleTypeGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no)
			throws NotFoundException {
		List<CodeVehicleTypeVo> dataList = null;

		try {

			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (commonCodeApiService.getIsLastModifiedVehicleType(if_modified_since) == false) {
					VehicleTypeList response = new VehicleTypeList();
					response.setMsg("Not Modified");
					return new ResponseEntity<VehicleTypeList>(response, HttpStatus.NOT_MODIFIED);
				}
			}

			dataList = commonCodeApiService.selectVehicleTypeList();

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<VehicleTypeList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<VehicleTypeList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<VehicleTypeList>(HttpStatus.NO_CONTENT);
		} else {
			VehicleTypeList response = new VehicleTypeList();
			response.setMsg(Return_Message.SUCCESS.getMessage());
			response.setVehicleTypeList(dataList);
			response.setTotalCnt(dataList.size());
			return new ResponseEntity<VehicleTypeList>(response, HttpStatus.OK);
		}
	}

	// 160804 [개인폰] [신규] [안전운행13] FCM_token 갱신
	@ApiOperation(value = "", notes = "FCM_token 갱신", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
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
			result = vehicleAppApiService.updateVehicleDevicePushId(fcmTokenEventVo);
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

	// 160804 [개인폰] [신규] [안전운행14] 개인 단말 차량 해제
	@ApiOperation(value = "", notes = "개인 단말 차량 해제", response = ResponseData.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/vehicle_unregister.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwmsVehicleUnregisterEventPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "passwd info", required = true) @RequestBody AppPersonDeviceVehicleUnRegVo appPersonDeviceVehicleUnRegVo)
			throws NotFoundException {
		ResponseData vo = new ResponseData();
		int result = 0;
		String msg = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;
		try {

			// 단말 번호 확인
			if (!StringUtils.isEmpty(device_no)) {
				appPersonDeviceVehicleUnRegVo.setDevice_no(device_no);
			}

			// 차량 번호 확인
			if (StringUtils.isEmpty(appPersonDeviceVehicleUnRegVo.getCar())) {
				vo.setMsg("할당된 차량정보가 없습니다.");
				return new ResponseEntity<ResponseData>(vo, HttpStatus.I_AM_A_TEAPOT);
			}

			logger.info("appPersonDeviceVehicleUnRegVo : " + appPersonDeviceVehicleUnRegVo.toString());

			// 개인 단말 해제
			result = naviApiService.appUnRegVehicle(appPersonDeviceVehicleUnRegVo, request_type);
			if (result < 0)
				statusCode = HttpStatus.I_AM_A_TEAPOT;

			if (result == -901)
				msg = "할당된 차량정보가 없습니다.";
			else if (result == -902)
				msg = "할당된 챠량정보가 일치하지 않습니다.";

			vo.setMsg(msg);
			return new ResponseEntity<ResponseData>(vo, statusCode);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 160804 [개인폰] [신규] [안전운행15] 목적지 정보 조회
	@ApiOperation(value = "", notes = "목적지 정보 조회 리스트를 가져온다.", response = CodeOrganiztionList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/destination.do", method = RequestMethod.GET)
	public ResponseEntity<CodeDestinationList> v1NgmsDestinationGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since,
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no)
			throws NotFoundException {
		List<CodeVehicleDestinationVo> dataList = null;
		List<CodeVehicleDestinationTypeVo> dataTypeList = null;
		try {

			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (commonCodeApiService.getIsLastModifiedDestination(if_modified_since) == false) {
					CodeDestinationList response = new CodeDestinationList();
					response.setMsg("Not Modified");
					return new ResponseEntity<CodeDestinationList>(response, HttpStatus.NOT_MODIFIED);
				}
			}

			dataTypeList = commonCodeApiService.selectDestinationTypeList();
			dataList = commonCodeApiService.selectDestinationList();
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<CodeDestinationList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CodeDestinationList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<CodeDestinationList>(HttpStatus.NO_CONTENT);
		} else {
			CodeDestinationList vo = new CodeDestinationList();
			vo.setMsg(Return_Message.SUCCESS.getMessage());
			vo.setDestinationTypeList(dataTypeList);
			vo.setDestinationList(dataList);
			vo.setTotalCnt(dataList.size());
			return new ResponseEntity<CodeDestinationList>(vo, HttpStatus.OK);
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

	// 160804 [개인폰] [신규] [안전운행12] 단말 ECGI 정보 조회
	@ApiOperation(value = "", notes = "단말 ECGI 정보 조회 리스트를 가져온다.", response = CodeOrganiztionList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/ecgi_and_beacon.do", method = RequestMethod.GET)
	public ResponseEntity<DeviceEcgiList> v1NgmsDeviceEcgiGet(
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

}

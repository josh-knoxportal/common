package com.nemustech.platform.lbs.wwms.api;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.common.util.SessionUtil;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;
import com.nemustech.platform.lbs.common.vo.SearchVo;
import com.nemustech.platform.lbs.wwms.model.DeviceDangerCud;
import com.nemustech.platform.lbs.wwms.model.DeviceDangerList;
import com.nemustech.platform.lbs.wwms.service.DeviceDangerApiService;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerCudVo;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerPasswordVo;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_V1_ROOT, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class DeviceDangerApiController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceDangerApiController.class);

	@Autowired
	private DeviceDangerApiService deviceDangerApiService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@ApiOperation(value = "", notes = "위험지역 단말관리 목록 정보를 가져온다.", response = DeviceDangerList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/device.do", method = RequestMethod.GET)
	public ResponseEntity<DeviceDangerList> v1WwmsDeviceDangerListGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "current page index (default_value is 0)", allowableValues = "{}") @RequestParam(value = "page_index", required = false, defaultValue = "0") int page_index,
			@ApiParam(value = "count per page (-1일 경우 모든 계정 정보 가져오기) (default_value is 10)", allowableValues = "{}") @RequestParam(value = "record_count_per_page", required = false, defaultValue = "-1") int record_count_per_page,
			@ApiParam(value = "search_filter") @RequestParam(value = "search_filter", required = false, defaultValue = "") String search_filter,
			@ApiParam(value = "search_type") @RequestParam(value = "search_type", required = false, defaultValue = "") String search_type,
			@ApiParam(value = "order_type") @RequestParam(value = "order_type", required = false, defaultValue = "") String order_type,
			@ApiParam(value = "order_desc_asc") @RequestParam(value = "order_desc_asc", required = false, defaultValue = "") String order_desc_asc,
			HttpServletRequest request) throws NotFoundException {

		logger.info("enter v1WwmsDeviceDangerListGet");

		DeviceDangerList response = null;

		try {
			SearchVo searchVo = new SearchVo();
			searchVo.setLimit_count(record_count_per_page);
			searchVo.setLimit_offset(page_index);
			searchVo.setSearch_filter(search_filter);
			searchVo.setSort_column(order_type);
			searchVo.setStr_order_by(order_desc_asc);
			response = deviceDangerApiService.selectDeviceDangerList(searchVo);
		}catch(DataAccessResourceFailureException e){
			return new ResponseEntity<DeviceDangerList>(HttpStatus.SERVICE_UNAVAILABLE);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<DeviceDangerList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (response == null) {
			logger.info("[v1WwmsDeviceDangerListGet] deviceDangerList is null");
			return new ResponseEntity<DeviceDangerList>(HttpStatus.NO_CONTENT);
		} else {
			response.setMsg(Return_Message.SUCCESS.getMessage());
			return new ResponseEntity<DeviceDangerList>(response, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "위험지역 단말관리 단말 정보를 가져온다.", response = DeviceDangerCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "단말기 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/device/{device_uid}.do", method = RequestMethod.GET)
	public ResponseEntity<DeviceDangerCud> v1WwmsDeviceDangerGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "UID of Device", required = true, allowableValues = "{}") @PathVariable(value = "device_uid") String device_uid)
			throws NotFoundException {

		DeviceDangerCud response = null;

		try {
			logger.info("device_uid:" + device_uid);

			DeviceDangerVo deviceDangerVo = new DeviceDangerVo();
			deviceDangerVo.setDevice_uid(device_uid);
			response = deviceDangerApiService.selectDeviceDanger(deviceDangerVo);
		}catch(DataAccessResourceFailureException e){
			return new ResponseEntity<DeviceDangerCud>(HttpStatus.SERVICE_UNAVAILABLE);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<DeviceDangerCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (response == null) {
			return new ResponseEntity<DeviceDangerCud>(HttpStatus.NO_CONTENT);
		} else {
			response.setMsg(Return_Message.SUCCESS.getMessage());
			return new ResponseEntity<DeviceDangerCud>(response, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "위험지역 단말관리 단말정보를 설정한다.", response = DeviceDangerCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/device.do", method = RequestMethod.POST)
	public ResponseEntity<DeviceDangerCud> v1WwmsDeviceDangerCudPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "operation code(insert,update,delete", required = true) @RequestHeader(value = "opcode", required = true) String opcode,
			@ApiParam(value = "단말 설정정보", required = true) @RequestBody DeviceDangerCudVo deviceDangerCudVo,
			HttpServletRequest request) throws NotFoundException, Exception {
		logger.info("opcode [{}]", opcode);

		String account_id = null;

		ResponseLoginVo account = SessionUtil.getAccountInfo();
		if (account != null) {
			account_id = account.getUser_id();
		} else {
			logger.info("session user id null [{}]", opcode);
		}

		if ("create".equals(opcode)) {
			// check exist
			if (isExistDeviceNo(deviceDangerCudVo.getDevice_no())) {
				DeviceDangerCud response = new DeviceDangerCud();

				DeviceDangerVo deviceDangerVo = new DeviceDangerVo();
				deviceDangerVo.setDevice_no(deviceDangerCudVo.getDevice_no());
				response = deviceDangerApiService.selectDeviceDanger(deviceDangerVo);

				if (response.getDeviceDangerCudVo().getIs_used() == 0) {
					deviceDangerCudVo.setDevice_uid(response.getDeviceDangerCudVo().getDevice_uid());

					deviceDangerCudVo.setCreator_id(account_id);
					deviceDangerCudVo.setStatus(1);
					deviceDangerApiService.deviceDangerCud(deviceDangerCudVo, "update");

					response.setMsg(Return_Message.SUCCESS.getMessage());
					return new ResponseEntity<DeviceDangerCud>(response, HttpStatus.OK);
				} else {
					response.setMsg("EXISTDEVICENO");
					return new ResponseEntity<DeviceDangerCud>(response, HttpStatus.OK);
				}
			}
		}

		try {
			deviceDangerCudVo.setCreator_id(account_id);
			deviceDangerApiService.deviceDangerCud(deviceDangerCudVo, opcode);
		} catch(DataAccessResourceFailureException e){
			return new ResponseEntity<DeviceDangerCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<DeviceDangerCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		DeviceDangerCud response = new DeviceDangerCud();
		response.setMsg(Return_Message.SUCCESS.getMessage());
		return new ResponseEntity<DeviceDangerCud>(response, HttpStatus.OK);
	}

	private boolean isExistDeviceNo(String device_no) throws Exception {
		if (deviceDangerApiService.isExistDeviceNo(device_no)) {
			return true;
		}
		return false;
	}

	@ApiOperation(value = "", notes = "위험지역 단말관리 비밀번호정보를 설정한다.", response = DeviceDangerCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/devicePassword.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> v1WwmsDeviceDangerPasswordPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "operation code(update)", required = true) @RequestHeader(value = "opcode", required = true) String opcode,
			@ApiParam(value = "단말 비밀번호 설정정보", required = true) @RequestBody DeviceDangerPasswordVo deviceDangerPasswordVo,
			HttpServletRequest request) throws NotFoundException {

		ResponseData response = null;
		String password = null;

		try {
			String editor_id = SessionUtil.getAccountInfo().getUser_id();

			logger.info("opcode [{}] session id [{}]", opcode, editor_id);

			if (editor_id == null) {
				// return logout
			}

			// check password = input_currentPassword ? pass : return
			/*
			 * if encode
			 */
			password = deviceDangerApiService.selectDevicePasswordDanger();
			if (!checkPassword(deviceDangerPasswordVo.getCurrent_password(), password)) {
				response = new ResponseData();
				response.setMsg("NOTMATCH");
				return new ResponseEntity<ResponseData>(response, HttpStatus.OK);
			}

			// update password
			deviceDangerPasswordVo.setEditor_id(editor_id);
			/*
			 * if encode
			 */
			deviceDangerPasswordVo.setNew_password(passwordEncoder.encode(deviceDangerPasswordVo.getNew_password()));
			deviceDangerApiService.modifyDevicePasswordDanger(deviceDangerPasswordVo);

		}catch(DataAccessResourceFailureException e){
			return new ResponseEntity<ResponseData>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ResponseData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response = new ResponseData();
		response.setMsg(Return_Message.SUCCESS.getMessage());
		return new ResponseEntity<ResponseData>(response, HttpStatus.OK);
	}

	private boolean checkPassword(String input_password, String password) {
		if (password == null || password == "" || password.length() < 60) {
			return false;
		}

		logger.info("passwordEncoder.matches [{}] ", passwordEncoder.matches(input_password, password));
		if (passwordEncoder.matches(input_password, password)) {
			logger.info("passwordEncoder matche");
			return true;
		}

		return false;
	}
}

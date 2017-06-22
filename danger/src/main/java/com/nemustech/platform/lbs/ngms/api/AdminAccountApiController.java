package com.nemustech.platform.lbs.ngms.api;

import java.util.List;

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
import org.springframework.web.util.WebUtils;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.util.SessionUtil;
import com.nemustech.platform.lbs.common.util.StringUtil;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;
import com.nemustech.platform.lbs.common.vo.SystemType;
import com.nemustech.platform.lbs.web.vehicle.service.AdminAccountApiService;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccount;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountCud;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountCudVo;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountList;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountSearchVo;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = "/v1/ngms", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class AdminAccountApiController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(AdminAccountApiController.class);

	@Autowired
	private AdminAccountApiService adminAccountApiService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@ApiOperation(value = "", notes = "계정 정보를 가져온다.", response = AdminAccountList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "계정 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/adminAccount.do", method = RequestMethod.GET)
	public ResponseEntity<AdminAccountList> v1NgmsAdminAccountListGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "current page index (default_value is 0)", allowableValues = "{}") @RequestParam(value = "page_index", required = false, defaultValue = "0") int page_index,
			@ApiParam(value = "count per page (-1일 경우 모든 계정 정보 가져오기) (default_value is 10)", allowableValues = "{}") @RequestParam(value = "record_count_per_page", required = false, defaultValue = "-1") int record_count_per_page,
			@ApiParam(value = "search_filter") @RequestParam(value = "search_filter", required = false, defaultValue = "") String search_filter,
			@ApiParam(value = "search_type") @RequestParam(value = "search_type", required = false, defaultValue = "") String search_type,
			HttpServletRequest request) throws NotFoundException {

		List<AdminAccountVo> dataList = null;
		int totalCnt = 0;

		int system_type = getSessionSystemType(request);

		try {
			AdminAccountSearchVo searchVo = new AdminAccountSearchVo();
			searchVo.setLimit_count(record_count_per_page);
			searchVo.setLimit_offset(page_index);
			searchVo.setSearch_filter(search_filter);
			searchVo.setSearch_type(search_type);
			searchVo.setSystem_type(system_type);

			dataList = adminAccountApiService.selectAdminAccountList(searchVo);
			totalCnt = adminAccountApiService.selectAdminAccountListTotalCnt(searchVo);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<AdminAccountList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<AdminAccountList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<AdminAccountList>(HttpStatus.NO_CONTENT);
		} else {
			AdminAccountList vo = new AdminAccountList();
			vo.setMsg(Return_Message.SUCCESS.getMessage());
			vo.setAdminAccountList(dataList);
			vo.setTotalCnt(totalCnt);
			return new ResponseEntity<AdminAccountList>(vo, HttpStatus.OK);
		}

	}

	@ApiOperation(value = "", notes = "계정 정보를 가져온다.", response = AdminAccount.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "계정 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/adminAccount/{account_uid}.do", method = RequestMethod.GET)
	public ResponseEntity<AdminAccount> v1NgmsAdminAccountGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "UID of account", required = true, allowableValues = "{}") @PathVariable(value = "account_uid") String account_uid,
			HttpServletRequest request) throws NotFoundException {

		AdminAccountVo adminAccount = null;

		int system_type = getSessionSystemType(request);

		try {
			logger.info("account_uid:" + account_uid);
			adminAccount = new AdminAccountVo();
			adminAccount.setAccount_uid(account_uid);
			adminAccount.setSystem_type(system_type);
			adminAccount = adminAccountApiService.selectAdminAccount(adminAccount);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<AdminAccount>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			return new ResponseEntity<AdminAccount>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (adminAccount == null) {
			return new ResponseEntity<AdminAccount>(HttpStatus.NO_CONTENT);
		} else {
			AdminAccount vo = new AdminAccount();
			vo.setMsg(Return_Message.SUCCESS.getMessage());
			vo.setAdminAccount(adminAccount);
			return new ResponseEntity<AdminAccount>(vo, HttpStatus.OK);
		}

	}

	@ApiOperation(value = "", notes = "계정 정보를 등록한다.", response = AdminAccountCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "계정 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/adminAccount.do", method = RequestMethod.POST)
	public ResponseEntity<AdminAccountCud> v1NgmsAdminAccountPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "operation code(insert,update,delete", required = true) @RequestHeader(value = "opcode", required = true) String opcode,
			@ApiParam(value = "device info", required = true) @RequestBody AdminAccountCudVo adminAccountCudVo,
			HttpServletRequest request) throws NotFoundException {

		AdminAccountVo org_account = null;
		String input_current_password = null;

		int system_type = getSessionSystemType(request);
		adminAccountCudVo.setSystem_type(system_type);

		int isAdmin = 0;

		try {
			logger.info("adminAccountVo:" + adminAccountCudVo.getCurrent_password());
			logger.info("adminAccountVo:" + adminAccountCudVo.getNew_password());
			logger.info("adminAccountVo:" + adminAccountCudVo.getPassword());

			ResponseLoginVo account = SessionUtil.getAccountInfo();
			if (account != null) {
				isAdmin = account.getIs_admin();
			}

			if ("create".equals(opcode)) {
				// check user_id
				if (isExistUserIdBySystemType(adminAccountCudVo)) {
					return returnMessage("EXISTUSERID");
				}

				// check email
				adminAccountCudVo.setOpcode_cu("create");
				if (isExistEmail(adminAccountCudVo)) {
					return returnMessage("EXISTEMAIL");
				}

				/*
				 * if encode
				 */
				String encode_newPassword = passwordEncoder.encode(adminAccountCudVo.getPassword());
				adminAccountCudVo.setPassword(encode_newPassword);
				adminAccountApiService.createAdminAccount(adminAccountCudVo);

			} else if ("update".equals(opcode)) {

				// check email
				adminAccountCudVo.setOpcode_cu("update");
				if (isExistEmail(adminAccountCudVo)) {
					return returnMessage("EXISTEMAIL");
				}

				input_current_password = adminAccountCudVo.getCurrent_password();
				logger.info("adminAccountCudVo.getCurrent_password() [{}]", input_current_password);

				if (null == input_current_password || "".equals(input_current_password)
						|| input_current_password == "") {
					logger.info("current password not exist~~~");
					return returnMessage("NOTPASSWORD");
				}

				org_account = adminAccountApiService.selectAdminAccount(adminAccountCudVo);
				logger.info("current.getPassword() [{}]", org_account.getPassword());

				/*
				 * if encode
				 */
				if (!checkPassword(input_current_password, org_account.getPassword())) {
					logger.info("current password not equal~~~");
					return returnMessage("NOTPASSWORD");
				}

				String input_new_password = adminAccountCudVo.getNew_password();
				logger.info("getNew_password() [{}]", input_new_password);

				/*
				 * if encode
				 */
				if (!(null == input_new_password || "".equals(input_new_password) || input_new_password == "")) {
					logger.info("update new password");
					adminAccountCudVo.setNew_password(passwordEncoder.encode(input_new_password));
				}

				adminAccountApiService.modifyAdminAccount(adminAccountCudVo);

			} else if ("updateByAdmin".equals(opcode)) {
				// check email
				adminAccountCudVo.setOpcode_cu("update");
				if (isExistEmail(adminAccountCudVo)) {
					return returnMessage("EXISTEMAIL");
				}

				/*
				 * if system_type danger & admin
				 */
				if (SystemType.DANGER.getValue() == system_type && isAdmin == 1) {
					adminAccountApiService.modifyAdminAccount(adminAccountCudVo);
					return returnMessage(Return_Message.SUCCESS.getMessage());
				}
			} else if ("delete".equals(opcode)) {
				adminAccountApiService.removeAdminAccount(adminAccountCudVo);
			} else if ("deleteByUid".equals(opcode)) {
				adminAccountApiService.removeAdminAccountByUid(adminAccountCudVo);
			} else if ("vehicledelete".equals(opcode)) {
				if (SystemType.VEHICLE.getValue() == system_type && isAdmin == 1) {
					adminAccountApiService.removeAdminAccount(adminAccountCudVo);
				} else {
					return returnMessage("NOAUTH");
				}
			}

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<AdminAccountCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<AdminAccountCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// return success
		return returnMessage(Return_Message.SUCCESS.getMessage());
	}

	@ApiOperation(value = "", notes = "계정아이디 중복확인을 한다.", response = AdminAccountCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "아이디 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/checkUserId.do", method = RequestMethod.POST)
	public ResponseEntity<AdminAccountCud> v1NgmsAdminAccountCheckUserIdPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "device info", required = true) @RequestBody AdminAccountCudVo adminAccountCudVo,
			HttpServletRequest request) throws NotFoundException {

		int system_type = getSessionSystemType(request);
		adminAccountCudVo.setSystem_type(system_type);

		try {

			if (StringUtil.isEmpty(adminAccountCudVo.getUser_id())) {
				return returnMessage("EMPTYUSERID");
			}

			logger.info("user_id [{}]", adminAccountCudVo.getUser_id());
			if (isExistUserIdBySystemType(adminAccountCudVo)) {
				return returnMessage("EXISTUSERID");
			}

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<AdminAccountCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<AdminAccountCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return returnMessage(Return_Message.SUCCESS.getMessage());
	}

	protected ResponseEntity<AdminAccountCud> returnMessage(String message) {
		logger.info("AdminAccount Api return Message [{}]", message);
		AdminAccountCud vo = new AdminAccountCud();
		vo.setMsg(message);
		return new ResponseEntity<AdminAccountCud>(vo, HttpStatus.OK);
	}

	private boolean isExistUserIdBySystemType(AdminAccountCudVo adminAccountCudVo) throws Exception {
		if (adminAccountApiService.isExistUserIdBySystemType(adminAccountCudVo)) {
			return true;
		}
		return false;
	}

	private boolean isExistEmail(AdminAccountCudVo adminAccountCudVo) throws Exception {
		if (adminAccountApiService.isExistEmail(adminAccountCudVo)) {
			return true;
		}
		return false;
	}

	private int getSessionSystemType(HttpServletRequest request) {
		int system_type = 0;
		ResponseLoginVo account = (ResponseLoginVo) WebUtils.getSessionAttribute(request,
				DefaultController.SESSION_ATTR_KEY_ACCOUNT);

		if (account != null) {
			system_type = Integer.parseInt(account.getSystem_type());
		}

		logger.info("system_type [{}]", system_type);
		return system_type;
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

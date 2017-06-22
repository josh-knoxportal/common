package com.nemustech.platform.lbs.aams.service;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import com.nemustech.platform.lbs.aams.mapper.LoginApiMapper;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.controller.DefaultController.Return_Message;
import com.nemustech.platform.lbs.common.model.ResponseLogin;
import com.nemustech.platform.lbs.common.service.MailService;
import com.nemustech.platform.lbs.common.vo.LoginHistoryVo;
import com.nemustech.platform.lbs.common.vo.LoginVo;
import com.nemustech.platform.lbs.common.vo.ResetLoginVo;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;
import com.nemustech.platform.lbs.common.vo.SystemType;

@Service
public class LoginApiService {
	private static final Logger logger = LoggerFactory.getLogger(LoginApiService.class);

	@Autowired
	protected ServletContext context;

	@Autowired
	private LoginApiMapper loginApimapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthService authService;

	@Autowired
	private MailService mailService;

	public ResponseLoginVo selectLoginInfo(LoginVo loginVo) throws Exception {
		return loginApimapper.selectLoginInfo(loginVo);
	}

	public ResponseLoginVo selectLoginInfo(String user_id, String system_type) throws Exception {
		LoginVo loginVo = new LoginVo();
		loginVo.setUser_id(user_id);
		loginVo.setSystem_type(system_type);
		return selectLoginInfo(loginVo);
	}

	@Transactional
	public int updateLoginDate(ResponseLoginVo responseLoginVo) throws Exception {
		return loginApimapper.updateLoginDate(responseLoginVo);
	}

	@Transactional
	private int updateResetPassword(ResetLoginVo loginVo) throws Exception {
		return loginApimapper.updateResetPassword(loginVo);
	}

	@Transactional
	public int updatePasswordFailCount(LoginVo loginVo) throws Exception {
		return loginApimapper.updatePasswordFailCount(loginVo);
	}

	@Transactional
	private int updatePasswordFailCountInit(LoginVo loginVo) throws Exception {
		return loginApimapper.updatePasswordFailCountInit(loginVo);
	}

	@Transactional
	public int insertVehicleLoginHistory(String user_id, int history_type) throws Exception {
		LoginHistoryVo loginHistoryVo = new LoginHistoryVo();
		loginHistoryVo.setUser_id(user_id);
		loginHistoryVo.setHistory_type(history_type);
		return loginApimapper.insertVehicleLoginHistory(loginHistoryVo);
	}

	@Transactional
	public int insertDangerLoginHistory(String user_id, int history_type) throws Exception {
		LoginHistoryVo loginHistoryVo = new LoginHistoryVo();
		loginHistoryVo.setUser_id(user_id);
		loginHistoryVo.setHistory_type(history_type);
		return loginApimapper.insertDangerLoginHistory(loginHistoryVo);
	}

	public ResponseEntity<ResponseLogin> chekLoginProcess(LoginVo loginVo, SystemType system_type,
			HttpServletRequest request) {

		ResponseLoginVo responseLoginVo = null;

		String user_id = "";
		String input_password = "";

		boolean isFirstLogin = false;
		String access_token = "";

		// get login user info
		try {
			user_id = loginVo.getUser_id();
			input_password = loginVo.getPassword();
			loginVo.setSystem_type(system_type.toString());

			// logger.info("user_id [{}] password [{}]", user_id,
			// input_password);

			responseLoginVo = selectLoginInfo(user_id, system_type.toString());

		} catch (Exception e) {
			return returnInternalServerError(e);
		}

		// isEmpty ?
		if (responseLoginVo == null) {
			logger.info("responseLoginVo == nul");
			return returnLoginApiMessage("NOMATCH");
		}

		// check password
		logger.info("responseLoginVo.getPassword() [{}] ", responseLoginVo.getPassword());
		/*
		 * if encode
		 */
		if (!checkPassword(input_password, responseLoginVo.getPassword())) {

			logger.info("password not matche");

			try {
				// update login fail count + 1
				updatePasswordFailCount(loginVo);
			} catch (Exception e) {
				return returnInternalServerError(e);
			}

			// fail count > 4
			if (responseLoginVo.getFail_count() >= 4) {
				return returnLoginApiMessage("POPUPPASSOWORD");
			}

			return returnLoginApiMessage("NOMATCH");
		}

		// do token
		try

		{
			access_token = authService.generateAccountToken(loginVo);
			responseLoginVo.setAccess_token(access_token);
		} catch (Exception e1) {
			return returnInternalServerError(e1);
		}

		// account update upd_date
		logger.info("account update upd_date [{}] ", responseLoginVo.getAccount_uid());
		try {
			updateLoginDate(responseLoginVo);
		} catch (Exception e2) {
			return returnInternalServerError(e2);
		}

		setLoginSessionAttribute(request, responseLoginVo);

		// check first login
		if ("".equals(responseLoginVo.getLast_login_date()) || responseLoginVo.getLast_login_date() == null) {
			isFirstLogin = true;
		}

		if (responseLoginVo.getFail_count() != 0) {
			try {
				updatePasswordFailCountInit(loginVo);
			} catch (Exception e) {
				return returnInternalServerError(e);
			}
		}

		// login history

		// redirect page & history
		String redirect_url = "/logout.do";

		if (SystemType.DANGER.equals(system_type)) {
			redirect_url = getReturnDangerUrl(responseLoginVo, isFirstLogin);

			logger.info("login History");
			try {
				insertDangerLoginHistory(user_id, 1);
			} catch (Exception e2) {
				return returnInternalServerError(e2);
			}
		}

		if (SystemType.VEHICLE.equals(system_type)) {
			redirect_url = getReturnVehicleUrl(responseLoginVo, isFirstLogin);

			logger.info("login History");
			try {
				insertVehicleLoginHistory(user_id, 1);
			} catch (Exception e2) {
				return returnInternalServerError(e2);
			}
		}

		logger.info("redirect_url [{}] ", redirect_url);

		return returnSuccess(responseLoginVo, redirect_url);
	}

	protected void setLoginSessionAttribute(HttpServletRequest request, ResponseLoginVo responseLoginVo) {
		// set Session
		logger.info("set Session [{}] ", responseLoginVo.getAccount_uid());
		WebUtils.setSessionAttribute(request, DefaultController.SESSION_ATTR_KEY_ACCOUNT, responseLoginVo);
		WebUtils.setSessionAttribute(request, DefaultController.SESSION_ATTR_KEY_ACCESS_TOKEN,
				responseLoginVo.getAccess_token());
	}

	protected ResponseEntity<ResponseLogin> returnSuccess(ResponseLoginVo responseLoginVo, String redirect_url) {
		ResponseLogin returnBodyData = new ResponseLogin();
		returnBodyData.setMsg(Return_Message.SUCCESS.getMessage());
		returnBodyData.setResponseLoginVo(responseLoginVo);
		returnBodyData.setForward_url(redirect_url);
		return new ResponseEntity<ResponseLogin>(returnBodyData, HttpStatus.OK);
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

	public String getReturnDangerUrl(ResponseLoginVo responseLoginVo, boolean isFirstLogin) {

		if (isFirstLogin) {
			if (responseLoginVo.getIs_admin() == 1) {
				return DefaultController.REQUESTMAPPING_DANGER_ROOT + "/sadmin/accountInfoModify.do";
			} else {
				return DefaultController.REQUESTMAPPING_DANGER_ROOT + "/admin/accountInfoModify.do";
			}
		}

		if (responseLoginVo.getIs_admin() == 1) {
			return DefaultController.REQUESTMAPPING_DANGER_ROOT + "/sadmin/restrict_area.do";
		}

		if (!(boolean) context.getAttribute("g_access_config"))
			return DefaultController.REQUESTMAPPING_DANGER_ROOT + "/admin/work_status_map.do";

		return DefaultController.REQUESTMAPPING_DANGER_ROOT + "/admin/worker_status_map.do";
	}

	public String getReturnVehicleUrl(ResponseLoginVo responseLoginVo, boolean isFirstLogin) {

		if (isFirstLogin) {
			if (responseLoginVo.getIs_admin() == 1) {
				return "/vehicle/sadmin/accountModify.do?account_uid=" + responseLoginVo.getAccount_uid();
			} else {
				return "/vehicle/admin/accountModify.do?account_uid=" + responseLoginVo.getAccount_uid();
			}
		}

		if (responseLoginVo.getIs_admin() == 1) {
			return "/vehicle/sadmin/driving_rule.do";
		}

		return "/vehicle/admin/vehicle_driving.do";
	}

	public ResponseEntity<ResponseLogin> resetPassword(ResetLoginVo resetLoginVo, SystemType system_type,
			HttpServletRequest request) {
		LoginVo loginVo = null;
		ResponseLoginVo responseLoginVo = null;
		String user_id = "";
		String email = "";
		String username = "";
		String system_Type_str = "";

		user_id = resetLoginVo.getUser_id();
		email = resetLoginVo.getEmail();
		username = resetLoginVo.getName();
		system_Type_str = String.valueOf(system_type.getValue());

		logger.info("user_id [{}] email [{}]", user_id, email);

		// check id
		try {
			loginVo = new LoginVo(system_Type_str, user_id, "");
			responseLoginVo = selectLoginInfo(loginVo);
		} catch (Exception e) {
			return returnInternalServerError(e);
		}

		if (responseLoginVo == null) {
			return returnLoginApiMessage("NOMATCHID");
		}

		// if vehicle, check name
		if (SystemType.VEHICLE.equals(system_type)) {
			if (!username.equals(responseLoginVo.getName())) {
				return returnLoginApiMessage("NOMATCHNAME");
			}
		}

		// check email
		if (!email.equals(responseLoginVo.getEmail())) {
			return returnLoginApiMessage("NOMATCHEMAIL");
		}

		// new password & encript
		String newPassword = RandomStringUtils.randomAlphanumeric(12);
		/*
		 * if encode
		 */
		// String encode_newPassword = newPassword;
		String encode_newPassword = passwordEncoder.encode(newPassword);
		logger.info("newPassword [{}] encode_newPassword [{}] ", newPassword, encode_newPassword);

		// account update upd_date
		logger.info("account update upd_date [{}] ", responseLoginVo.getAccount_uid());
		try {
			resetLoginVo.setSystem_type(system_Type_str);
			resetLoginVo.setPassword(encode_newPassword);
			updateResetPassword(resetLoginVo);
		} catch (Exception e) {
			return returnInternalServerError(e);
		}

		// send email
		try {

			// To-Do
			mailService.sendMail("", email, "SmartPlant 임시 비밀번호 입니다.", "재발급된 임시 비밀번호는 " + newPassword + " 입니다.");

		} catch (ServletException e) {
			logger.info("Exception [{}]", e.getMessage());
			return returnLoginApiMessage("FAILEMAIL");
		} catch (IOException e) {
			logger.info("Exception [{}]", e.getMessage());
			return returnLoginApiMessage("FAILEMAIL");
		}

		return returnLoginApiMessage(Return_Message.SUCCESS.getMessage());
	}

	public ResponseEntity<ResponseLogin> selectSearchId(ResetLoginVo resetLoginVo, SystemType system_type,
			HttpServletRequest request) {
		ResponseLoginVo responseLoginVo = null;

		String email = "";
		String username = "";
		String system_Type_str = "";

		email = resetLoginVo.getEmail();
		username = resetLoginVo.getName();
		system_Type_str = String.valueOf(system_type.getValue());

		logger.info("name [{}] email [{}]", username, email);

		// check id
		try {
			resetLoginVo.setSystem_type(system_Type_str);
			responseLoginVo = loginApimapper.selectSearchId(resetLoginVo);
		} catch (Exception e) {
			return returnInternalServerError(e);
		}

		if (responseLoginVo == null || "".equals(responseLoginVo.getUser_id())) {
			return returnLoginApiMessage("NOMATCHID");
		}

		// send email
		try {
			mailService.sendMail("", email, "SmartPlant 아이디 찾기 입니다.",
					"SmartPlant 아이디는 " + responseLoginVo.getUser_id() + " 입니다.");
		} catch (ServletException e) {
			logger.info("Exception [{}]", e.getMessage());
			return returnLoginApiMessage("FAILEMAIL");
		} catch (IOException e) {
			logger.info("Exception [{}]", e.getMessage());
			return returnLoginApiMessage("FAILEMAIL");
		}

		return returnLoginApiMessage(Return_Message.SUCCESS.getMessage());
	}

	protected ResponseEntity<ResponseLogin> returnLoginApiMessage(String message) {
		logger.info("Login Api return Message [{}]", message);
		ResponseLogin returnBodyData = new ResponseLogin();
		returnBodyData.setMsg(message);
		return new ResponseEntity<ResponseLogin>(returnBodyData, HttpStatus.OK);
	}

	protected ResponseEntity<ResponseLogin> returnInternalServerError(Exception e) {
		logger.info("Exception [{}]", e.getMessage());
		e.printStackTrace();
		return new ResponseEntity<ResponseLogin>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

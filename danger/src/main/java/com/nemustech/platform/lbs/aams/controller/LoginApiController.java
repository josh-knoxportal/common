package com.nemustech.platform.lbs.aams.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.util.WebUtils;

import com.nemustech.platform.lbs.aams.service.LoginApiService;
import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.model.ResponseLogin;
import com.nemustech.platform.lbs.common.util.SessionUtil;
import com.nemustech.platform.lbs.common.vo.LoginVo;
import com.nemustech.platform.lbs.common.vo.ResetLoginVo;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;
import com.nemustech.platform.lbs.common.vo.SystemType;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class LoginApiController extends DefaultController {
	private static final Logger logger = LoggerFactory.getLogger(LoginApiController.class);

	@Autowired
	private LoginApiService loginApiService;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "", notes = "차량 권한체크.", response = ResponseLogin.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "위험차량 로그인 성공 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "v1/ngms/admincheck.do", method = RequestMethod.POST)
	public ResponseEntity<String> v1ajaxAuth(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type)
			throws NotFoundException {
		JSONObject jobj = new JSONObject();

		try {
			ResponseLoginVo responseLoginVo = SessionUtil.getAccountInfo();
			jobj.put("is_login", SessionUtil.isLogin() == false ? 0 : 1);
			if (responseLoginVo != null) {
				jobj.put("is_admin", responseLoginVo.getIs_admin());
				jobj.put("system_type", responseLoginVo.getSystem_type());
			} else {
				jobj.put("is_admin", null);
				jobj.put("system_type", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<String>(jobj.toJSONString(), HttpStatus.OK);
	}

	@ApiOperation(value = "", notes = "위험지역 로그인.", response = ResponseLogin.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "로그인 성공 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "v1/ngms/danger/login.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseLogin> v1NgmsDangerLoginPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "login info", required = true) @RequestBody LoginVo loginVo, HttpServletRequest request)
			throws NotFoundException {

		/*
		 * 1 : 위험지역, 2 : 위험차량
		 */
		return checkLoginInfo(loginVo, SystemType.DANGER, request);
	}

	private ResponseEntity<ResponseLogin> checkLoginInfo(LoginVo loginVo, SystemType system_type,
			HttpServletRequest request) {

		if (loginVo == null) {
			return new ResponseEntity<ResponseLogin>(HttpStatus.NO_CONTENT);
		}

		return loginApiService.chekLoginProcess(loginVo, system_type, request);
	}

	@RequestMapping(value = "{system_type}/logout.do", method = RequestMethod.GET)
	public String doLogout(HttpServletRequest request, HttpServletResponse response, Model model,
			SessionStatus sessionStatus, @PathVariable(value = "system_type") int system_type) throws Exception {
		logger.info("{} {}", request.getMethod(), request.getRequestURI());
		logger.info("model: " + model);

		String redirect_url = null;

		redirect_url = "redirect:" + getURLWithContextPath(request) + "/admin/login.do";
		logger.info("logout History");
		loginApiService.insertDangerLoginHistory(SessionUtil.getAccountInfo().getUser_id(), 0);

		// clear access_token column?
		WebUtils.setSessionAttribute(request, DefaultController.SESSION_ATTR_KEY_ACCOUNT, null);
		WebUtils.setSessionAttribute(request, DefaultController.SESSION_ATTR_KEY_ACCESS_TOKEN, null);

		sessionStatus.setComplete();
		request.getSession().invalidate();

		logger.info("redirect_url : {}", redirect_url);
		return redirect_url;

	}

	public static String getURLWithContextPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}

	@ApiOperation(value = "", notes = "위험지역 로그인 비밀번호 재설정", response = ResponseLogin.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "비밀번호 재설정 성공 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "v1/ngms/danger/resetPassword.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseLogin> doDangerResetPassword(HttpServletRequest request,
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "reset password info", required = true) @RequestBody ResetLoginVo resetLoginVo)
			throws Exception {
		logger.info("{} {}", request.getMethod(), request.getRequestURI());
		return loginApiService.resetPassword(resetLoginVo, SystemType.DANGER, request);
	}

	@ApiOperation(value = "", notes = "위험지역 로그인 아이디찾기", response = ResponseLogin.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "아이디 찾기 성공 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "v1/ngms/danger/searchId.do", method = RequestMethod.POST)
	public ResponseEntity<ResponseLogin> doDangerSearchid(HttpServletRequest request,
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "reset password info", required = true) @RequestBody ResetLoginVo resetLoginVo)
			throws Exception {
		logger.info("{} {}", request.getMethod(), request.getRequestURI());
		return loginApiService.selectSearchId(resetLoginVo, SystemType.DANGER, request);
	}

}

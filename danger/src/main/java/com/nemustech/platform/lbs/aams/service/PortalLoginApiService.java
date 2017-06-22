package com.nemustech.platform.lbs.aams.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lgcns.encypt.EncryptUtil;
import com.nemustech.platform.lbs.aams.controller.PortalLoiginApiController;
import com.nemustech.platform.lbs.aams.mapper.LoginApiMapper;
import com.nemustech.platform.lbs.common.service.PropertyService;
import com.nemustech.platform.lbs.common.vo.LoginVo;
import com.nemustech.platform.lbs.common.vo.PortalLoginVo;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;
import com.nemustech.platform.lbs.common.vo.SystemType;

@Service
public class PortalLoginApiService {
	private static final Logger logger = LoggerFactory.getLogger(PortalLoiginApiController.class);

	@Autowired
	PropertyService propertyService;

	@Autowired
	private LoginApiService loginApiService;

	@Autowired
	private LoginApiMapper loginApimapper;

	@Autowired
	private AuthService authService;

	public String processPortalLogin(HttpServletRequest request, String system_type, PortalLoginVo portalLoginVo)
			throws Exception {

		portalLoginVo = getEncParameter(portalLoginVo);

		if (!isValidParamter(portalLoginVo)) {
			return getReturnInitPage(request);
		}

		portalLoginVo.setSystem_type(system_type);
		return doPortalLoginStepOne(portalLoginVo, request);
	}

	public PortalLoginVo getEncParameter(PortalLoginVo portalLoginVo) {

		try {
			// 파라미터는 아이디, 이메일만 체크함
			// 사번은 관리하지 않으므로 패스
			String user_id = getDecryptParameter(portalLoginVo.getEncryptUserId());
			if (!StringUtils.isEmpty(user_id)) {
				portalLoginVo.setUser_id(user_id);
			}

			// String email =
			// getDecryptParameter(portalLoginVo.getDecryptMail());
			// if (!StringUtils.isEmpty(email)) {
			// portalLoginVo.setEmail(email);
			// }
		} catch (Exception e) {
			logger.error("getEncParameter exception !!");
			return portalLoginVo;
		}

		return portalLoginVo;
	}

	private String getDecryptParameter(String encryptParameter) {

		logger.info("encryptParameter [{}]", encryptParameter);
		// 복호화
		String portal_encrypt_key = propertyService.getString("portal.encrypt.key");
		String decryptStr = EncryptUtil.decryptText(encryptParameter, portal_encrypt_key);
		logger.info("Decrypt datetime+encryptParameter : " + decryptStr);

		String[] decryptArr = decryptStr.split("\\|", 0);
		if (decryptArr.length < 2) {
			logger.info("Error!!");
			return "";
		}

		// yyyyMMddHHmmss 포맷의 Datetime (GMT+0), 파리미터 유효성 검증에 활용
		logger.info("Decrypt datetime : " + decryptArr[0]);

		// 복호화된 파라미터 값
		logger.info("Decrypt encryptParameter : " + decryptArr[1]);

		return decryptArr[1];
	}

	public boolean isValidParamter(PortalLoginVo portalLoginVo) {

		if (StringUtils.isEmpty(portalLoginVo.getUser_id())) {
			logger.info("=== LG Portal param portalLoginVo User Id is null");
			return false;
		}
		return true;
	}

	public String doPortalLoginStepOne(PortalLoginVo portalLoginVo, HttpServletRequest request) {

		ResponseLoginVo responseLoginVo = null;

		// 1. check exist id
		try {
			responseLoginVo = loginApimapper.selectIsExistPortalId(portalLoginVo);
		} catch (Exception e) {
			e.printStackTrace();
			return getReturnInitPage(request);
		}

		// 2. not exist, return main page
		if (StringUtils.isEmpty(responseLoginVo)) {
			logger.info("return responseLoginVo is null");
			return getReturnInitPage(request);
		}

		// 3. login step
		return doPortalLoginStepTwo(portalLoginVo, responseLoginVo, request);
	}

	private String doPortalLoginStepTwo(PortalLoginVo portalLoginVo, ResponseLoginVo responseLoginVo,
			HttpServletRequest request) {

		String user_id = "";
		String access_token = "";
		String system_type = portalLoginVo.getSystem_type();
		LoginVo loginVo = new LoginVo();

		// do token
		try {
			loginVo.setUser_id(portalLoginVo.getUser_id());
			loginVo.setPassword(portalLoginVo.getUser_id());
			loginVo.setSystem_type(system_type);
			access_token = authService.generateAccountToken(loginVo);
			responseLoginVo.setAccess_token(access_token);
		} catch (Exception e1) {
			e1.printStackTrace();
			return getReturnInitPage(request);
		}

		// account update upd_date
		logger.info("account update upd_date [{}] ", responseLoginVo.getAccount_uid());
		try {
			loginApiService.updateLoginDate(responseLoginVo);
		} catch (Exception e2) {
			return getReturnInitPage(request);
		}

		// set Session
		loginApiService.setLoginSessionAttribute(request, responseLoginVo);

		// redirect page & history
		String redirect_url = getReturnInitPage(request);

		if (SystemType.DANGER.toString().equals(system_type)) {
			redirect_url = getReturnDangerUrl(responseLoginVo);

			logger.info("login History");
			try {
				loginApiService.insertDangerLoginHistory(user_id, 1);
			} catch (Exception e2) {
				return getReturnInitPage(request);
			}
		}

		logger.info("redirect_url [{}] ", redirect_url);

		return redirect_url;
	}

	protected String getReturnInitPage(HttpServletRequest request) {
		return "danger/portal/loginError";
	}

	private String getReturnDangerUrl(ResponseLoginVo responseLoginVo) {

		if (responseLoginVo.getIs_admin() == 1) {
			return "danger/sadmin/map/RestrictArea";
		}

		return "danger/admin/map/WorkerStatusMap";
	}
}

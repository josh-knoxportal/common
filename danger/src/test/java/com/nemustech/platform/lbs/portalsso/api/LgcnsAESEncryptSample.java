package com.nemustech.platform.lbs.portalsso.api;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lgcns.encypt.EncryptUtil;

public class LgcnsAESEncryptSample {

	private static final Logger logger = LoggerFactory.getLogger(LgcnsAESEncryptSample.class);

	public static void main(String[] args) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		TimeZone jst = TimeZone.getTimeZone("GMT+0");
		java.util.Calendar cal = java.util.Calendar.getInstance(jst);
		sdf.setTimeZone(cal.getTimeZone());

		String str = sdf.format(cal.getTime());
		String encryptEmpNo = EncryptUtil.encryptText(str + "|" + "Z123456");
		logger.info("================== Encrypt EmpNo : " + encryptEmpNo);

		String encryptMail = EncryptUtil.encryptText(str + "|" + "sangfroi@naver.com");
		logger.info("================== Encrypt email : " + encryptMail);

		String encryptUserId = EncryptUtil.encryptText(str + "|" + "danger");
		logger.info("================== Encrypt danger : " + encryptUserId);

		String encryptUserId2 = EncryptUtil.encryptText(str + "|" + "user");
		logger.info("================== Encrypt user : " + encryptUserId2);

		/* URL Parameter통해 전달 받은 경우 예제 */

		// G-Portal에서 넘겨주는 파라미터 명 ( GET 방식의 경우 URLEncoder/URLDecoder 를 활용하여야 함.
		// 사번 : encryptEmpNo --> (String)request.getParameter(“encryptEmpNo”);
		// 메일주소 : encryptMail --> (String)request.getParameter(“encryptMail”);
		// 유저 ID : encryptUserId -->
		// (String)request.getParameter(“encryptUserId”);

		String decryptEmpNo = getDecryptParameter(encryptEmpNo);
		String decryptMail = getDecryptParameter(encryptMail);
		String decryptUserId = getDecryptParameter(encryptUserId);

		logger.debug("Decrypt encryptParameter [" + decryptEmpNo + "] [" + decryptMail + "] [" + decryptUserId + "] ");
	}

	protected static String getDecryptParameter(String encryptParameter) {

		logger.info(encryptParameter);
		// 복호화
		String decryptStr = EncryptUtil.decryptText(encryptParameter, "ThisIsIkepSecurityKey");
		logger.info("Decrypt datetime+encryptParameter : " + decryptStr);

		String[] decryptArr = decryptStr.split("\\|", 0);
		if (decryptArr.length < 2) {
			logger.info("Error!!");
			return "Error";
		}

		// yyyyMMddHHmmss 포맷의 Datetime (GMT+0), 파리미터 유효성 검증에 활용
		logger.info("Decrypt datetime : " + decryptArr[0]);

		// 복호화된 파라미터 값
		logger.info("Decrypt encryptParameter : " + decryptArr[1]);

		return decryptArr[1];
	}
}

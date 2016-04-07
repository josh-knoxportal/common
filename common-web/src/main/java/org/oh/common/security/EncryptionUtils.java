package org.oh.common.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.oh.common.exception.CommonException;
import org.oh.common.util.LogUtil;

public abstract class EncryptionUtils {
	public static String seedDecode(String text, String key) {
		String decode = "";
		SeedCipher seed = new SeedCipher();
		try {
			if (text.equals("") || text.equals(" ")) {
				decode = text;
			} else {
				decode = seed.decryptAsString(Base64.decode(text), key.getBytes());
			}
		} catch (IOException e) {
			LogUtil.writeLog(e, EncryptionUtils.class);
			decode = " ";
		}
		return decode.trim();
	}

	public static String seedEncode(String text, String key) {
		String encode = null;
		if (text.length() == 1) {
			text = text + " ";
		}
		SeedCipher seed = new SeedCipher();
		try {
			encode = Base64.encodeBytes((seed.encrypt(text.getBytes("UTF-8"), key.getBytes())));
		} catch (UnsupportedEncodingException e) {
			throw new CommonException(CommonException.ERROR, e.getMessage(), e);
		}

		return encode;
	}

	/**
	 * 암호화 비밀번호 세팅
	 * 
	 * @return
	 */
	public static String dateSetting() {
		String nowDate = "";
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyyMMdd");
		nowDate = sdf.format(cal.getTime());
		sdf.applyPattern("ddMMyyyy");
		nowDate = nowDate + sdf.format(cal.getTime());
		return nowDate;
	}
}
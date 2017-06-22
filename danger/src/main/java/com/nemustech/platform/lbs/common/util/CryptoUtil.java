package com.nemustech.platform.lbs.common.util;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

public class CryptoUtil {

	// private final static Log log = LogFactory.getLog(CryptoUtil.class);

	public static String encodeBinary(byte[] data) throws Exception {
		if (data == null)
			return "";
		return new String(Base64.encodeBase64(data));
	}

	public static String encode(String data) throws Exception {
		return encodeBinary(data.getBytes());
	}

	/**
	 * 데이터를 복호화하는 기능
	 * 
	 * @param String
	 *            data 복호화할 데이터
	 * @return String result 복호화된 데이터
	 * @exception Exception
	 */
	public static byte[] decodeBinary(String data) throws Exception {
		return Base64.decodeBase64(data.getBytes());
	}

	/**
	 * 데이터를 복호화하는 기능
	 * 
	 * @param String
	 *            data 복호화할 데이터
	 * @return String result 복호화된 데이터
	 * @exception Exception
	 */
	public static String decode(String data) throws Exception {
		return new String(decodeBinary(data));
	}

	/**
	 * SHA-256 방식을 이용하여 단방향 패스워드 생성한다.
	 *
	 * @param str
	 *            패스워드 문자열
	 * @return String
	 */
	public static String getOneWayPassword(String str) throws Exception {
		if (str == null || str.equals(""))
			return str;

		byte[] bytes = str.getBytes("UTF-8");

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] byteData = md.digest(bytes);

		String ab = Base64.encodeBase64String(byteData);
		return ab;

	}

	/*
	 * public static String getOneWayPassword(String str) throws Exception{ if
	 * (str == null || str.equals("")) return str; String password = str;
	 * 
	 * MessageDigest md = MessageDigest.getInstance("SHA-256");
	 * md.update((password).getBytes()); byte byteData[] = md.digest();
	 * 
	 * //convert the byte to hex format method 1 StringBuffer sb = new
	 * StringBuffer(); for (int i = 0; i < byteData.length; i++) {
	 * sb.append(Integer.toString((byteData[i] & 0xff) + 0x100,
	 * 16).substring(1)); } return sb.toString(); }
	 */

	public static void main(String[] args) {

		try {
			System.out.println(CryptoUtil.getOneWayPassword("1234"));
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}

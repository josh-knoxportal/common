package org.oh.common.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * 인코딩/디코딩을 처리한다. <br/>
 * 현재 지원하는 코덱은 BASE64, HEX(16진수), HEXSTR(콜론(":")으로 구분한 16진수) 이며 코덱을 지정하지 않을 경우는 UTF-8인코딩을 한다.
 */
public abstract class CodecUtil {
	public enum CODEC {
		BASE64, HEX, HEXSTR, UTF8
	}

	/**
	 * 바이트 배열을 인코딩 해서 돌려 준다.
	 * 
	 * <pre>
	 * 예)
	 *      byte[] orgins = "ABCDEF1000".getBytes();
	 *      <br>
	 *      String encoded_HEX      = CodecUtil.getString(orgins, CODEC.HEX);       // HEX 인코딩 문자열 리턴 
	 *      String encoded_BASE64   = CodecUtil.getString(orgins, CODEC.BASE64);    // BASE64 인코딩 문자열 리턴
	 *      String encoded_HEXSTR   = CodecUtil.getString(orgins, CODEC.HEXSTR);    // HEXSTR 인코딩 문자열 리턴
	 * </pre>
	 * 
	 * @param bytes 인코딩할 바이트 배열
	 * @param codec 인코딩 방법
	 * @return 인코딩된 문자열
	 */
	public static String encode(byte[] bytes, CODEC codec) {
		String str = null;

		switch (codec) {
			case BASE64:
				str = base64encode(bytes);
				break;

			case HEX:
				str = hexEncode(bytes);
				break;

			case HEXSTR:
				str = hexStringEncode(bytes);
				break;

			default:
				try {
					str = new String(bytes, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					LogUtil.writeLog(e, CodecUtil.class);
					str = "";
				}
		}

		return str;
	}

	/**
	 * 인코딩된 문자열을 디코딩해서 돌려 준다.
	 * 
	 * <pre>
	 * 예)
	 *      String hexString = "5:BA:A6:1E:4C:9B:93:F3:F0:68:22:50:B6:CF:83:31:B7:EE:68:FD:8";
	 *      <br>
	 *      byte[] decoded   = CodecUtil.getBytes(hexString, CODEC.HEXSTR); // HEXSTR로 인코딩된 문자열을 디코딩한 바이트 배열 리턴
	 * </pre>
	 * 
	 * @param str 디코딩을 원하는 문자열
	 * @param codec 디코딩 방법
	 * @return 디코딩된 바이트 배열
	 */
	public static byte[] decode(String str, CODEC codec) {
		byte[] decoded = null;

		switch (codec) {
			case BASE64:
				decoded = base64decode(str);
				break;

			case HEX:
				decoded = hexDecode(str);
				break;

			case HEXSTR:
				decoded = hexStringDecode(str);
				break;

			default:
				try {
					decoded = str.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					LogUtil.writeLog(e, CodecUtil.class);
					decoded = str.getBytes();
				}
		}

		return decoded;
	}

	/**
	 * 바이트 배열을 HEX로 인코딩 해서 돌려 준다.
	 *
	 * <pre>
	 * 예)
	 *      byte[] orgins = "ABCDEF1000".getBytes();
	 *      <br>
	 *      String encoded_HEX      = CodecUtil.hexStringEncode(orgins);       // HEX 인코딩 문자열 리턴
	 * </pre>
	 * 
	 * @param bytes 인코딩할 바이트 배열
	 * @return HEX로 인코딩된 문자열
	 */
	protected static String hexStringEncode(byte[] bytes) {
		char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		StringBuffer strBuf = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			strBuf.append(hexChar[(bytes[i] & 0xf0) >>> 4]); // fill left with
			// zero bits
			strBuf.append(":");
			strBuf.append(hexChar[bytes[i] & 0x0f]);
		}

		return strBuf.toString();
	}

	/**
	 * HEX로 인코딩된 문자열을 디코딩해서 바이트 배열로 돌려 준다.
	 *
	 * <pre>
	 * 예)
	 *      String hexString = "5:BA:A6:1E:4C:9B:93:F3:F0:68:22:50:B6:CF:83:31:B7:EE:68:FD:8";
	 *      <br>
	 *      byte[] decodeString   = CodecUtil.hexStringDecode(hexString);       // 디코딩된 바이트 배열 리턴
	 * </pre>
	 * 
	 * @param str HEX로 인코딩된 문자열
	 * @return 디코딩된 바이트 배열
	 */
	protected static byte[] hexStringDecode(String str) {
		return hexDecode(StringUtil.remove(str, ':'));
	}

	@SuppressWarnings("restriction")
	protected static String base64encode(byte[] bytes) {
		String strResult = null;

//		BASE64Encoder base64Encoder = new BASE64Encoder(); // sun.* 라이브러리는 사용안하는 것을 권장함 (2013-03-29)
		try {
//			strResult = base64Encoder.encode(bytes);
			strResult = Base64.encodeBase64String(bytes);
		} catch (Exception e) {
			LogUtil.writeLog(e, CodecUtil.class);
		}

		return strResult;
	}

	@SuppressWarnings("restriction")
	protected static byte[] base64decode(String str) {
		byte[] buf = null;

//		BASE64Decoder base64Decoder = new BASE64Decoder(); // sun.* 라이브러리는 사용안하는 것을 권장함 (2013-03-29)
		try {
//			return base64Decoder.decodeBuffer(str);
			return Base64.decodeBase64(str);
		} catch (Exception e) {
			LogUtil.writeLog(e, CodecUtil.class);
		}

		return buf;
	}

	protected static String hexEncode(byte[] bytes) {
		return Hex.encodeHexString(bytes);
	}

	protected static byte[] hexDecode(String str) {
		try {
			return Hex.decodeHex(str.toCharArray());
		} catch (DecoderException e) {
			LogUtil.writeLog(e, CodecUtil.class);
			return "".getBytes();
		}
	}
}

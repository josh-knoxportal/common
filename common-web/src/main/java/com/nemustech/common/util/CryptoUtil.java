package com.nemustech.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.nemustech.common.util.CodecUtil.CODEC;

/**
 * 암호화 관련 유틸리티 클래스
 */
public abstract class CryptoUtil {
	/** Value - {@value} */
	protected static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	/** Value - {@value} */
	protected static final String HASH_MD5_ALGORITHM = "MD5";
	/** Value - {@value} */
	protected static final String HASH_SHA1_ALGORITHM = "SHA1";

//	protected static final RathonCrypt RATHON_CRYPT = new RathonCrypt();

	protected static String _salt;

	static {
		_salt = "1102CNCM$$";
	}

	/**
	 * 바이트 배열에 대해서 base64로 인코딩된 {@link #HASH_MD5_ALGORITHM} 해쉬값을 돌려 준다.
	 * 
	 * <pre>
	 * 예)
	 *    String plainText = "ABCDEFG한글";
	 *    <br>
	 *    String str = CryptoUtil.doHashMD5String(plainText.getBytes("UTF-8")); // base64로 인코딩된 해쉬값
	 * </pre>
	 * 
	 * @param plainBytes 해쉬값이 필요한 바이트 배열
	 * @return base64로 인코딩된 해쉬값.
	 */
	public static String doHashMD5String(byte[] plainBytes) {
		byte[] hashedBytes = doHashMD5(plainBytes);

		return CodecUtil.encode(hashedBytes, CODEC.BASE64);
	}

	/**
	 * 바이트 배열에 대해서 {@link #HASH_MD5_ALGORITHM} 해쉬값을 돌려 준다.
	 * 
	 * <pre>
	 * 예)
	 *    String plainText = "ABCDEFG한글";
	 *    <br>
	 *    byte[] bytes = CryptoUtil.doHashMD5(plainText.getBytes("UTF-8")); // 해쉬값
	 * </pre>
	 * 
	 * @param plainBytes 해쉬값이 필요한 바이트 배열
	 * @return 해쉬값
	 */
	public static byte[] doHashMD5(byte[] plainBytes) {
		byte[] hashBytes = null;
		MessageDigest _digester;

		try {
			_digester = MessageDigest.getInstance(HASH_MD5_ALGORITHM);
			_digester.update(plainBytes, 0, plainBytes.length);
			hashBytes = _digester.digest();
		} catch (Exception e) {
			LogUtil.writeLog(e, CryptoUtil.class);
			hashBytes = null;
		}

		return hashBytes;
	}

	/**
	 * 단방향 암호화 함수.<br>
	 * 바이트 배열에 대해서 base64로 인코딩된 {@link #HASH_SHA1_ALGORITHM} 해쉬값을 돌려 준다.
	 * 
	 * <pre>
	 * 예)
	 *    String plainText = "ABCDEFG한글";
	 *    <br>
	 *    byte[] bytes = CryptoUtil.doHashSHA1String(plainText.getBytes("UTF-8")); // 해쉬값
	 * </pre>
	 * 
	 * @param plainBytes 해쉬값이 필요한 바이트 배열
	 * @return base64로 인코딩된 해쉬값.
	 */
	public static String doHashSHA1String(byte[] plainBytes) {
		byte[] hashedBytes = doHashSHA1(plainBytes);

		return CodecUtil.encode(hashedBytes, CODEC.BASE64);
	}

	/**
	 * 단방향 암호화 함수.<br>
	 * 주어진 문자열에 대해서 base64로 인코딩된 {@link #HASH_SHA1_ALGORITHM} 해쉬값을 돌려 준다.
	 * 
	 * <pre>
	 * 예)
	 *    String plainText = "ABCDEFG한글";
	 *    <br>
	 *    byte[] bytes = CryptoUtil.doHashSHA1String(plainText, "UTF-8"); // 해쉬값
	 * </pre>
	 *
	 * @param plainText 해쉬값이 필요한 문자열
	 * @param charsetName 문자셋명
	 * @return base64로 인코딩된 해쉬값.
	 */
	public static String doHashSHA1String(String plainText, String charsetName) {
		try {
			return doHashSHA1String(plainText.getBytes(charsetName));
		} catch (Exception e) {
			LogUtil.writeLog(e, CryptoUtil.class);
		}

		return null;
	}

	/**
	 * 바이트 배열에 대해서 {@link #HASH_SHA1_ALGORITHM} 해쉬값을 돌려 준다.
	 * 
	 * @param plainBytes 해쉬값이 필요한 바이트 배열
	 * @return 해쉬값
	 */
	public static byte[] doHashSHA1(byte[] plainBytes) {
		byte[] hashBytes = null;
		MessageDigest _digester;

		try {
			_digester = MessageDigest.getInstance(HASH_SHA1_ALGORITHM);
			_digester.update(plainBytes, 0, plainBytes.length);
			hashBytes = _digester.digest();
		} catch (Exception e) {
			LogUtil.writeLog(e, CryptoUtil.class);
			hashBytes = null;
		}

		return hashBytes;
	}

	/**
	 * 바이트 배열에 대해서 {@link #HMAC_SHA1_ALGORITHM} HMAC(Hashed Message Authenticate Code)값을 돌려 준다.
	 * 
	 * @param plainBytes HMAC값이 필요한 바이트 배열
	 * @param key HMAC 값 생성에 필요한 key 값
	 * @return HMAC 값
	 */
	public static byte[] doHmac(byte[] plainBytes, String key) {
		byte[] hmacBytes = null;
		SecretKeySpec signingKey;
		Mac mac;

		try {
			signingKey = new SecretKeySpec(doHashMD5((_salt + key).getBytes("UTF-8")), HMAC_SHA1_ALGORITHM);
		} catch (UnsupportedEncodingException e) {
			signingKey = new SecretKeySpec(doHashMD5((_salt + key).getBytes()), HMAC_SHA1_ALGORITHM);
		}

		try {
			mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			hmacBytes = mac.doFinal(plainBytes);
		} catch (Exception e) {
			LogUtil.writeLog(e, CryptoUtil.class);
			hmacBytes = null;
		}

		return hmacBytes;
	}

	/**
	 * 바이트 배열에 대해서 {@link #HMAC_SHA1_ALGORITHM} HMAC(Hashed Message Authenticate Code)값을 돌려 준다.
	 * 
	 * @param plainBytes HMAC값이 필요한 바이트 배열
	 * @param key HMAC 값 생성에 필요한 key 값
	 * @return base64로 인코딩된 HMAC 값
	 */
	public static String doHmacString(byte[] plainBytes, String key) {
		byte[] hmacBytes = doHmac(plainBytes, key);

		return CodecUtil.encode(hmacBytes, CODEC.BASE64);
	}

	public static String encodeMD5(String str) {
		return encode(str, "MD5");
	}

	public static String encodeSHA256(String str) {
		return encode(str, "SHA-256");
	}

//	public static String encodeSHA256Rathon(String str) {
//		if (str == null)
//			return str;
//
//		return RATHON_CRYPT.encodeSHA256(str);
//	}

	/**
	 * 암호화한다.
	 * 
	 * @param str
	 * @param algorithm MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512
	 * @return
	 */
	public static String encode(String str, String algorithm) {
		if (str == null)
			return str;

		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

			messageDigest.update(str.getBytes());
			byte byteData[] = messageDigest.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			return sb.toString();
		} catch (Exception e) {
			LogUtil.writeLog(e, CryptoUtil.class);

			return str;
		}
	}

	public static void main(String[] args) {
		System.out.println(encodeMD5("a1234")); // 828c88f34ecb4c1ca8d89e018c6fad1a
		System.out.println(encodeSHA256("a1234")); // 3e0a3501a65b4a7bf889c6f180cc6e35747e5aaff931cc90b760671efa09aeac
//		System.out.println(encodeSHA256Rathon("a1234"));
	}
}

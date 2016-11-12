package com.nemustech.common.util;

import org.apache.commons.codec.binary.Base64;
import com.nemustech.common.security.EncryptionUtils;

/**
 * The Class SecurityUtils.
 */
public abstract class SecurityUtils {
	/**
	 * Gets the base64 encode string.
	 *
	 * @param str the str
	 * @return the base64 encode string
	 */
	public final static String getBase64EncodeString(String str) {
		return new String(Base64.encodeBase64(str.getBytes()));
	}

	/**
	 * Gets the base64 decode string.
	 *
	 * @param str the str
	 * @return the base64 decode string
	 */
	public final static String getBase64DecodeString(String str) {
		return new String(Base64.decodeBase64(str.getBytes()));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] str = { "22003330", "22001537", "22004460" };
		String[] strEnc = new String[3];

		for (int i = 0; i < str.length; i++) {
			strEnc[i] = SecurityUtils.getBase64EncodeString(EncryptionUtils.seedEncode(str[i], "9987665433210000"));

			System.out.println(str[i] + ": " + strEnc[i]);
		}
	}
}

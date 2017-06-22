package com.nemustech.platform.lbs.common.api;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nemustech.platform.lbs.common.util.Const;

public class PasswordEncoderTest {

	@Test
	public void test() {
		String password = "niabms!1121212121";
		String password2 = "0327";
		// base64

		byte[] encoded = Base64.encodeBase64(password2.getBytes());
		byte[] decoded = Base64.decodeBase64(encoded);
		System.out.println("인코딩 전 : " + password2);
		System.out.println("인코딩 text : " + new String(encoded));
		System.out.println("디코딩 text : " + new String(decoded));

		// default
		SHAPasswordEncoder shaPasswordEncoderdefault = new SHAPasswordEncoder();
		shaPasswordEncoderdefault.setEncodeHashAsBase64(true);
		System.out.println("default SHA 암호화: " + shaPasswordEncoderdefault.encode(password));
		System.out.println("default SHA 비교: "
				+ shaPasswordEncoderdefault.matches(password, shaPasswordEncoderdefault.encode(password)));

		// new 512, true
		SHAPasswordEncoder shaPasswordEncoder = new SHAPasswordEncoder(Const.SHA_512);
		shaPasswordEncoder.setEncodeHashAsBase64(true);
		System.out.println("shaPasswordEncoder SHA 암호화: " + shaPasswordEncoder.encode(password));
		System.out.println("SHA 비교: " + shaPasswordEncoder.matches(password, shaPasswordEncoder.encode(password)));
		System.out.println("SHA 비교: "
				+ shaPasswordEncoder.matches(shaPasswordEncoder.encode(password), shaPasswordEncoder.encode(password)));

		PasswordEncoding passwordEncoding = new PasswordEncoding(shaPasswordEncoder);
		System.out.println("passwordEncoding   SHA 암호화: " + passwordEncoding.encode(password));
		System.out.println("SHA 비교: " + passwordEncoding.matches(password, passwordEncoding.encode(password)));

		// encode false
		SHAPasswordEncoder shaPasswordEncoderfalse = new SHAPasswordEncoder(Const.SHA_512);
		shaPasswordEncoderfalse.setEncodeHashAsBase64(false);
		System.out.println("SHA 암호화: " + shaPasswordEncoderfalse.encode(password));
		System.out.println("SHA 비교: " + shaPasswordEncoderfalse.matches(password, shaPasswordEncoder.encode(password)));

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		passwordEncoding = new PasswordEncoding(passwordEncoder);
		System.out.println("1. BCrypt 암호화: " + passwordEncoding.encode(password));
		System.out.println("1. BCrypt 비교: " + passwordEncoding.matches(password, passwordEncoding.encode(password)));

		// 다시 암호화
		System.out.println("2. BCrypt 암호화: " + passwordEncoding.encode(password));
		System.out.println("2. BCrypt 비교: " + passwordEncoding.matches(password, passwordEncoding.encode(password)));
	}
}

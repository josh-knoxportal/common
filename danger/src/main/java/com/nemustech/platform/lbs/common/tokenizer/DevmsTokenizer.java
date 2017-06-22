package com.nemustech.platform.lbs.common.tokenizer;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DevmsTokenizer {
	private static final String PRIV_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJN9w7Kbe62HjyH4oSeR/zBjGn5PV1e+QoLHtw82XnfV7pqDnXT8NGGKESQZUKCFzN5YMa9lEBX2BvnRL4CJDn1oC3Fcikb7jYvyzAT7y7P4wY8t3JnR0D7sevGpXsjterFu5YJ3jA+gJmg9OaPrV1Csxmbopz/CVWV+ZZSJNZdHAgMBAAECgYBvj92LKBiw9azKoWokqEZCr3PGvvTuJbfhfHd3RkTGmEdXEN71Mh/c97Zne86E3TBNrqBGb0dvlR1JsXUg4IrYIjpxQR0h2uQ38ibQaVW1eU/C/zqvd0ZBPcVe7I0OJe4c0SFQawkOIGjoo2gfOhK5Lbcsr/E7O0lel/xxe1kcIQJBANCl7tEP5n4VnVRGwnqOal5ctUNMpf1DYr14M6vXW1m4lkWYV0fbqFtbfVb91fOByrlE+p8wudQsR1SHIt6NRysCQQC09rrmIOWaQN31JBj2lHHeSVr8PGG25hhLe6SX2zK7K4uEiBg4tWAtBYlGCTkajFG/x2/tWCQoraRtB3tF6+JVAkAIGJg2toY8GDydm41KUiJgfPpvnRbHyAiZz4M5xMZ/qcDy/GFOoYBb4yXu7TSTGVMzczDCYpKjYAB4kDHTLuo3AkEAki7hyfb3IwWVh2rSk5ZU6bkeO7ZXRRVYuNk3m8hhkdtaSARcdKrn91aKjr3ymCSUATUNkJBsHUQwGDc7+L7VRQJBAKLxvEgIe/S+Wo5ytmb/1Wnn6xGSoUwGARLs6jFiRs8uFYCvf57cm3KfsW08PNJElQ5OC7Qy0wtRuDJM9kbutd4=";
	private static final String TOKEN_NAME = "slbs-token";
	private static final String MASTER_TOK = "slbs-master";
	
	private static Device device = new Device();
	
	public static boolean decryptAccessToken(String token) {
		if(token == null || token.length() == 0) {
			return false;
		}
		

		String decText = null;
		byte[] cipherByte = null;
		PrivateKey privateKey = null;
		try {
			cipherByte = RSAUtil.hexToBytes(token);
			if(cipherByte==null) { 
				return false;
			}
			privateKey = RSAUtil.convertStringToPrivateKey(PRIV_KEY);
			if(privateKey == null) {
				return false;
			}
			decText = new String(RSAUtil.decryptWithPrivKey(cipherByte,privateKey),"UTF-8");
		} catch (InvalidKeyException e) {
			return false;
		} catch (UnsupportedEncodingException e) {
			return false;
		} catch (IllegalBlockSizeException e) {
			return false;
		} catch (BadPaddingException e) {
			return false;
		} catch (NoSuchAlgorithmException e) {
			return false;
		} catch (NoSuchPaddingException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
		if(decText == null || decText.length() == 0) {
			return false;
		}
		
		String[] text_arr = decText.split(":");
		
		String tokenName = text_arr[0].trim();
		if(tokenName.equals(MASTER_TOK)) {
			return true;
		}
		
		if(!tokenName.equals(TOKEN_NAME)) {
			return false;
		}
		
		long expire_time = Long.parseLong(text_arr[1].trim());
		long now = System.currentTimeMillis();
		if( expire_time < now) {
			return false;
		}
		
		device.setExpire_time(expire_time);
		device.setNow(now);
		device.setDevice_id(text_arr[2]);
		device.setModel(text_arr[3]);
		device.setOs(text_arr[4]);
		device.setOs_version(text_arr[5]);
		
		return true;
	}
	
	public static String getDevice_Id() {
		return device.getDevice_id();
	}
	
	public static String getModel() {
		return device.getModel();
	}
	
	public static String getOs() {
		return device.getOs();
	}
	
	public static String getOs_version() {
		return device.getOs_version();
	}
	
	public static long getExpire_time() {
		return device.getExpire_time();
	}
	
	public static long getNow() {
		return device.getNow();
	}
	
	public static Device decryptAccessTokenDebug(String token) {
		if(token == null || token.length() == 0) {
			return null;
		}
		

		String decText = null;
		byte[] cipherByte = null;
		PrivateKey privateKey = null;
		try {
			cipherByte = RSAUtil.hexToBytes(token);
			if(cipherByte==null) { 
				return null;
			}
			privateKey = RSAUtil.convertStringToPrivateKey(PRIV_KEY);
			if(privateKey == null) {
				return null;
			}
			decText = new String(RSAUtil.decryptWithPrivKey(cipherByte,privateKey),"UTF-8");
		} catch (InvalidKeyException e) {
			return null;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IllegalBlockSizeException e) {
			return null;
		} catch (BadPaddingException e) {
			return null;
		} catch (NoSuchAlgorithmException e) {
			return null;
		} catch (NoSuchPaddingException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
		if(decText == null || decText.length() == 0) {
			return null;
		}
		
		String[] text_arr = decText.split(":");
		
		long expire_time = Long.parseLong(text_arr[1].trim());
		long now = System.currentTimeMillis();
		
		device.setExpire_time(expire_time);
		device.setNow(now);
		device.setDevice_id(text_arr[2]);
		device.setModel(text_arr[3]);
		device.setOs(text_arr[4]);
		device.setOs_version(text_arr[5]);
		
		if( expire_time < now) {
			return device;
		}
		
		String tokenName = text_arr[0].trim();
		if(!tokenName.equals(TOKEN_NAME)) {
			return device;
		}
		return device;
	}
}

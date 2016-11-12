package com.nemustech.common.util;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encrypt / Decrypt Class Library
 *
 * <pre>
 * <code>
 * String sso = "후니는 OOO를 사랑해";
 * try {
 * 	AESEncrypter aesEncrypter = new AESEncrypter();
 * 	String encryptSSO = aesEncrypter.encrypt(sso);
 * 	String decryptSSO = aesEncrypter.decrypt(encryptSSO);
 * 	Toast.makeText(this, "[AESEncrypter]\n\n원문: " + sso + "\n\n암호: " + encryptSSO + "\n\n복호: " + decryptSSO,
 * 			Toast.LENGTH_SHORT).show();
 * } catch (Exception ex) {
 * 	Toast.makeText(this, "[AESEncrypter] Error\n\n" + ex, Toast.LENGTH_SHORT).show();
 * }
 * </code>
 * </pre>
 */
public class AESEncrypter {
	// ==========================================
	private final String TAG = "AESEncrypter";
	private Cipher rijndael;
	private SecretKeySpec key;
	private IvParameterSpec initalVector;
	private String specVectorKey = "POINTI.MOBILE.APPs-SERVICE";
	// ==========================================
	private boolean debugFlag = true; // true, false

	// ==========================================
	/**
	 * Creates a StringEncrypter instance.
	 */
	public AESEncrypter() // throws Exception
	{
		String specKey = "DONG-WHA.MOBILE";
		String spekVector = "hfXWOtDxdAIdvRRjgIzBlXD8XeCS+1qt+JvVlCy0dkxdQi0aNz4HDMAYLtx/Imwn";

		try {
			// Create a AES algorithm.
			this.rijndael = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// Initialize an encryption key and an initial vector.
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			this.key = new SecretKeySpec(md5.digest(specKey.getBytes("UTF8")), "AES");
			this.initalVector = new IvParameterSpec(md5.digest(spekVector.getBytes("UTF8")));
		} catch (Exception ex) {
			// appLog.e(debugFlag, TAG, ex.getMessage());
		}
	}

	// ==========================================
	/**
	 * Encrypts a string.
	 *
	 * @param value A string to encrypt. It is converted into UTF-8 before being encrypted. Null is regarded as an empty
	 *            string.
	 * @return An encrypted string.
	 */
	public String encrypt(String value) {
		if (value == null || value.trim().equals(""))
			return "";
		value = value.trim();

		try {
			// Initialize the cryptography algorithm.
			this.rijndael.init(Cipher.ENCRYPT_MODE, this.key, this.initalVector);

			// Get a UTF-8 byte array from a unicode string.
			byte[] utf8Value = value.getBytes("UTF8");

			// Encrypt the UTF-8 byte array.
			byte[] encryptedValue = this.rijndael.doFinal(utf8Value);

			// Return a base64 encoded string of the encrypted byte array.
			return Base64Encoder.encode(encryptedValue);
		} catch (Exception ex) {
			// appLog.e(debugFlag, TAG, ex.getMessage());
			return "";
		}
	}

	/**
	 * Encrypts a string.
	 */
	public String encrypt(String value, String key) {
		if (value == null || value.trim().equals(""))
			return "";
		value = value.trim();

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			SecretKeySpec vKey = new SecretKeySpec(md5.digest((this.specVectorKey + "" + key).getBytes("UTF8")), "AES");

			// Initialize the cryptography algorithm.
			this.rijndael.init(Cipher.ENCRYPT_MODE, vKey, this.initalVector);

			// Get a UTF-8 byte array from a unicode string.
			byte[] utf8Value = value.getBytes("UTF8");

			// Encrypt the UTF-8 byte array.
			byte[] encryptedValue = this.rijndael.doFinal(utf8Value);

			// Return a base64 encoded string of the encrypted byte array.
			return Base64Encoder.encode(encryptedValue);
		} catch (Exception ex) {
			// appLog.e(debugFlag, TAG, ex.getMessage());
			return "";
		}
	}

	/*
	 * public String encrypt(String value) throws Exception {
	 * if (value == null) value = "";
	 * 
	 * // Initialize the cryptography algorithm.
	 * this.rijndael.init(Cipher.ENCRYPT_MODE, this.key, this.initalVector);
	 * 
	 * // Get a UTF-8 byte array from a unicode string.
	 * byte[] utf8Value = value.getBytes("UTF8");
	 * 
	 * // Encrypt the UTF-8 byte array.
	 * byte[] encryptedValue = this.rijndael.doFinal(utf8Value);
	 * 
	 * // Return a base64 encoded string of the encrypted byte array.
	 * return Base64Encoder.encode(encryptedValue);
	 * }
	 */

	// ==========================================
	/**
	 * Decrypts a string which is encrypted with the same key and initial vector.
	 *
	 * @param value A string to decrypt. It must be a string encrypted with the same key and initial vector. Null or an
	 *            empty string is not allowed.
	 * @return A decrypted string
	 */
	public String decrypt(String value) {
		if (value == null || value.trim().equals(""))
			return "";
		value = value.trim();

		try {
			// Initialize the cryptography algorithm.
			this.rijndael.init(Cipher.DECRYPT_MODE, this.key, this.initalVector);

			// Get an encrypted byte array from a base64 encoded string.
			byte[] encryptedValue = Base64Encoder.decode(value);

			// Decrypt the byte array.
			byte[] decryptedValue = this.rijndael.doFinal(encryptedValue);

			// Return a string converted from the UTF-8 byte array.
			return new String(decryptedValue, "UTF8");
		} catch (Exception ex) {
			// appLog.e(debugFlag, TAG, ex.getMessage());
			return "";
		}
	}

	/**
	 * Decrypts string
	 */
	public String decrypt(String value, String key) {
		if (value == null || value.trim().equals(""))
			return "";
		value = value.trim();

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			SecretKeySpec vKey = new SecretKeySpec(md5.digest((this.specVectorKey + "" + key).getBytes("UTF8")), "AES");

			// Initialize the cryptography algorithm.
			this.rijndael.init(Cipher.DECRYPT_MODE, vKey, this.initalVector);

			// Get an encrypted byte array from a base64 encoded string.
			byte[] encryptedValue = Base64Encoder.decode(value);

			// Decrypt the byte array.
			byte[] decryptedValue = this.rijndael.doFinal(encryptedValue);

			// Return a string converted from the UTF-8 byte array.
			return new String(decryptedValue, "UTF8");
		} catch (Exception ex) {
			// appLog.e(debugFlag, TAG, ex.getMessage());
			return "";
		}
	}

	/*
	 * public String decrypt(String value) throws Exception {
	 * if (value == null || value.equals("")) throw new Exception("The cipher string can not be null or an empty string.");
	 * 
	 * // Initialize the cryptography algorithm. this.rijndael.
	 * init(Cipher.DECRYPT_MODE, this.key, this.initalVector);
	 * 
	 * // Get an encrypted byte array from a base64 encoded string.
	 * byte[] encryptedValue = Base64Encoder.decode(value);
	 * 
	 * // Decrypt the byte array.
	 * byte[] decryptedValue = this.rijndael.doFinal(encryptedValue);
	 * 
	 * // Return a string converted from the UTF-8 byte array.
	 * return new String(decryptedValue, "UTF8");
	 * }
	 */
}
package com.nemustech.platform.lbs.common.tokenizer;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

public class RSAUtil {
	
	private static final String ALGORITHM = "RSA";
	private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	
	private static SecureRandom sr = new SecureRandom();
	
	public static byte[] hexToBytes(String hex){
		return DatatypeConverter.parseHexBinary(hex);
	}
	public static String bytesTohex(byte[] bytes){
		return DatatypeConverter.printHexBinary(bytes);
	}

	public static KeyPair newKeyPair(int rsabits) throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
		generator.initialize(rsabits, sr);
		return generator.generateKeyPair();
	}

	public static byte[] pubKeyToBytes(PublicKey pubKey){
		return pubKey.getEncoded(); 
	}
	public static byte[] privKeyToBytes(PrivateKey priKey){
		return priKey.getEncoded();
	}

	public static PublicKey bytesToPubKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException{
		return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(bytes));
	}
	public static PrivateKey bytesToPrivKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException{
		return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(bytes));
	}

	public static byte[] encryptWithPubKey(byte[] input, PublicKey key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(input);
	}
	public static byte[] decryptWithPrivKey(byte[] input, PrivateKey key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(input);
	}

	public static String convertPublicKeyToString(PublicKey key) {
		byte[] publicKeyBytes = key.getEncoded();	
		byte[] encoded = Base64.encodeBase64(publicKeyBytes);
		String pubKeyStr = new String(encoded);
		return pubKeyStr;
	}

	public static PublicKey convertStringToPublicKey(String key) {
		byte[] publicKeyByte = null;
		PublicKey pubKey = null;
		try {
			publicKeyByte = Base64.decodeBase64(key.getBytes());
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyByte);
			KeyFactory keyFact = KeyFactory.getInstance("RSA");
			pubKey = keyFact.generatePublic(x509KeySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pubKey;
	}

	public static String convertPrivateKeyToString(PrivateKey key) {
		byte[] privateKeyBytes = key.getEncoded();
		byte[] encoded = Base64.encodeBase64(privateKeyBytes);
		String priKeyStr = new String(encoded);
		
		return priKeyStr;
	}

	public static PrivateKey convertStringToPrivateKey(String key) {
		byte[] privateKeyByte = null;
		PrivateKey privKey = null;
		try {
			privateKeyByte = Base64.decodeBase64(key.getBytes());
			PKCS8EncodedKeySpec privSpec=new PKCS8EncodedKeySpec(privateKeyByte);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			privKey = kf.generatePrivate(privSpec);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
		return privKey;
	}
}

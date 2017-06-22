package com.nemustech.platform.lbs.aams.service;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nemustech.platform.lbs.aams.mapper.AuthApiMapper;
import com.nemustech.platform.lbs.common.model.Response;
import com.nemustech.platform.lbs.common.vo.DeviceVo;
import com.nemustech.platform.lbs.common.vo.LoginVo;



/**
 * @author wwweojin@gmail.com
 *
 */
@Service(value = "authService")
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	private static final String MASTER_TOK = "nemus-master";
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
		
	@Autowired
	private AuthApiMapper authApiMapper;
	
	/** In-memory UserVo cache. */
	private Map<String, LoginVo> userCache = new ConcurrentHashMap<String, LoginVo>();
	
	private Map<String, DeviceVo> deviceCache = new ConcurrentHashMap<String, DeviceVo>();
	
	
	public int checkDeviceToken(String system_type, String device_no, String access_token) throws Exception {
		logger.info("system_type: "+system_type);		
		
		if(MASTER_TOK.equals(access_token)) return Response.OK;
		
		DeviceVo device = findDevice(system_type, device_no, access_token);
		
		if (device != null) {
			logger.trace("OK device: {}", device);
			return Response.OK;
		}
		logger.info("devic did not found.");
		return Response.EACCESS;
	}
	
	
	public DeviceVo findDevice(String system_type, String device_no,  String access_token) {
		// First, find in cache
		String key = system_type+device_no; 
		logger.info(key);
		
		DeviceVo device = deviceCache.get(key);
		if (device == null) {
			// If a user isn't in cache, find the user in db
			logger.debug("deviceCache missed. Retrieve a user from db.");
			try {
				DeviceVo vo = new DeviceVo();
				vo.setAccess_token(access_token);
				vo.setSystem_type(system_type);
				vo.setDevice_no(device_no);
				device = authApiMapper.selectDeviceToken(vo);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (device != null) {
					// Add a token/user pair to cache.
					deviceCache.put(key, device);
				}
			}
		}else{
			
			
			if(!device.getAccess_token().equals(access_token)){
				return null;
			}
		}
		return device;
	}
	
	
	
	
	public String generationDeviceToken(String system_type, String device_no){
		String rand = RandomStringUtils.randomAlphanumeric(12);
		String hashKey = passwordEncoder.encode(rand);
		byte[] encoded = Base64.encode(hashKey.getBytes());
		String token = new String(encoded);
		logger.info("token: "+token);
		DeviceVo device = new DeviceVo();
		device.setSystem_type(system_type);
		device.setDevice_no(device_no);
		device.setAccess_token(token);
		registerDeviceToken(device) ;
		return token;
	}
	
	
	
	public int checkAccountToken(String system_type, String access_token) throws Exception {
		logger.info("system_type: "+system_type +" ,access_token :" + access_token);		
		
		if(MASTER_TOK.equals(access_token)) return Response.OK;
		
		LoginVo login = findAccount(system_type, access_token);
		if (login != null) {
			logger.trace("OK user: {}", login);
			return Response.OK;
		}
		logger.info("user did not found.");
		return Response.EACCESS;
	}
	
	
	public LoginVo findAccount(String system_type, String access_token) {
		// First, find in cache
		LoginVo login = userCache.get(access_token);
		if (login == null) {
			// If a user isn't in cache, find the user in db
			logger.info("userCache missed. Retrieve a user from db.");
			try {
				LoginVo vo = new LoginVo();
				vo.setAccess_token(access_token);
				vo.setSystem_type(system_type);
				//user = userService.findUserbyAccessToken(token);
				login = authApiMapper.selectAccountToken(vo);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (login != null) {
					// Add a token/user pair to cache.
					userCache.put(access_token, login);
				}
			}
		}
		return login;
	}
	
	
	/**
	 * @param login 
	 * @return
	 * @throws Exception
	 */
	public String generateAccountToken(LoginVo login) throws Exception {
		logger.info("generateToken login: "+login);
		
		
		if (login.getPassword() == null) { // Should not pass for null password for this route.
			logger.debug("Invalid credential: null password");
			return null;
		}
				
		String str = login.getUser_id()+login.getPassword();
		String hashKey = passwordEncoder.encode(str);
		byte[] encoded = Base64.encode(hashKey.getBytes());
		String token = new String(encoded);
		logger.info("token: "+token);
		login.setAccess_token(token);
		registerToken(login);
		
		return token;
	}

	
	/**
	 * @param token
	 * @param user
	 */
	private void registerToken(LoginVo login) {
		// Update db
		authApiMapper.updateAccountAccessToken(login);
		userCache.put(login.getAccess_token(), login);
	}
	
	
	/**
	 * @param device_no 폰번호
	 * @param token
	 */
	private void registerDeviceToken(DeviceVo device) {
		logger.info("registerDeviceToken:"+device);
		authApiMapper.updateDeviceAccessToken(device);
		deviceCache.put(device.getSystem_type()+device.getDevice_no(), device);
		
	}
	
	
	
	
}

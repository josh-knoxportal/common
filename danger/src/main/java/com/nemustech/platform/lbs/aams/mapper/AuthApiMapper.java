package com.nemustech.platform.lbs.aams.mapper;


import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.common.vo.DeviceVo;
import com.nemustech.platform.lbs.common.vo.LoginVo;


@Repository(value = "AuthApiMapper")
public interface AuthApiMapper {
	public int updateAccountAccessToken(LoginVo login);	
	public int updateDeviceAccessToken(DeviceVo device);
	
	
	public DeviceVo selectDeviceToken(DeviceVo device);
	public LoginVo selectAccountToken(LoginVo login);
	
	
	
	

}

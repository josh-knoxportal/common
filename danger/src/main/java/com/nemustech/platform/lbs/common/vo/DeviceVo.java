// -*- Java -*-
/*  $Id: $  */
/*===========================================================================

	LoginVo.java

	$Author: sangho.lee $
	$URL: $
	$Revision: $
	$Date: $


	  Date: Oct 5, 2015 18:04:13
	Author: sangho.lee

===========================================================================*/
/*  $Source: $  */
/*  $Header: $  */
package com.nemustech.platform.lbs.common.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author wwweojin@gmail.com
 * @version r1 ($Id$)
 * @since r1
 */
public class DeviceVo {
	private String system_type;
	private String device_no;
	private String access_token;
	
	
	@ApiModelProperty(value ="access_token")
	@JsonProperty("access_token")	
	public String getAccess_token() {
		return access_token;
	}


	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	

	@ApiModelProperty(value ="device_no")
	@JsonProperty("device_no")	
	public String getDevice_no() {
		return device_no;
	}


	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value ="system_type")
	@JsonProperty("system_type")	
	public String getSystem_type() {
		return system_type;
	}


	public void setSystem_type(String system_type) {
		this.system_type = system_type;
	}
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class DeviceVo {");
		sb.append(" access_token: \"").append(access_token).append('"');
		sb.append(" device_no: \"").append(device_no).append('"');
		sb.append(" system_type: \"").append(system_type).append('"');
		
		sb.append("}\n");
		return sb.toString();
	}


	
}

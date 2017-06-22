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
public class GcmDataVo {
	private String action ;
	private String device;	
	private String message;
	
	
	
	
	@ApiModelProperty(value ="action")
	@JsonProperty("action")	
	public String getAction() {
		return action;
	}


	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}


	@ApiModelProperty(value ="device")
	@JsonProperty("device")	
	public String getDevice() {
		return device;
	}


	/**
	 * @param device the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
	}


	@ApiModelProperty(value ="message")
	@JsonProperty("message")	
	public String getMessage() {
		return message;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class "+this.getClass().getName()+" {");
		sb.append(" action: \"").append(action).append('"');
		sb.append(" device: \"").append(device).append('"');
		sb.append(" message: \"").append(message).append('"');
		
		sb.append("}\n");
		return sb.toString();
	}


	
}

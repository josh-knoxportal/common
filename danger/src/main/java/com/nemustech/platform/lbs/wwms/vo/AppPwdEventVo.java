package com.nemustech.platform.lbs.wwms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 **/
@ApiModel(description = "APP 패스워드 확인이벤트 등록 VO")
public class AppPwdEventVo {

	private String password;
	private String device_no;
	private String fcm_token;
	private String isenc;

	@ApiModelProperty(value = "password")
	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "fcm_token")
	@JsonProperty("fcm_token")
	public String getFcm_token() {
		return fcm_token;
	}

	public void setFcm_token(String fcm_token) {
		this.fcm_token = fcm_token;
	}

	@ApiModelProperty(value = "isenc")
	@JsonProperty("isenc")
	public String getIsenc() {
		return isenc;
	}

	public void setIsenc(String isenc) {
		this.isenc = isenc;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AppPwdEventVo {\n");
		sb.append(" ,device_no: ").append(device_no).append("\n");
		sb.append(" ,password: ").append(password).append("\n");
		sb.append(" ,fcm_token: ").append(fcm_token).append("\n");
		sb.append(" ,isenc: ").append(isenc).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

}

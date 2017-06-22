package com.nemustech.platform.lbs.ngms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 개인단말 등록
 */
@ApiModel(description = "개인단말 등록 VO")
public class AppPersonVehicleRegVo {

	private String device_uid;
	private String vehicle_uid;
	private String gcm_token;
	private String car;
	private String device_no;
	private int assigned;

	private String vehicle_type;
	private long time;

	@ApiModelProperty(value = "device_uid")
	@JsonProperty("device_uid")
	public String getDevice_uid() {
		return device_uid;
	}

	public void setDevice_uid(String device_uid) {
		this.device_uid = device_uid;
	}

	@ApiModelProperty(value = "vehicle_uid")
	@JsonProperty("vehicle_uid")
	public String getVehicle_uid() {
		return vehicle_uid;
	}

	public void setVehicle_uid(String vehicle_uid) {
		this.vehicle_uid = vehicle_uid;
	}

	@ApiModelProperty(value = "gcm_token")
	@JsonProperty("gcm_token")
	public String getGcm_token() {
		return gcm_token;
	}

	public void setGcm_token(String gcm_token) {
		this.gcm_token = gcm_token;
	}

	@ApiModelProperty(value = "car")
	@JsonProperty("car")
	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "assigned")
	@JsonProperty("assigned")
	public int getAssigned() {
		return assigned;
	}

	public void setAssigned(int assigned) {
		this.assigned = assigned;
	}

	@ApiModelProperty(value = "vehicle_type")
	@JsonProperty("vehicle_type")
	public String getVehicle_type() {
		return vehicle_type;
	}

	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

	@ApiModelProperty(value = "time")
	@JsonProperty("time")
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

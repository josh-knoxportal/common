package com.nemustech.platform.lbs.ngms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 개인 단말 차량 해제
 */
@ApiModel(description = "개인 단말 차량 해제 VO")
public class AppPersonDeiveVehicleRegVo {

	private String device_no;
	private String car;
	private long time;

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "car")
	@JsonProperty("car")
	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
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

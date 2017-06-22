package com.nemustech.platform.lbs.wwms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 작업미할당 출입 이벤트 정보
 */
@ApiModel(description = "작업미할당 출입 이벤트 VO")
public class AppUnAssignedEnterEventVo {

	private String worker_uid;
	private String device_no;
	private String type;
	private int zone_id;
	private int entered;
	private int restricted;
	private String position;
	private long time;

	@ApiModelProperty(value = "worker_uid")
	@JsonProperty("worker_uid")
	public String getWorker_uid() {
		return worker_uid;
	}

	public void setWorker_uid(String worker_uid) {
		this.worker_uid = worker_uid;
	}

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "type")
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ApiModelProperty(value = "time")
	@JsonProperty("time")
	public int getZone_id() {
		return zone_id;
	}

	public void setZone_id(int zone_id) {
		this.zone_id = zone_id;
	}

	@ApiModelProperty(value = "time")
	@JsonProperty("time")
	public int getEntered() {
		return entered;
	}

	public void setEntered(int entered) {
		this.entered = entered;
	}

	@ApiModelProperty(value = "time")
	@JsonProperty("time")
	public int getRestricted() {
		return restricted;
	}

	public void setRestricted(int restricted) {
		this.restricted = restricted;
	}

	@ApiModelProperty(value = "time")
	@JsonProperty("time")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
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

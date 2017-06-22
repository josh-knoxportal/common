package com.nemustech.platform.lbs.wwms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160725 [개인폰] [신규] Alive Report 정보
 */
@ApiModel(description = "Alive Report 보고 정보 VO")
public class AppAliveReportEventVo extends ResponseData {

	private String device_no;
	private String worker_uid;

	private double position_x;
	private double position_y;

	private int end_yn;
	private long time;

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "worker_uid")
	@JsonProperty("worker_uid")
	public String getWorker_uid() {
		return worker_uid;
	}

	public void setWorker_uid(String worker_uid) {
		this.worker_uid = worker_uid;
	}

	@ApiModelProperty(value = "position_x")
	@JsonProperty("position_x")
	public double getPosition_x() {
		return position_x;
	}

	public void setPosition_x(double position_x) {
		this.position_x = position_x;
	}

	@ApiModelProperty(value = "position_y")
	@JsonProperty("position_y")
	public double getPosition_y() {
		return position_y;
	}

	public void setPosition_y(double position_y) {
		this.position_y = position_y;
	}

	@ApiModelProperty(value = "end_yn")
	@JsonProperty("end_yn")
	public int getEnd_yn() {
		return end_yn;
	}

	public void setEnd_yn(int end_yn) {
		this.end_yn = end_yn;
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

package com.nemustech.platform.lbs.wwms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 작업자 등록 VO
 */
@ApiModel(description = "작업자 등록 VO")
public class AppWorkerRegEventVo {

	private String worker_uid;
	private String device_no;
	private String worker_name;
	private String worker_type;
	private String worker_organization_code;
	private String worker_organization_name;
	private String device_network_type;
	private long time;

	@ApiModelProperty(value = "worker_uid")
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

	@ApiModelProperty(value = "worker_name")
	@JsonProperty("worker_name")
	public String getWorker_name() {
		return worker_name;
	}

	public void setWorker_name(String worker_name) {
		this.worker_name = worker_name;
	}

	@ApiModelProperty(value = "worker_type")
	@JsonProperty("worker_type")
	public String getWorker_type() {
		return worker_type;
	}

	public void setWorker_type(String worker_type) {
		this.worker_type = worker_type;
	}

	@ApiModelProperty(value = "worker_organization_code")
	@JsonProperty("worker_organization_code")
	public String getWorker_organization_code() {
		return worker_organization_code;
	}

	public void setWorker_organization_code(String worker_organization_code) {
		this.worker_organization_code = worker_organization_code;
	}

	@ApiModelProperty(value = "worker_organization_name")
	@JsonProperty("worker_organization_name")
	public String getWorker_organization_name() {
		return worker_organization_name;
	}

	public void setWorker_organization_name(String worker_organization_name) {
		this.worker_organization_name = worker_organization_name;
	}

	@ApiModelProperty(value = "time")
	@JsonProperty("time")
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@ApiModelProperty(value = "device_network_type")
	@JsonProperty("device_network_type")
	public String getDevice_network_type() {
		return device_network_type;
	}

	public void setDevice_network_type(String device_network_type) {
		this.device_network_type = device_network_type;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

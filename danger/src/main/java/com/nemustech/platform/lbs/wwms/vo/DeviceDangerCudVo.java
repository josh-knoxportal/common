package com.nemustech.platform.lbs.wwms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "위험지역 단말 정보 VO")
public class DeviceDangerCudVo {

	private String device_uid;
	private String device_no;
	private String device_no_1;
	private String device_no_2;
	private String device_no_3;
	private String reg_device_no;
	private String factory_uid;
	private String account_uid;
	private String account_name;
	private String creator_id;
	private String reg_date;
	private String upd_date;
	private int is_used;
	private int is_assigned;
	private int status;
	private String network_type;
	private int is_company;
	private String worker_uid;
	private String last_server_access_time;

	@ApiModelProperty(value = "device_uid")
	@JsonProperty("device_uid")
	public String getDevice_uid() {
		return device_uid;
	}

	public void setDevice_uid(String device_uid) {
		this.device_uid = device_uid;
	}

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "factory_uid")
	@JsonProperty("factory_uid")
	public String getFactory_uid() {
		return factory_uid;
	}

	public void setFactory_uid(String factory_uid) {
		this.factory_uid = factory_uid;
	}

	@ApiModelProperty(value = "account_uid")
	@JsonProperty("account_uid")
	public String getAccount_uid() {
		return account_uid;
	}

	public void setAccount_uid(String account_uid) {
		this.account_uid = account_uid;
	}

	@ApiModelProperty(value = "reg_date")
	@JsonProperty("reg_date")
	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	@ApiModelProperty(value = "upd_date")
	@JsonProperty("upd_date")
	public String getUpd_date() {
		return upd_date;
	}

	public void setUpd_date(String upd_date) {
		this.upd_date = upd_date;
	}

	@ApiModelProperty(value = "account_name")
	@JsonProperty("account_name")
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	@ApiModelProperty(value = "device_no_1")
	@JsonProperty("device_no_1")
	public String getDevice_no_1() {
		return device_no_1;
	}

	public void setDevice_no_1(String device_no_1) {
		this.device_no_1 = device_no_1;
	}

	@ApiModelProperty(value = "device_no_2")
	@JsonProperty("device_no_2")
	public String getDevice_no_2() {
		return device_no_2;
	}

	public void setDevice_no_2(String device_no_2) {
		this.device_no_2 = device_no_2;
	}

	@ApiModelProperty(value = "device_no_3")
	@JsonProperty("device_no_3")
	public String getDevice_no_3() {
		return device_no_3;
	}

	public void setDevice_no_3(String device_no_3) {
		this.device_no_3 = device_no_3;
	}

	@ApiModelProperty(value = "creator_id")
	@JsonProperty("creator_id")
	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}

	@ApiModelProperty(value = "reg_device_no")
	@JsonProperty("reg_device_no")
	public String getReg_device_no() {
		return reg_device_no;
	}

	public void setReg_device_no(String reg_device_no) {
		this.reg_device_no = reg_device_no;
	}

	@ApiModelProperty(value = "is_used")
	@JsonProperty("is_used")
	public int getIs_used() {
		return is_used;
	}

	public void setIs_used(int is_used) {
		this.is_used = is_used;
	}

	@ApiModelProperty(value = "status")
	@JsonProperty("status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@ApiModelProperty(value = "is_assigned")
	@JsonProperty("is_assigned")
	public int getIs_assigned() {
		return is_assigned;
	}

	public void setIs_assigned(int is_assigned) {
		this.is_assigned = is_assigned;
	}

	@ApiModelProperty(value = "network_type")
	@JsonProperty("network_type")
	public String getNetwork_type() {
		return network_type;
	}

	public void setNetwork_type(String network_type) {
		this.network_type = network_type;
	}

	@ApiModelProperty(value = "is_company")
	@JsonProperty("is_company")
	public int getIs_company() {
		return is_company;
	}

	public void setIs_company(int is_company) {
		this.is_company = is_company;
	}

	@ApiModelProperty(value = "worker_uid")
	@JsonProperty("worker_uid")
	public String getWorker_uid() {
		return worker_uid;
	}

	public void setWorker_uid(String worker_uid) {
		this.worker_uid = worker_uid;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@ApiModelProperty(value = "last_server_access_time")
	@JsonProperty("last_server_access_time")
	public String getLast_server_access_time() {
		return last_server_access_time;
	}

	public void setLast_server_access_time(String last_server_access_time) {
		this.last_server_access_time = last_server_access_time;
	}
}

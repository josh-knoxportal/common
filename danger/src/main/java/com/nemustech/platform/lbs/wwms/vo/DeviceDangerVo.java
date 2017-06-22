package com.nemustech.platform.lbs.wwms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "위험지역 단말 정보 VO")
public class DeviceDangerVo {

	private String device_uid;
	private String device_no;
	private String factory_uid;
	private String account_uid;
	private int is_used;
	private int status;
	private String upd_date;
	private String factory_name;
	private String account_name;
	private String is_used_value;
	private String is_assigned_name;
	private String access_token;
	private String last_work_uid;
	private String network_type;
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

	// private String zone_id; // 160810 [개인폰] [추가] [출입감지 01] 단말기 정보조회 허용되는 존 항목
	@ApiModelProperty(value = "zone_id")
	@JsonProperty("zone_id")
	public String getZone_id() {
		return factory_uid;
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

	@ApiModelProperty(value = "is_used")
	@JsonProperty("is_used")
	public int getIs_used() {
		return is_used;
	}

	public void setIs_used(int is_used) {
		this.is_used = is_used;
	}

	@ApiModelProperty(value = "upd_date")
	@JsonProperty("upd_date")
	public String getUpd_date() {
		return upd_date;
	}

	public void setUpd_date(String upd_date) {
		this.upd_date = upd_date;
	}

	@ApiModelProperty(value = "factory_name")
	@JsonProperty("factory_name")
	public String getFactory_name() {
		return factory_name;
	}

	public void setFactory_name(String factory_name) {
		this.factory_name = factory_name;
	}

	@ApiModelProperty(value = "account_name")
	@JsonProperty("account_name")
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	@ApiModelProperty(value = "is_used_value")
	@JsonProperty("is_used_value")
	public String getIs_used_value() {
		return is_used_value;
	}

	public void setIs_used_value(String is_used_value) {
		this.is_used_value = is_used_value;
	}

	@ApiModelProperty(value = "is_assigned_name")
	@JsonProperty("is_assigned_name")
	public String getIs_assigned_name() {
		return is_assigned_name;
	}

	public void setIs_assigned_name(String is_assigned_name) {
		this.is_assigned_name = is_assigned_name;
	}

	@ApiModelProperty(value = "access_token")
	@JsonProperty("access_token")
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	@ApiModelProperty(value = "status")
	@JsonProperty("status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@ApiModelProperty(value = "last_work_uid")
	@JsonProperty("last_work_uid")
	public String getLast_work_uid() {
		return last_work_uid;
	}

	public void setLast_work_uid(String last_work_uid) {
		this.last_work_uid = last_work_uid;
	}

	@ApiModelProperty(value = "network_type")
	@JsonProperty("network_type")
	public String getNetwork_type() {
		return network_type;
	}

	public void setNetwork_type(String network_type) {
		this.network_type = network_type;
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

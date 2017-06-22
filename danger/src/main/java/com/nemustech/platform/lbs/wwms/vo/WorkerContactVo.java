package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "위험지역에 있는 작업자 연락처 VO")
public class WorkerContactVo  {
	private String company = "";
	private String name = "";
	private String device_no = "";
	private String push_id = "";
	private int    is_authorized = 1;
	
	
	@ApiModelProperty(value ="company")
	@JsonProperty("company")
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	@ApiModelProperty(value ="name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
		
	@ApiModelProperty(value ="device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}
	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}
	
	@ApiModelProperty(value ="is_authorized")
	@JsonProperty("is_authorized")
	public int getIs_authorized() {
		return is_authorized;
	}
	public void setIs_authorized(int is_authorized) {
		this.is_authorized = is_authorized;
	}
	
	
	@ApiModelProperty(value ="push_id")
	@JsonProperty("push_id")
	public String getPush_id() {
		return push_id;
	}
	public void setPush_id(String push_id) {
		this.push_id = push_id;
	}
}

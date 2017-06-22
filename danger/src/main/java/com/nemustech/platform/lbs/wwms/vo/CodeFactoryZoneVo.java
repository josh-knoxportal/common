package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "단위공자 및 ZONE 목록 VO")
public class CodeFactoryZoneVo  {
	private String type = "";
	private String uid = "";
	private String name = "";
	private int	   is_restricted = 0;
		
	@ApiModelProperty(value ="type")
	@JsonProperty("type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	@ApiModelProperty(value ="uid")
	@JsonProperty("uid")
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@ApiModelProperty(value ="name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	@ApiModelProperty(value ="is_restricted")
	@JsonProperty("is_restricted")
	public int getIs_restricted() {
		return is_restricted;
	}
	public void setIs_restricted(int is_restricted) {
		this.is_restricted = is_restricted;
	}
	
	
}

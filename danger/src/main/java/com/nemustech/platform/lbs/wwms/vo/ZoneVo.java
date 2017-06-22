package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "비콘 존 정보 VO")
public class ZoneVo  {	
	private String  zone_uid;
	private String  factory_uid;
	private String 	name;
	private String  is_restricted;
	
	@ApiModelProperty(value ="zone_uid")
	@JsonProperty("zone_uid")
	public String getZone_uid() {
		return zone_uid;
	}
	public void setZone_uid(String zone_uid) {
		this.zone_uid = zone_uid;
	}
	
	@ApiModelProperty(value ="factory_uid")
	@JsonProperty("factory_uid")
	public String getFactory_uid() {
		return factory_uid;
	}
	public void setFactory_uid(String factory_uid) {
		this.factory_uid = factory_uid;
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
	public String getIs_restricted() {
		return is_restricted;
	}
	public void setIs_restricted(String is_restricted) {
		this.is_restricted = is_restricted;
	}
}

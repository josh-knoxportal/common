package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "비콘 정보 VO")
public class BeaconVo  {	
	private String  beacon_id;
	private String  beacon_position;
	private String  beacon_name;
	
	
	@ApiModelProperty(value ="beacon_id")
	@JsonProperty("beacon_id")
	public String getBeacon_id() {
		return beacon_id;
	}
	public void setBeacon_id(String beacon_id) {
		this.beacon_id = beacon_id;
	}
	
	@ApiModelProperty(value ="beacon_position")
	@JsonProperty("beacon_position")
	public String getBeacon_position() {
		return beacon_position;
	}
	public void setBeacon_position(String beacon_position) {
		this.beacon_position = beacon_position;
	}
	
	@ApiModelProperty(value ="beacon_name")
	@JsonProperty("beacon_name")
	public String getBeacon_name() {
		return beacon_name;
	}
	public void setBeacon_name(String beacon_name) {
		this.beacon_name = beacon_name;
	}
}

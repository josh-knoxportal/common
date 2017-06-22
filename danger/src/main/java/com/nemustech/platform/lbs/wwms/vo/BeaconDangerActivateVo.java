package com.nemustech.platform.lbs.wwms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 위험지역 비콘관리 비콘활성 정보 VO
 **/
@ApiModel(description = "위험지역 비콘관리 비콘활성 정보 VO")
public class BeaconDangerActivateVo {

	private int total_count;
	private int not_activated;
	private int activated;

	@ApiModelProperty(value = "total_count")
	@JsonProperty("total_count")
	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	@ApiModelProperty(value = "not_activated")
	@JsonProperty("not_activated")
	public int getNot_activated() {
		return not_activated;
	}

	public void setNot_activated(int not_activated) {
		this.not_activated = not_activated;
	}

	@ApiModelProperty(value = "activated")
	@JsonProperty("activated")
	public int getActivated() {
		return activated;
	}

	public void setActivated(int activated) {
		this.activated = activated;
	}

}

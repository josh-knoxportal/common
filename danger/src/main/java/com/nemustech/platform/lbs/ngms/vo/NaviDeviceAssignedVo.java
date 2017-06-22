package com.nemustech.platform.lbs.ngms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 네이게이션 단말 할당 개수 정보 VO
 **/
@ApiModel(description = "네이게이션 단말 할당 개수 정보  VO")
public class NaviDeviceAssignedVo {

	private int device_total_count;
	private int not_assigned;
	private int assigned;

	@ApiModelProperty(value = "device_total_count")
	@JsonProperty("device_total_count")
	public int getDevice_total_count() {
		return device_total_count;
	}

	public void setDevice_total_count(int device_total_count) {
		this.device_total_count = device_total_count;
	}

	@ApiModelProperty(value = "not_assigned")
	@JsonProperty("not_assigned")
	public int getNot_assigned() {
		return not_assigned;
	}

	public void setNot_assigned(int not_assigned) {
		this.not_assigned = not_assigned;
	}

	@ApiModelProperty(value = "assigned")
	@JsonProperty("assigned")
	public int getAssigned() {
		return assigned;
	}

	public void setAssigned(int assigned) {
		this.assigned = assigned;
	}

}

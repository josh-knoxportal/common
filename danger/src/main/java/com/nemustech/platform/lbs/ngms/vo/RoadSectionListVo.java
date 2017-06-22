package com.nemustech.platform.lbs.ngms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 **/
@ApiModel(description = "Road Section List VO")
public class RoadSectionListVo {
	/* default */
	private String vehicle_type = "";

	

	@ApiModelProperty(value = "")
	@JsonProperty("vehicle_type")
	public String getVehicle_type() {
		return vehicle_type;
	}

	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class RoadSectionListVo {\n");
		sb.append("  vehicle_type: ").append(vehicle_type).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}

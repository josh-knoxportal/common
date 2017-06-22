package com.nemustech.platform.lbs.ngms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 네이게이션맵 정보
 **/
@ApiModel(description = "차량등록/해제 정보")
public class NaviVehicle extends ResponseData {

	private String vehicle_uid;

	@ApiModelProperty(value = "vehicle_uid")
	@JsonProperty("vehicle_uid")
	public String getVehicle_uid() {
		return vehicle_uid;
	}

	/**
	 * @param vehicle_uid
	 *            the vehicle_uid to set
	 */
	public void setVehicle_uid(String vehicle_uid) {
		this.vehicle_uid = vehicle_uid;
	}

	// test
	// test

}

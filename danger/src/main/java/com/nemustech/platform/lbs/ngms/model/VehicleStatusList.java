package com.nemustech.platform.lbs.ngms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.VehicleStatusVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 차량상태정보 목록
 **/
@ApiModel(description = "차량상태정보 목록")
public class VehicleStatusList extends ResponseData{

	private List<VehicleStatusVo> vehicleStatusList;
	
	 /**
	   * 차량상태정보 정보
	   **/
	@ApiModelProperty(value = "차량등록정보 목록")
	@JsonProperty("vehicleStatusList")
	public List<VehicleStatusVo> getVehicleStatusList() {
		return vehicleStatusList;
	}

	public void setVehicleStatusList(List<VehicleStatusVo> vehicleStatusList) {
		this.vehicleStatusList = vehicleStatusList;
	}
}

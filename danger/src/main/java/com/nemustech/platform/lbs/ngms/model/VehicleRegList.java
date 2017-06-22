package com.nemustech.platform.lbs.ngms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.VehicleRegVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 차량등록정보 목록
 **/
@ApiModel(description = "차량등록정보 목록")
public class VehicleRegList extends ResponseData{

	private List<VehicleRegVo> vehicleRegList;
	private int totalCnt  = 0;
	
	 /**
	   * 차량등록정보 정보
	   **/
	@ApiModelProperty(value = "차량등록정보 목록")
	@JsonProperty("vehicleRegList")
	public List<VehicleRegVo> getVehicleRegList() {
		return vehicleRegList;
	}

	public void setVehicleRegList(List<VehicleRegVo> vehicleRegList) {
		this.vehicleRegList = vehicleRegList;
	}
	
	@ApiModelProperty(value = "차량등록정보 Total Count")
	@JsonProperty("totalCnt")
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	

	
	

	



}

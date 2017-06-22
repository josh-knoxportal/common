package com.nemustech.platform.lbs.ngms.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleTypeVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 차량유형 List
 */
@ApiModel(description = "차량유형 List VO")
public class VehicleTypeList extends ResponseData {

	private List<CodeVehicleTypeVo> vehicleTypeList;
	private int totalCnt = 0;

	@ApiModelProperty(value = "vehicleTypeList")
	@JsonProperty("vehicleTypeList")
	public List<CodeVehicleTypeVo> getVehicleTypeList() {
		return vehicleTypeList;
	}

	public void setVehicleTypeList(List<CodeVehicleTypeVo> vehicleTypeList) {
		this.vehicleTypeList = vehicleTypeList;
	}

	@ApiModelProperty(value = "totalCnt")
	@JsonProperty("totalCnt")
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

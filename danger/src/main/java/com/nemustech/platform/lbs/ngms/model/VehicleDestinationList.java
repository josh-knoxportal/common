package com.nemustech.platform.lbs.ngms.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleDestinationVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 목적지 정보 List
 */
@ApiModel(description = "목적지 정보 List VO")
public class VehicleDestinationList extends ResponseData {
	private List<CodeVehicleDestinationVo> vehicleDestinationVo;
	private int totalCnt = 0;

	@ApiModelProperty(value = "vehicleDestinationVo")
	@JsonProperty("vehicleDestinationVo")
	public List<CodeVehicleDestinationVo> getVehicleDestinationVo() {
		return vehicleDestinationVo;
	}

	public void setVehicleDestinationVo(List<CodeVehicleDestinationVo> vehicleDestinationVo) {
		this.vehicleDestinationVo = vehicleDestinationVo;
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

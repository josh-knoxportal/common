package com.nemustech.platform.lbs.ngms.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleDestinationTypeVo;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleDestinationVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 차량 목적지 정보
 */
@ApiModel(description = "차량 목적지 정보 목록")
public class CodeDestinationList extends ResponseData {
	private List<CodeVehicleDestinationTypeVo> destinationTypeList;
	private List<CodeVehicleDestinationVo> destinationList;
	private int totalCnt = 0;

	@ApiModelProperty(value = "차량 목적지 정보 목록")
	@JsonProperty("destinationList")
	public List<CodeVehicleDestinationVo> getDestinationList() {
		return destinationList;
	}

	public void setDestinationList(List<CodeVehicleDestinationVo> destinationList) {
		this.destinationList = destinationList;
	}

	@ApiModelProperty(value = "차량 목적지 정보 목록 총개수")
	@JsonProperty("totalCnt")
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	@ApiModelProperty(value = "차량 목적지 타입 정보")
	@JsonProperty("destinationTypeList")
	public List<CodeVehicleDestinationTypeVo> getDestinationTypeList() {
		return destinationTypeList;
	}

	public void setDestinationTypeList(List<CodeVehicleDestinationTypeVo> destinationTypeList) {
		this.destinationTypeList = destinationTypeList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

package com.nemustech.platform.lbs.wwms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.FactoryCoordVo;
import com.nemustech.platform.lbs.wwms.vo.FactoryVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 비콘 존 및 좌표계 목록
 **/
@ApiModel(description = "단위공장 및 좌표계 목록")
public class FactoryCoordList extends ResponseData {
	
	private List<FactoryVo> 	 factoryList;
	private List<FactoryCoordVo> factoryCoordList;

	@ApiModelProperty(value = "factoryList")
	@JsonProperty("factoryList")
	public List<FactoryVo> getFactoryList() {
		return factoryList;
	}
	public void setFactoryList(List<FactoryVo> factoryList) {
		this.factoryList = factoryList;
	}
	
	
	@ApiModelProperty(value = "factoryCoordList")
	@JsonProperty("factoryCoordList")
	public List<FactoryCoordVo> getFactoryCoordList() {
		return factoryCoordList;
	}
	public void setFactoryCoordList(List<FactoryCoordVo> factoryCoordList) {
		this.factoryCoordList = factoryCoordList;
	}
	
}
package com.nemustech.platform.lbs.wwms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.CodeFactoryZoneVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 공통코드 : 단위공자 및 ZONE 목록
 **/
@ApiModel(description = "단위공자 및 ZONE 목록")
public class CodeFactoryZoneList extends ResponseData {

	private List<CodeFactoryZoneVo> codeFactoryZoneList;
	private int totalCnt = 0;


	@ApiModelProperty(value = "단위공자 및 ZONE 목록")
	@JsonProperty("codeFactoryZoneList")
	public List<CodeFactoryZoneVo> getCodeFactoryZoneList() {
		return codeFactoryZoneList;
	}

	public void setCodeFactoryZoneList(List<CodeFactoryZoneVo> codeFactoryZoneList) {
		this.codeFactoryZoneList = codeFactoryZoneList;
	}

	@ApiModelProperty(value = "단위공자 및 ZONE 목록 Total Count")
	@JsonProperty("totalCnt")
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
}
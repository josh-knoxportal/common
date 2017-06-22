package com.nemustech.platform.lbs.wwms.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.ParterNameVo;

/**
 * 공통코드 : 부서/업체명
 **/
@ApiModel(description = "부서/업체명 목록")
public class ParterNameList extends ResponseData {

	private List<ParterNameVo> parternameList;

	@ApiModelProperty(value = "서/업체명 목록")
	@JsonProperty("parterNameList")
	public List<ParterNameVo> getParternameList() {
		return parternameList;
	}

	public void setParternameList(List<ParterNameVo> parternameList) {
		this.parternameList = parternameList;
	}

}

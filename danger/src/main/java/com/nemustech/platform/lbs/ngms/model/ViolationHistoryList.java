package com.nemustech.platform.lbs.ngms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.ViolationHistoryVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "")
public class ViolationHistoryList extends ResponseData{

	private List<ViolationHistoryVo> list;
	
	 /**
	   *
	   **/
	@ApiModelProperty(value = "")
	@JsonProperty("violationHistoryList")
	public List<ViolationHistoryVo> getViolationHistoryList() {
		return list;
	}

	public void setViolationHistoryList(List<ViolationHistoryVo> list) {
		this.list = list;
	}
}

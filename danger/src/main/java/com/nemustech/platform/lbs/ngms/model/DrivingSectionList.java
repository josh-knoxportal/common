package com.nemustech.platform.lbs.ngms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.DrivingSectionVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "")
public class DrivingSectionList extends ResponseData{
	private List<DrivingSectionVo> list;
	
	 /**
	   *
	   **/
	@ApiModelProperty(value = "")
	@JsonProperty("drivingSectionList")
	public List<DrivingSectionVo> getDrivingSectionList() {
		return list;
	}

	public void setDrivingSectionList(List<DrivingSectionVo> list) {
		this.list = list;
	}
}

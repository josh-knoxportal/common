package com.nemustech.platform.lbs.ngms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.DrivingEventVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "")
public class DrivingEventList extends ResponseData{
	private List<DrivingEventVo> list;
	
	 /**
	   *
	   **/
	@ApiModelProperty(value = "")
	@JsonProperty("drivingEventList")
	public List<DrivingEventVo> getDrivingEventList() {
		return list;
	}

	public void setDrivingEventList(List<DrivingEventVo> list) {
		this.list = list;
	}
}

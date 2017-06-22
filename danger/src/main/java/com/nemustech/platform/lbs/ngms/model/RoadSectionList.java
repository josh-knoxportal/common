package com.nemustech.platform.lbs.ngms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.RoadSectionVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "")
public class RoadSectionList extends ResponseData{
	private List<RoadSectionVo> list;
	
	 /**
	   *
	   **/
	@ApiModelProperty(value = "")
	@JsonProperty("roadSectionList")
	public List<RoadSectionVo> getRoadSectionList() {
		return list;
	}

	public void setRoadSectionList(List<RoadSectionVo> list) {
		this.list = list;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("count")
	public int getCount(){
		if(list != null)
			return list.size();
		else
			return 0;
	}
}

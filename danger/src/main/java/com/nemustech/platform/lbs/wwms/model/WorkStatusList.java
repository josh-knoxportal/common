package com.nemustech.platform.lbs.wwms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.WorkStatusVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 작업자별 현황 목록
 **/
@ApiModel(description = "작업자별 현황 목록")
public class WorkStatusList extends ResponseData{

	private List<WorkStatusVo> workStatusList;
	private int totalcnt;
	
	 /**
	   * 작업자별 현황 정보
	   **/
	@ApiModelProperty(value = "작업자별 현황 목록")
	@JsonProperty("workStatusList")
	public List<WorkStatusVo> getWorkStatusList() {
		return workStatusList;
	}

	public void setWorkStatusList(List<WorkStatusVo> workStatusList) {
		this.workStatusList = workStatusList;
	}

	public int getTotalcnt() {
		return totalcnt;
	}

	public void setTotalcnt(int totalcnt) {
		this.totalcnt = totalcnt;
	}
	
}

package com.nemustech.platform.lbs.wwms.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.WorkMntVo;

/**
 * 작업 목록
 **/
@ApiModel(description = "작업 목록")
public class WorkMntList extends ResponseData {
	private List<WorkMntVo> workMntList;
	private int totalcnt;

	/**
	 * 작업자 목록 정보
	 **/
	@ApiModelProperty(value = "작업 목록")
	@JsonProperty("workMntList")
	public List<WorkMntVo> getWorkMntList() {
		return workMntList;
	}

	public void setWorkMntList(List<WorkMntVo> workMntList) {
		this.workMntList = workMntList;
	}

	public int getTotalcnt() {
		return totalcnt;
	}

	public void setTotalcnt(int totalcnt) {
		this.totalcnt = totalcnt;
	}

}

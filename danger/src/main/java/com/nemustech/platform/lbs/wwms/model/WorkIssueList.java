package com.nemustech.platform.lbs.wwms.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.WorkIssueVo;

/**
 * 출입감지 현황 목록
 **/
@ApiModel(description = "출입감지 현황 목록")
public class WorkIssueList extends ResponseData {

	private List<WorkIssueVo> workIssueList;
	private int totalcnt;

	/**
	 * 출입감지 현황 정보
	 **/
	@ApiModelProperty(value = "출입감지 현황 목록")
	@JsonProperty("workIssueList")
	public List<WorkIssueVo> getWorkIssueList() {
		return workIssueList;
	}

	public void setWorkIssueList(List<WorkIssueVo> workIssueList) {
		this.workIssueList = workIssueList;
	}

	public int getTotalcnt() {
		return totalcnt;
	}

	public void setTotalcnt(int totalcnt) {
		this.totalcnt = totalcnt;
	}

}

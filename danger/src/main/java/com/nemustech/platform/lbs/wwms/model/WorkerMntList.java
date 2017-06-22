package com.nemustech.platform.lbs.wwms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.WorkerMntVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 작업자  목록
 **/
@ApiModel(description = "작업자 목록")
public class WorkerMntList extends ResponseData{

	private List<WorkerMntVo> workerMntList;
	private int totalcnt;
	 /**
	   * 작업자 목록 정보
	   **/
	@ApiModelProperty(value = "작업자 목록")
	@JsonProperty("workerMntList")
	public List<WorkerMntVo> getWorkerMntList() {
		return workerMntList;
	}

	public void setWorkerMntList(List<WorkerMntVo> workerMntList) {
		this.workerMntList = workerMntList;
	}

	public int getTotalcnt() {
		return totalcnt;
	}

	public void setTotalcnt(int totalcnt) {
		this.totalcnt = totalcnt;
	}
	
}

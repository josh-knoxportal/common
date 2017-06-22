package com.nemustech.platform.lbs.wwms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.WorkerContactVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 위험지역내 작업자 연락처 목록
 **/
@ApiModel(description = "위험지역내 작업자 연락처 목록")
public class WorkerContactList extends ResponseData{

	private List<WorkerContactVo> workerContactList;
	
	 /**
	   * 위험지역내 작업자 연락처
	   **/
	@ApiModelProperty(value = "위험지역내 작업자 연락처 목록")
	@JsonProperty("workerContactList")
	public List<WorkerContactVo> getWorkerContactList() {
		return workerContactList;
	}

	public void setWorkerContactList(List<WorkerContactVo> workerContactList) {
		this.workerContactList = workerContactList;
	}
}

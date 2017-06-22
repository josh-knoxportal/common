package com.nemustech.platform.lbs.wwms.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.WorkVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 작업자 갱신 결과
 **/
@ApiModel(description = "작업자 갱신 결과")
public class WorkCud extends ResponseData{

	private WorkVo workCud;
	
	 /**
	   * 작업자 갱신 결과
	   **/
	@ApiModelProperty(value = "작업자 갱신 결과")
	@JsonProperty("workCud")
	public WorkVo getWorkCud() {
		return workCud;
	}

	public void setWorkCud(WorkVo workCud) {
		this.workCud = workCud;
	}
}

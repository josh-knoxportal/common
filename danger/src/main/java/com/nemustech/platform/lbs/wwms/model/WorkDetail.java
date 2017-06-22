package com.nemustech.platform.lbs.wwms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.WorkMntVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "작업 상세 정보")
public class WorkDetail extends ResponseData {
	private WorkMntVo workMntVo;

	@ApiModelProperty(value = "작업 상세 정보")
	@JsonProperty("workMntVo")
	public WorkMntVo getWorkMntVo() {
		return workMntVo;
	}

	public void setWorkMntVo(WorkMntVo workMntVo) {
		this.workMntVo = workMntVo;
	}

}

package com.nemustech.platform.lbs.wwms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.CodeWorkTypeVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 공통코드 : 작업유형 코드
 **/
@ApiModel(description = "작업유형 코드 목록")
public class CodeWorkTypeList extends ResponseData {

	private List<CodeWorkTypeVo> codeWorkTypeList;
	private int totalCnt = 0;


	@ApiModelProperty(value = "작업유형 코드 목록")
	@JsonProperty("codeWorkTypeList")
	public List<CodeWorkTypeVo> getCodeWorkTypeList() {
		return codeWorkTypeList;
	}

	public void setCodeWorkTypeList(List<CodeWorkTypeVo> codeWorkTypeList) {
		this.codeWorkTypeList = codeWorkTypeList;
	}

	@ApiModelProperty(value = "작업유형 코드 목록 Total Count")
	@JsonProperty("totalCnt")
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
}
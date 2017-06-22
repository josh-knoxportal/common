package com.nemustech.platform.lbs.wwms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.CodeWorkNoVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 공통코드 : 작업유형 코드
 **/
@ApiModel(description = "작업유형 코드 목록")
public class CodeWorkNoList extends ResponseData {

	private List<CodeWorkNoVo> codeWorkNoList;
	private int totalCnt = 0;




	@ApiModelProperty(value = "작업번호 코드 목록 Total Count")
	@JsonProperty("totalCnt")
	public int getTotalCnt() {
		if(codeWorkNoList != null)
			totalCnt = codeWorkNoList.size();
		
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	@ApiModelProperty(value = "작업번호 목록 Total Count")
	@JsonProperty("codeWorkNoList")
	public List<CodeWorkNoVo> getCodeWorkNoList() {
		return codeWorkNoList;
	}

	public void setCodeWorkNoList(List<CodeWorkNoVo> codeWorkNoList) {
		this.codeWorkNoList = codeWorkNoList;
	}
}
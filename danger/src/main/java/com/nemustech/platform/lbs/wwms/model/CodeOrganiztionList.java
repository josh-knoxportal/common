package com.nemustech.platform.lbs.wwms.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.CodeOrganizationVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 부서/협력업체 정보
 */
@ApiModel(description = "부서/협력업체 정보 목록")
public class CodeOrganiztionList extends ResponseData {
	private List<CodeOrganizationVo> organization;
	private int totalCnt = 0;

	@ApiModelProperty(value = "부서/협력업체 정보 목록")
	@JsonProperty("organization")
	public List<CodeOrganizationVo> getOrganization() {
		return organization;
	}

	public void setOrganization(List<CodeOrganizationVo> organization) {
		this.organization = organization;
	}

	@ApiModelProperty(value = "부서/협력업체 정보 목록 총개수")
	@JsonProperty("totalCnt")
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

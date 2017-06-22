package com.nemustech.platform.lbs.web.vehicle.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 계정목록정보 목록
 **/
@ApiModel(description = "계정목록정보 목록")
public class AdminAccountList extends ResponseData {

	private List<AdminAccountVo> adminAccountList;
	private int totalCnt = 0;

	@ApiModelProperty(value = "계정목록정보 목록")
	@JsonProperty("adminAccountList")
	public List<AdminAccountVo> getAdminAccountList() {
		return adminAccountList;
	}

	public void setAdminAccountList(List<AdminAccountVo> adminAccountList) {
		this.adminAccountList = adminAccountList;
	}

	@ApiModelProperty(value = "계정목록정보 Total Count")
	@JsonProperty("totalCnt")
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

}

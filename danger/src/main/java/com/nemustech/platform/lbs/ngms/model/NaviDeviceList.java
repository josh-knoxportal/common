package com.nemustech.platform.lbs.ngms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceAssignedVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 네비게이션 단말기 정보 목록
 **/
@ApiModel(description = "네비게이션 단말기 정보 목록")
public class NaviDeviceList extends ResponseData {

	private List<NaviDeviceVo> naviDeiviceList;
	private int totalCnt = 0;
	private NaviDeviceAssignedVo naviDeviceAssigned;

	@ApiModelProperty(value = "네비게이션 단말기 정보 목록")
	@JsonProperty("naviDeiviceList")
	public List<NaviDeviceVo> getNaviDeiviceList() {
		return naviDeiviceList;
	}

	public void setNaviDeiviceList(List<NaviDeviceVo> naviDeiviceList) {
		this.naviDeiviceList = naviDeiviceList;
	}

	@ApiModelProperty(value = "네비게이션 단말기 정보 목록 Total Count")
	@JsonProperty("totalCnt")
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	@ApiModelProperty(value = "단말 할당 개수 정보 VO")
	@JsonProperty("naviDeviceAssigned")
	public NaviDeviceAssignedVo getNaviDeviceAssigned() {
		return naviDeviceAssigned;
	}

	public void setNaviDeviceAssigned(NaviDeviceAssignedVo naviDeviceAssigned) {
		this.naviDeviceAssigned = naviDeviceAssigned;
	}

}
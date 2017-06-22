package com.nemustech.platform.lbs.wwms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "위험지역 단말관리 목록")
public class DeviceDangerList extends ResponseData {

	private List<DeviceDangerVo> deviceDangerList;
	private int totalCnt = 0;

	@ApiModelProperty(value = "위험지역 단말관리 목록")
	@JsonProperty("deviceDangerList")
	public List<DeviceDangerVo> getDeviceDangerList() {
		return deviceDangerList;
	}

	public void setDeviceDangerList(List<DeviceDangerVo> deviceDangerList) {
		this.deviceDangerList = deviceDangerList;
	}

	@ApiModelProperty(value = "위험지역 단말관리 목록 Total Count")
	@JsonProperty("totalCnt")
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
}

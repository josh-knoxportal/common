package com.nemustech.platform.lbs.ngms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 네비게이션 단말기
 **/
@ApiModel(description = "네비게이션 단말기 상세")
public class NaviDevice extends ResponseData {

	private NaviDeviceVo naviDevice;

	/**
	 * 네비게이션 단말기 정보
	 **/
	@ApiModelProperty(value = "네비게이션 단말기 상세")
	@JsonProperty("naviDevice")
	public NaviDeviceVo getNaviDevice() {
		return naviDevice;
	}

	public void setNaviDevice(NaviDeviceVo naviDevice) {
		this.naviDevice = naviDevice;
	}

}

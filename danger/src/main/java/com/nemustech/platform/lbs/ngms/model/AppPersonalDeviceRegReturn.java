package com.nemustech.platform.lbs.ngms.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [추가] [안전운행 11] 개인단말 차량 등록
 */
@ApiModel(description = "안전운행 개인단말 등록 리턴 VO")
public class AppPersonalDeviceRegReturn extends ResponseData {

	private String device_uid;
	private NaviDeviceVo naviDeviceVo;

	@ApiModelProperty(value = "device_uid")
	@JsonProperty("device_uid")
	public String getDevice_uid() {
		return device_uid;
	}

	public void setDevice_uid(String device_uid) {
		this.device_uid = device_uid;
	}

	@ApiModelProperty(value = "naviDevice")
	@JsonProperty("naviDevice")
	public NaviDeviceVo getNaviDeviceVo() {
		return naviDeviceVo;
	}

	public void setNaviDeviceVo(NaviDeviceVo naviDeviceVo) {
		this.naviDeviceVo = naviDeviceVo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

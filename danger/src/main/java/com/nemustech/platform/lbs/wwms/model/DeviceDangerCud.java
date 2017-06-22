package com.nemustech.platform.lbs.wwms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerCudVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "위험지역 단말관리 단말정보")
public class DeviceDangerCud extends ResponseData {

	private DeviceDangerCudVo deviceDangerCudVo;

	@ApiModelProperty(value = "위험지역 단말관리 단말정보")
	@JsonProperty("deviceDangerVo")
	public DeviceDangerCudVo getDeviceDangerCudVo() {
		return deviceDangerCudVo;
	}

	public void setDeviceDangerCudVo(DeviceDangerCudVo deviceDangerCudVo) {
		this.deviceDangerCudVo = deviceDangerCudVo;
	}

}

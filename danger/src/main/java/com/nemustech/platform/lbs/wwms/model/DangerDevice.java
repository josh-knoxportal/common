package com.nemustech.platform.lbs.wwms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "위험지역  단말정보")
public class DangerDevice extends ResponseData {

	private DeviceDangerVo deviceDangerVo;
	private WorkReg WorkReg;

	@ApiModelProperty(value = "위험지역 단말기정보")
	@JsonProperty("deviceDangerVo")
	public DeviceDangerVo getDeviceDangerVo() {
		return deviceDangerVo;
	}

	public void setDeviceDangerVo(DeviceDangerVo deviceDangerVo) {
		this.deviceDangerVo = deviceDangerVo;
	}

	public WorkReg getWorkReg() {
		return WorkReg;
	}

	public void setWorkReg(WorkReg workReg) {
		WorkReg = workReg;
	}

	

}

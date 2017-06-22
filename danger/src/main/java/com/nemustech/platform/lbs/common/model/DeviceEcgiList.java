package com.nemustech.platform.lbs.common.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.vo.DeviceBeaconVo;
import com.nemustech.platform.lbs.common.vo.DeviceEcgiVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "API 단말 ECGI 리턴 정보")
public class DeviceEcgiList extends ResponseData {

	private List<DeviceEcgiVo> ecgi;
	private List<DeviceBeaconVo> beacon;

	@ApiModelProperty(value = "ecgi")
	@JsonProperty("ecgi")
	public List<DeviceEcgiVo> getEcgi() {
		return ecgi;
	}

	public void setEcgi(List<DeviceEcgiVo> ecgi) {
		this.ecgi = ecgi;
	}

	@ApiModelProperty(value = "beacon")
	@JsonProperty("beacon")
	public List<DeviceBeaconVo> getBeacon() {
		return beacon;
	}

	public void setBeacon(List<DeviceBeaconVo> beacon) {
		this.beacon = beacon;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

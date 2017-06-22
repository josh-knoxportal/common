package com.nemustech.platform.lbs.wwms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.BeaconDangerVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "위험지역 비콘관리 비콘정보")
public class BeaconDangerCud extends ResponseData {

	private BeaconDangerVo beaconDangerVo;

	@ApiModelProperty(value = "위험지역 비콘관리 비콘정보")
	@JsonProperty("beaconDangerVo")
	public BeaconDangerVo getBeaconDangerVo() {
		return beaconDangerVo;
	}

	public void setBeaconDangerVo(BeaconDangerVo beaconDangerVo) {
		this.beaconDangerVo = beaconDangerVo;
	}

}

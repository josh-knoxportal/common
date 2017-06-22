package com.nemustech.platform.lbs.wwms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.BeaconDangerActivateVo;
import com.nemustech.platform.lbs.wwms.vo.BeaconDangerVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "위험지역 비콘관리 목록")
public class BeaconDangerList extends ResponseData {

	private List<BeaconDangerVo> beaconDangerList;
	private BeaconDangerActivateVo beaconActivate;
	private int totalCnt = 0;

	@ApiModelProperty(value = "위험지역 비콘관리 목록")
	@JsonProperty("beaconDangerList")
	public List<BeaconDangerVo> getBeaconDangerList() {
		return beaconDangerList;
	}

	public void setBeaconDangerList(List<BeaconDangerVo> beaconDangerList) {
		this.beaconDangerList = beaconDangerList;
	}

	@ApiModelProperty(value = "위험지역 비콘관리 목록 Total Count")
	@JsonProperty("totalCnt")
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	@ApiModelProperty(value = "위험지역 비콘관리 활성/비활성 개수 정보")
	@JsonProperty("beaconActivate")
	public BeaconDangerActivateVo getBeaconActivate() {
		return beaconActivate;
	}

	public void setBeaconActivate(BeaconDangerActivateVo beaconActivate) {
		this.beaconActivate = beaconActivate;
	}

}

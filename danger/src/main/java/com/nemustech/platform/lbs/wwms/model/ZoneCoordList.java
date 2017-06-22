package com.nemustech.platform.lbs.wwms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.ZoneCoordVo;
import com.nemustech.platform.lbs.wwms.vo.ZoneVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 비콘 존 및 좌표계 목록
 **/
@ApiModel(description = "비콘 존 및 좌표계 목록")
public class ZoneCoordList extends ResponseData {
	
	private List<ZoneVo> 	 zoneList;
	private List<ZoneCoordVo> zoneCoordList;

	@ApiModelProperty(value = "zoneList")
	@JsonProperty("zoneList")
	public List<ZoneVo> getZoneList() {
		return zoneList;
	}
	public void setZoneList(List<ZoneVo> zoneList) {
		this.zoneList = zoneList;
	}
	
	
	@ApiModelProperty(value = "zoneCoordList")
	@JsonProperty("zoneCoordList")
	public List<ZoneCoordVo> getZoneCoordList() {
		return zoneCoordList;
	}
	public void setZoneCoordList(List<ZoneCoordVo> zoneCoordList) {
		this.zoneCoordList = zoneCoordList;
	}
	
}
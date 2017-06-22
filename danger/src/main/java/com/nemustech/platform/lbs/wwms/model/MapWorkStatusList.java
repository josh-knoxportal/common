package com.nemustech.platform.lbs.wwms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.MapWorkStatusOnFactoriesVo;
import com.nemustech.platform.lbs.wwms.vo.MapWorkStatusOnWorkTypeVo;
import com.nemustech.platform.lbs.wwms.vo.MapWorkStatusOnZonesVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 조건에 따라 맵에 표시할 작업 현황
 **/
@ApiModel(description = "조건에 따라 맵에 표시할 작업 현황")
public class MapWorkStatusList extends ResponseData {
	private String show_type = "";
	
	private List<MapWorkStatusOnFactoriesVo> 	factoryStatusList;
	private List<MapWorkStatusOnZonesVo> 		zoneStatusList;
	private List<MapWorkStatusOnWorkTypeVo> 	worktypeStatusList;
	
	
	@ApiModelProperty(value ="show_type")
	@JsonProperty("show_type")
	public String getShow_type() {
		return show_type;
	}
	public void setShow_type(String show_type) {
		this.show_type = show_type;
	}
	
	@ApiModelProperty(value ="factoryStatusList")
	@JsonProperty("factoryStatusList")
	public List<MapWorkStatusOnFactoriesVo> getFactoryStatusList() {
		return factoryStatusList;
	}
	public void setFactoryStatusList(List<MapWorkStatusOnFactoriesVo> factoryStatusList) {
		this.factoryStatusList = factoryStatusList;
	}
	
	@ApiModelProperty(value ="zoneStatusList")
	@JsonProperty("zoneStatusList")
	public List<MapWorkStatusOnZonesVo> getZoneStatusList() {
		return zoneStatusList;
	}
	public void setZoneStatusList(List<MapWorkStatusOnZonesVo> zoneStatusList) {
		this.zoneStatusList = zoneStatusList;
	}
	
	@ApiModelProperty(value ="worktypeStatusList")
	@JsonProperty("worktypeStatusList")
	public List<MapWorkStatusOnWorkTypeVo> getWorktypeStatusList() {
		return worktypeStatusList;
	}
	public void setWorktypeStatusList(List<MapWorkStatusOnWorkTypeVo> worktypeStatusList) {
		this.worktypeStatusList = worktypeStatusList;
	}
	
	
}
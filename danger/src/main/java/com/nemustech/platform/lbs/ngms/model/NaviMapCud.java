package com.nemustech.platform.lbs.ngms.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.common.vo.MapVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 네이게이션맵 정보
 **/
@ApiModel(description = "네이게이션맵 정보")
public class NaviMapCud extends ResponseData{

	private MapVo naviMapVo;

	@ApiModelProperty(value = "네이게이션맵 정보")
	@JsonProperty("naviMapVo")	
	public MapVo getNaviMapVo() {
		return naviMapVo;
	}

	public void setNaviMapVo(MapVo naviMapVo) {
		this.naviMapVo = naviMapVo;
	}
	

	
	

	



}

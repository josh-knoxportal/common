package com.nemustech.platform.lbs.wwms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.GpsZoneVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "GpsZone")
public class GpsZone extends ResponseData{
	private List<GpsZoneVo> list;
	
		
	@ApiModelProperty(value = "")
	@JsonProperty("count")
	public int getCount(){
		if(list != null)
			return list.size();
		else
			return 0;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("gps_zones")
	public List<GpsZoneVo> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<GpsZoneVo> list) {
		this.list = list;
	}
}

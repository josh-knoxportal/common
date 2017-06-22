package com.nemustech.platform.lbs.wwms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.BeaconZoneVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "BeaconZone")
public class BeaconZone extends ResponseData{
	private List<BeaconZoneVo> list;
	
		
	@ApiModelProperty(value = "")
	@JsonProperty("count")
	public int getCount(){
		if(list != null)
			return list.size();
		else
			return 0;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("beacon_zones")
	public List<BeaconZoneVo> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<BeaconZoneVo> list) {
		this.list = list;
	}
}

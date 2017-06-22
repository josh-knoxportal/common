package com.nemustech.platform.lbs.wwms.model;




import java.util.List;

import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.wwms.vo.AppZoneEventVo;

import io.swagger.annotations.ApiModel;



/**
 *
 **/
@ApiModel(description = "RestrictedMap")
public class RestrictedMap extends ResponseData{
	private List<AppZoneEventVo> restrictedList;

	public List<AppZoneEventVo> getRestrictedList() {
		return restrictedList;
	}

	public void setRestrictedList(List<AppZoneEventVo> restrictedList) {
		this.restrictedList = restrictedList;
	}
	
		
	
}

package com.nemustech.platform.lbs.wwms.vo;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 검색 VO
 **/
@ApiModel(description = "위험지역 비콘존 설정")
public class MapZoneRestrictUpdateRequestVo  {
	private String		 zone_uids = "";
	/* local */
	private List<String> zoneList;
	
	public List<String> getFadctoryList() {
		return zoneList;
	}
	public void setZoneList(List<String> zoneList) {
		this.zoneList = zoneList;
	}
	

	
	@ApiModelProperty(value ="위험지역 비콘존 식별자 리스트( , 구분)")
	@JsonProperty("zone_uids")
	public String getZone_uids() {
		return zone_uids;
	}
	public void setZone_uids(String zone_uids) {
		this.zoneList = new ArrayList<String>();
		this.zone_uids = zone_uids;
		String[] arrUids = zone_uids.split(",");		
		if (arrUids != null) {
			for(int i = 0; i < arrUids.length; i++) {
				this.zoneList.add(arrUids[i]);
			}
		}
	}	
	
	/* */
	public boolean isEmpty() {
		if (this.zoneList == null || this.zoneList.size() == 0) return true;
		return false;
	}
	
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class SpeedLimitRequestVo {\n");
		sb.append("  zone_uids: ").append(zone_uids).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
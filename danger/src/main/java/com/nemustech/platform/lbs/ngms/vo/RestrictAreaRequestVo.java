package com.nemustech.platform.lbs.ngms.vo;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.util.StringUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 제한지역설정 VO
 **/
@ApiModel(description = "제한지역설정")
public class RestrictAreaRequestVo  {
	private String		 section_uids = "";
	private int			 is_restrict_area = 0;
	
	/* [2016/08/18] added by capsy */
	private String		 vehicle_type = "";
	
	/* local */
	private List<String> sectionList;
	
	public List<String> getSectionList() {
		return sectionList;
	}
	public void setSectionList(List<String> sectionList) {
		this.sectionList = sectionList;
	}
	
	
	@ApiModelProperty(value ="제한지역설정을 위한 도로구간식별자 리스트")
	@JsonProperty("section_uids")
	public String getSection_uids() {
		return section_uids;
	}
	public void setSection_uids(String section_uids) {
		this.section_uids = section_uids;
		String[] arrUids = section_uids.split(",");
		this.sectionList = new ArrayList<String>();
		if (arrUids != null) {
			for(int i = 0; i < arrUids.length; i++) {
				this.sectionList.add(arrUids[i]);
			}
		}
	}
	
	@ApiModelProperty(value ="설정값")
	@JsonProperty("is_restrict_area")	
	public int getIs_restrict_area() {
		return is_restrict_area;
	}
	public void setIs_restrict_area(int is_restrict_area) {
		this.is_restrict_area = is_restrict_area;
	}
	
	
	
	/* [2016/08/18] added by capsy */
	public String getVehicle_type() {
		return vehicle_type;
	}
	/* [2016/08/18] added by capsy */
	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}
	
	
	/* */
	public boolean isEmpty() {
		if (StringUtil.isEmpty(vehicle_type) || this.sectionList == null || this.sectionList.size() == 0) return true;
		return false;
	}
	
	
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class RestrictAreaRequestVo {\n");
		sb.append("  section_uids: ").append(section_uids).append("\n");
		sb.append("  ,is_restrict_area: ").append(is_restrict_area).append("\n");
	
		
		sb.append("}\n");
		return sb.toString();
	}
}

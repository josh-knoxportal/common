package com.nemustech.platform.lbs.ngms.vo;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 제한속도갱신 VO
 **/
@ApiModel(description = "제한속도갱신")
public class SpeedLimitRequestVo  {
	private int			 is_all = 0;
	private String		 section_uids = "";
	private int			 speed_limit;
	
	/* local */
	private List<String> sectionList;
	
	public List<String> getSectionList() {
		return sectionList;
	}
	public void setSectionList(List<String> sectionList) {
		this.sectionList = sectionList;
	}
	
	/* */	
	public int getIs_all() {
		return is_all;
	}
	
	
	@ApiModelProperty(value ="제한지역설정을 위한 도로구간식별자 리스트( , 구분)(주의 : '*'는 모두")
	@JsonProperty("section_uids")
	public String getSection_uids() {
		return section_uids;
	}
	public void setSection_uids(String section_uids) {
		this.sectionList = new ArrayList<String>();
		
		if ("*".equals(section_uids)) {
			this.is_all = 1;
			this.section_uids = "";
			return ;
		}
		
		this.section_uids = section_uids;
		String[] arrUids = section_uids.split(",");		
		if (arrUids != null) {
			for(int i = 0; i < arrUids.length; i++) {
				this.sectionList.add(arrUids[i]);
			}
		}
	}	
	
	@ApiModelProperty(value ="제한속도")
	@JsonProperty("speed_limit")
	public int getSpeed_limit() {
		return speed_limit;
	}
	public void setSpeed_limit(int speed_limit) {
		this.speed_limit = speed_limit;
	}
	
	
	
	/* */
	public boolean isEmpty() {
		if (this.is_all == 1) return false;
		if (this.sectionList == null || this.sectionList.size() == 0) return true;
		return false;
	}
	
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class SpeedLimitRequestVo {\n");
		sb.append("  section_uids: ").append(section_uids).append("\n");
		sb.append("  ,speed_limit: ").append(speed_limit).append("\n");
	
		
		sb.append("}\n");
		return sb.toString();
	}
}

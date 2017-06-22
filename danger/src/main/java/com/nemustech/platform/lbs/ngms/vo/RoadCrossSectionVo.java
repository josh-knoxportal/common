package com.nemustech.platform.lbs.ngms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "Road Linked VO")
public class RoadCrossSectionVo  {
	private String 	cross_uid;
	private String 	section_uid;
	
	
/*
	@ApiModelProperty(value ="")
	@JsonProperty("xxxx")
 */
	
	@ApiModelProperty(value ="")
	@JsonProperty("cross_uid")
	public String getCross_uid() {
		return cross_uid;
	}


	public void setCross_uid(String cross_uid) {
		this.cross_uid = cross_uid;
	}
	
	@ApiModelProperty(value ="")
	@JsonProperty("section_uid")
	public String getSection_uid() {
		return section_uid;
	}


	public void setSection_uid(String section_uid) {
		this.section_uid = section_uid;
	}
	


	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class RoadSectionVo {\n");
		sb.append("  cross_uid: ").append(cross_uid).append("\n");
		sb.append(" ,section_uid: ").append(section_uid).append("\n");
		sb.append("}\n");
		return sb.toString();
	}


	




}

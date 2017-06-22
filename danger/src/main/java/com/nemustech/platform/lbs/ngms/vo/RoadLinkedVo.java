package com.nemustech.platform.lbs.ngms.vo;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "Road Linked VO")
public class RoadLinkedVo  {
	private String 	master_section_uid;
	private List<String> 	sub_section_uid;
	
	
/*
	@ApiModelProperty(value ="")
	@JsonProperty("xxxx")
 */
	
	@ApiModelProperty(value ="기준도로")
	@JsonProperty("master_section_uid")
	public String getMaster_section_uid() {
		return master_section_uid;
	}


	public void setMaster_section_uid(String master_section_uid) {
		this.master_section_uid = master_section_uid;
	}
	
	@ApiModelProperty(value ="연결도로")
	@JsonProperty("sub_section_uid")
	public List<String> getSub_section_uid() {
		return sub_section_uid;
	}


	public void setSub_section_uid(List<String> sub_section_uid) {
		this.sub_section_uid = sub_section_uid;
	}
	


	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class RoadSectionVo {\n");
		sb.append("  master_section_uid: ").append(master_section_uid).append("\n");
		sb.append(" ,sub_section_uid: ").append(sub_section_uid).append("\n");
		sb.append("}\n");
		return sb.toString();
	}


	




}

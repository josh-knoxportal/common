package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "APP 패스워드 확인이벤트 등록 VO")
public class AppWorkUnRegEventVo  {
	
	private String 	work_uid;
	private String 	device_no;

	

	@ApiModelProperty(value ="work_uid")
	@JsonProperty("work_uid")
	public String getWork_uid() {
		return work_uid;
	}

	public void setWork_uid(String work_uid) {
		this.work_uid = work_uid;
	}


	@ApiModelProperty(value ="device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}
	
	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}


	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class AppWorkUnRegEventVo {\n");
		sb.append(" ,device_no: ").append(device_no).append("\n");
		sb.append(" ,work_uid: ").append(work_uid).append("\n");
		
		sb.append("}\n");
		return sb.toString();
	}

	

	

	
}

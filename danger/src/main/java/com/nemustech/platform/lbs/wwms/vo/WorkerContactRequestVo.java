package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.util.StringUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "위험지역에 있는 작업자 연락처 조회 파라미터 VO")
public class WorkerContactRequestVo  {
	private String type = "";
	private String uid = "";
	
	
	@ApiModelProperty(value ="factory or zone")
	@JsonProperty("type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	@ApiModelProperty(value ="uid")
	@JsonProperty("uid")
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
	/* */
	public boolean isValid() {
		if (StringUtil.isEmpty(this.type) ||
				StringUtil.isEmpty(this.uid) ) return false;
		
		if ("factory".equals(this.type) ||
				"zone".equals(this.type)) {
			return true;
		}
		
		return false;
	}
}

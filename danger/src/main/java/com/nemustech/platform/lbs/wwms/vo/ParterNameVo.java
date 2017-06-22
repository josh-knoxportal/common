package com.nemustech.platform.lbs.wwms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel(description = "부서/업체명 VO")
public class ParterNameVo {
	private String parter_name;
	private String parter_code;

	@ApiModelProperty(value = "parter_name")
	@JsonProperty("parter_name")
	public String getParter_name() {
		return parter_name;
	}

	public void setParter_name(String parter_name) {
		this.parter_name = parter_name;
	}

	@ApiModelProperty(value = "parter_code")
	@JsonProperty("parter_code")
	public String getParter_code() {
		return parter_code;
	}

	public void setParter_code(String parter_code) {
		this.parter_code = parter_code;
	}

}

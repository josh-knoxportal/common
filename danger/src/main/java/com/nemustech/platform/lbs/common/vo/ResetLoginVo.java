package com.nemustech.platform.lbs.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "ResetLoginVo 정보")
public class ResetLoginVo extends LoginVo {

	private String email;
	private String name;

	@ApiModelProperty(value = "email")
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ApiModelProperty(value = "name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

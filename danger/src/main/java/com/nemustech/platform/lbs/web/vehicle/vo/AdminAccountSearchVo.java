package com.nemustech.platform.lbs.web.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.vo.SearchVo;

import io.swagger.annotations.ApiModelProperty;

public class AdminAccountSearchVo extends SearchVo {
	private String search_type;
	private int system_type;

	@ApiModelProperty(value = "search_type")
	@JsonProperty("search_type")
	public String getSearch_type() {
		return search_type;
	}

	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}

	@ApiModelProperty(value = "system_type")
	@JsonProperty("system_type")
	public int getSystem_type() {
		return system_type;
	}

	public void setSystem_type(int system_type) {
		this.system_type = system_type;
	}

}

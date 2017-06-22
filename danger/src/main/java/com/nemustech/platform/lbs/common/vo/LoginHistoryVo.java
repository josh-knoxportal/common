package com.nemustech.platform.lbs.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "LoginHistoryVo 정보")
public class LoginHistoryVo {

	private String user_id;
	private int history_type;

	@ApiModelProperty(value = "user_id")
	@JsonProperty("user_id")
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	@ApiModelProperty(value = "history_type")
	@JsonProperty("history_type")
	public int getHistory_type() {
		return history_type;
	}

	public void setHistory_type(int history_type) {
		this.history_type = history_type;
	}

}

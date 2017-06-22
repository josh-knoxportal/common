package com.nemustech.platform.lbs.wwms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "위험지역 단말 비밀번호 정보 VO")
public class DeviceDangerPasswordVo {

	private String current_password;
	private String new_password;
	private String editor_id;

	@ApiModelProperty(value = "current_password")
	@JsonProperty("current_password")
	public String getCurrent_password() {
		return current_password;
	}

	public void setCurrent_password(String current_password) {
		this.current_password = current_password;
	}

	@ApiModelProperty(value = "new_password")
	@JsonProperty("new_password")
	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}

	@ApiModelProperty(value = "editor_id")
	@JsonProperty("editor_id")
	public String getEditor_id() {
		return editor_id;
	}

	public void setEditor_id(String editor_id) {
		this.editor_id = editor_id;
	}

}

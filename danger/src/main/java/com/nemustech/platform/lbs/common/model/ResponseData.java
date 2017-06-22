package com.nemustech.platform.lbs.common.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * API 리턴 정보
 **/
@ApiModel(description = "API 리턴 정보")
public class ResponseData  {

	
	private String msg = "";
	
	@ApiModelProperty(value = "Message")
	@JsonProperty("msg")
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class ResponseData {\n");
		sb.append("  msg: ").append(msg).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}

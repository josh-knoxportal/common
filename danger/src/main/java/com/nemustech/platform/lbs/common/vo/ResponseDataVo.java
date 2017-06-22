package com.nemustech.platform.lbs.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * API 리턴 정보
 **/
@ApiModel(description = "API 리턴 정보")
public class ResponseDataVo  {

	private String message = "";
		
	/**
	 * 설명
	 **/
	@ApiModelProperty(value = "설명")
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class ResponseData {\n");

		sb.append("  message: ").append(message).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	public JSONObject toJsonNotiObj() {
		JSONObject jobj = this.toJsonMsgObj();
		
		return jobj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJsonMsgObj() {
		JSONObject jobj = new JSONObject();
		jobj.put("message", this.getMessage());
		return jobj;
	}
}

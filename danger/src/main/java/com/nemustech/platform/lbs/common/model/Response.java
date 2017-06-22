package com.nemustech.platform.lbs.common.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * API 리턴 정보
 **/
@ApiModel(description = "API 리턴 정보")
public class Response  {
	
	public static final int OK = 0;
	public static final int ERROR = -1;
	public static final int ENOTFOUND = -2;
	public static final int EACCESS = -5;
	

	private Long id = null;
	private String msg = "";

	/**
	 * 생성되는 id (optional)
	 **/
	@ApiModelProperty(value = "생성되는 id (optional)")
	@JsonProperty("id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

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

		sb.append("  id: ").append(id).append("\n");
		sb.append("  msg: ").append(msg).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}

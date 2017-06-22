package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "작업코드 VO")
public class CodeWorkTypeVo  {
	private String code = "";
	private String name = "";
	private int	   order_no = 0;
	
	
	@ApiModelProperty(value ="code")
	@JsonProperty("code")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	@ApiModelProperty(value ="name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	@ApiModelProperty(value ="order_no")
	@JsonProperty("order_no")
	public int getOrder_no() {
		return order_no;
	}
	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}
}

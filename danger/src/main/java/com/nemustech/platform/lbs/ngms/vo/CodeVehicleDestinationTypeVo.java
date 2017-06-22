package com.nemustech.platform.lbs.ngms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 목적지 타입 정보 VO
 */
@ApiModel(description = "목적지 타입 정보 VO")
public class CodeVehicleDestinationTypeVo {
	private String type;
	private String type_name;
	private int order_no;

	@ApiModelProperty(value = "type")
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ApiModelProperty(value = "type_name")
	@JsonProperty("type_name")
	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	@ApiModelProperty(value = "order_no")
	@JsonProperty("order_no")
	public int getOrder_no() {
		return order_no;
	}

	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}

}

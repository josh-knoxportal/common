package com.nemustech.platform.lbs.ngms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 차량유형
 */
@ApiModel(description = "차량유형 VO")
public class CodeVehicleTypeVo {

	private String code = "";
	private String name = "";
	private int order_no = 0;

	@ApiModelProperty(value = "code")
	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ApiModelProperty(value = "name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(value = "order_no")
	@JsonProperty("order_no")
	public int getOrder_no() {
		return order_no;
	}

	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

package com.nemustech.platform.lbs.ngms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 목적지 정보 VO
 */
@ApiModel(description = "목적지 정보 VO")
public class CodeVehicleDestinationVo {

	private String type;
	private String type_name;
	private String name;
	private int order_no;
	private double x;
	private double y;

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

	@ApiModelProperty(value = "x")
	@JsonProperty("x")
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	@ApiModelProperty(value = "y")
	@JsonProperty("y")
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

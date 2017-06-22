package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "공장 좌표계 정보 VO")
public class FactoryCoordVo  {	
	private String  factory_uid;
	private int 	order_no;
	private float	x;
	private float   y;
	private float	latitude;
	private float   longitude;
	
	
	@ApiModelProperty(value ="factory_uid")
	@JsonProperty("factory_uid")
	public String getFactory_uid() {
		return factory_uid;
	}
	public void setFactory_uid(String factory_uid) {
		this.factory_uid = factory_uid;
	}
	
	
	
	@ApiModelProperty(value ="order_no")
	@JsonProperty("order_no")
	public int getOrder_no() {
		return order_no;
	}
	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}
	
	
	@ApiModelProperty(value ="x")
	@JsonProperty("x")
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	
	
	@ApiModelProperty(value ="y")
	@JsonProperty("y")
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	
	@ApiModelProperty(value ="latitude")
	@JsonProperty("latitude")
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	
	@ApiModelProperty(value ="longitude")
	@JsonProperty("longitude")
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
}

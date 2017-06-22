package com.nemustech.platform.lbs.common.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 네이게이션맵 정보
 **/
@ApiModel(description = "위도/경도 X,Y 좌표")
public class Coordinate {

	private double x;
	private double y;
	
	private double lat;
	private double lng;

	
	
	@ApiModelProperty(value = "x좌표")
	@JsonProperty("x")
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	@ApiModelProperty(value = "y좌표")
	@JsonProperty("y")
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@ApiModelProperty(value = "위도")
	@JsonProperty("lat")
	public double getLat() {
		return lat;
	}
	
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	@ApiModelProperty(value = "경도")
	@JsonProperty("lng")
	public double getLng() {
		return lng;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}

}

package com.nemustech.platform.lbs.ngms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.Coordinate;
import com.nemustech.platform.lbs.common.model.CoordinateOrder;
import com.nemustech.platform.lbs.common.model.ResponseData;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 네이게이션맵 정보
 **/
@ApiModel(description = "네이게이션맵 정보")
public class NaviMap extends ResponseData{

	
	private long version;
	private int polygon;
	private Coordinate center;
	private List<CoordinateOrder> points;
	

	@ApiModelProperty(value = "map version")
	@JsonProperty("version")
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	
	@ApiModelProperty(value = "polygon count")
	@JsonProperty("polygon")
	public int getPolygon() {
		return polygon;
	}
	public void setPolygon(int polygon) {
		this.polygon = polygon;
	}
	
	@ApiModelProperty(value = "center coordinate")
	@JsonProperty("center")
	public Coordinate getCenter() {
		return center;
	}
	public void setCenter(Coordinate center) {
		this.center = center;
	}
	
	@ApiModelProperty(value = "center list")
	@JsonProperty("points")
	public List<CoordinateOrder> getPoints() {
		return points;
	}
	public void setPoints(List<CoordinateOrder> points) {
		this.points = points;
	}
	

}

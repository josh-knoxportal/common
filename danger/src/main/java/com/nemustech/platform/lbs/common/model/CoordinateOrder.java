package com.nemustech.platform.lbs.common.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 네이게이션맵 정보
 **/
@ApiModel(description = " order 포함위도/경도 X,Y 좌표")
public class CoordinateOrder extends Coordinate{

	private int order;

	@ApiModelProperty(value = "x좌표")
	@JsonProperty("order")
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	
}

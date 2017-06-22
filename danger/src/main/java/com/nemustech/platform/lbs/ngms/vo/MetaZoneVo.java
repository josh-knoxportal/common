package com.nemustech.platform.lbs.ngms.vo;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.CoordinateOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * // 160804 [개인폰] [추가] [안전운행 16] Meta MAP GPS Zone 정보 조회
 */
@ApiModel(description = "Meta Zone VO")
public class MetaZoneVo {
	private String meta_zone_id;
	private String meta_zone_name;
	private int restricted;
	private int polygon;
	private List<CoordinateOrder> zone_points;

	@ApiModelProperty(value = "meta_zone_id")
	@JsonProperty("meta_zone_id")
	public String getMeta_zone_id() {
		return meta_zone_id;
	}

	public void setMeta_zone_id(String meta_zone_id) {
		this.meta_zone_id = meta_zone_id;
	}

	@ApiModelProperty(value = "meta_zone_name")
	@JsonProperty("meta_zone_name")
	public String getMeta_zone_name() {
		return meta_zone_name;
	}

	public void setMeta_zone_name(String meta_zone_name) {
		this.meta_zone_name = meta_zone_name;
	}

	@ApiModelProperty(value = "restricted")
	@JsonProperty("restricted")
	public int getRestricted() {
		return restricted;
	}

	public void setRestricted(int restricted) {
		this.restricted = restricted;
	}

	@ApiModelProperty(value = "polygon")
	@JsonProperty("polygon")
	public int getPolygon() {
		return polygon;
	}

	public void setPolygon(int polygon) {
		this.polygon = polygon;
	}

	@ApiModelProperty(value = "zone_points")
	@JsonProperty("zone_points")
	public List<CoordinateOrder> getZone_points() {
		return zone_points;
	}

	public void setZone_points(List<CoordinateOrder> zone_points) {
		this.zone_points = zone_points;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

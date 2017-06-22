package com.nemustech.platform.lbs.wwms.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.CoordinateOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 **/
@ApiModel(description = "GPS Zone VO")
public class GpsZoneVo {
	private String gps_zone_id;
	private String gps_zone_name; // 160804 [개인폰] [추가] [안전운행9] gps_zone_name
	private int restricted;
	private int polygon;

	private List<CoordinateOrder> zone_points;

	// private List<BeaconZoneVo> beacon_zones;

	@ApiModelProperty(value = "gps_zone_id")
	@JsonProperty("gps_zone_id")
	public String getGps_zone_id() {
		return gps_zone_id;
	}

	/**
	 * @param gps_zone_id
	 *            the gps_zone_id to set
	 */
	public void setGps_zone_id(String gps_zone_id) {
		this.gps_zone_id = gps_zone_id;
	}

	@ApiModelProperty(value = "restricted")
	@JsonProperty("restricted")
	public int getRestricted() {
		return restricted;
	}

	/**
	 * @param restricted
	 *            the restricted to set
	 */
	public void setRestricted(int restricted) {
		this.restricted = restricted;
	}

	@ApiModelProperty(value = "polygon")
	@JsonProperty("polygon")
	public int getPolygon() {
		// 160804 [개인폰] [수정] polygon 값은 MAX(order_no)
		// if (zone_points != null)
		// polygon = zone_points.size();
		return polygon;
	}

	/**
	 * @param polygon
	 *            the polygon to set
	 */
	public void setPolygon(int polygon) {
		this.polygon = polygon;
	}

	@ApiModelProperty(value = "zone_points")
	@JsonProperty("zone_points")
	public List<CoordinateOrder> getZone_points() {
		return zone_points;
	}

	/**
	 * @param zone_points
	 *            the zone_points to set
	 */
	public void setZone_points(List<CoordinateOrder> zone_points) {
		this.zone_points = zone_points;
	}
	/*
	 * @ApiModelProperty(value ="beacon_zones")
	 * 
	 * @JsonProperty("beacon_zones") public List<BeaconZoneVo> getBeacon_zones()
	 * { return beacon_zones; }
	 * 
	 * public void setBeacon_zones(List<BeaconZoneVo> beacon_zones) {
	 * this.beacon_zones = beacon_zones; }
	 */

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class " + this.getClass().getName() + " {\n");
		sb.append("  gps_zone_id: ").append(gps_zone_id).append("\n");
		sb.append("  gps_zone_name: ").append(gps_zone_name).append("\n");
		sb.append(" ,restricted: ").append(restricted).append("\n");
		sb.append(" ,polygon: ").append(polygon).append("\n");
		sb.append(" ,zone_points: ").append(zone_points).append("\n");
		// sb.append(" ,beacon_zones: ").append(beacon_zones).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

	@ApiModelProperty(value = "gps_zone_name")
	@JsonProperty("gps_zone_name")
	public String getGps_zone_name() {
		return gps_zone_name;
	}

	public void setGps_zone_name(String gps_zone_name) {
		this.gps_zone_name = gps_zone_name;
	}

}

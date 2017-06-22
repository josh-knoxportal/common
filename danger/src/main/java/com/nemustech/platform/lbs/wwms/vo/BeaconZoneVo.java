package com.nemustech.platform.lbs.wwms.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 **/
@ApiModel(description = "Beacon Zone VO")
public class BeaconZoneVo {
	private String beacon_zone_id;
	private String beacon_zone_name; // 160804 [개인폰] [추가]
	private String gps_zone_name; // 160804 [개인폰] [추가] gps_zone_name
	private String gps_zone_id;
	private int restricted;
	private int beacon_count;

	private List<BeaconVo> beacon_list;

	@ApiModelProperty(value = "beacon_zone_id")
	@JsonProperty("beacon_zone_id")
	public String getBeacon_zone_id() {
		return beacon_zone_id;
	}

	/**
	 * @param beacon_zone_id
	 *            the beacon_zone_id to set
	 */
	public void setBeacon_zone_id(String beacon_zone_id) {
		this.beacon_zone_id = beacon_zone_id;
	}

	@ApiModelProperty(value = "beacon_count")
	@JsonProperty("beacon_count")
	public int getBeacon_count() {
		if (beacon_list != null)
			beacon_count = beacon_list.size();

		return beacon_count;
	}

	/**
	 * @param beacon_count
	 *            the beacon_count to set
	 */
	public void setBeacon_count(int beacon_count) {
		this.beacon_count = beacon_count;
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

	@ApiModelProperty(value = "beacon_list")
	@JsonProperty("beacon_list")
	public List<BeaconVo> getBeacon_list() {
		return beacon_list;
	}

	public void setBeacon_list(List<BeaconVo> beacon_list) {
		this.beacon_list = beacon_list;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class " + this.getClass().getName() + " {\n");
		sb.append("  beacon_zone_id: ").append(beacon_zone_id).append("\n");
		sb.append("  beacon_zone_name: ").append(beacon_zone_name).append("\n");
		sb.append("  gps_zone_id: ").append(gps_zone_id).append("\n");
		sb.append("  gps_zone_name: ").append(gps_zone_name).append("\n");
		sb.append(" ,restricted: ").append(restricted).append("\n");
		sb.append(" ,beacon_count: ").append(beacon_count).append("\n");
		sb.append(" ,beacon_list: ").append(beacon_list).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

	@ApiModelProperty(value = "gps_zone_id")
	@JsonProperty("gps_zone_id")
	public String getGps_zone_id() {
		return gps_zone_id;
	}

	public void setGps_zone_id(String gps_zone_id) {
		this.gps_zone_id = gps_zone_id;
	}

	@ApiModelProperty(value = "gps_zone_name")
	@JsonProperty("gps_zone_name")
	public String getGps_zone_name() {
		return gps_zone_name;
	}

	public void setGps_zone_name(String gps_zone_name) {
		this.gps_zone_name = gps_zone_name;
	}

	@ApiModelProperty(value = "beacon_zone_name")
	@JsonProperty("beacon_zone_name")
	public String getBeacon_zone_name() {
		return beacon_zone_name;
	}

	public void setBeacon_zone_name(String beacon_zone_name) {
		this.beacon_zone_name = beacon_zone_name;
	}
}

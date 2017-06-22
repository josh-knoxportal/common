package com.nemustech.platform.lbs.wwms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.Coordinate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 **/
@ApiModel(description = "차량운행기록 VO")
public class AppZoneEventVo {
	private String type;
	private String work_uid;
	private String worker_uid;
	private String zone_id;
	private String device_no;
	private String device_uid;
	private int entered;
	private int restricted;
	private long time;
	private String beacon_zone_id;

	private Coordinate position;

	private int authorizied = 0;
	private String zone_name;
	private int event_enter_type; // 작업인는 경우 1, 작업할당 없이 진출입 한 경우 0

	@ApiModelProperty(value = "event type[gps,beacon]")
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	@ApiModelProperty(value = "work uid")
	@JsonProperty("work_uid")
	public String getWork_uid() {
		return work_uid;
	}

	/**
	 * @param work_uid
	 *            the work_uid to set
	 */
	public void setWork_uid(String work_uid) {
		this.work_uid = work_uid;
	}

	@ApiModelProperty(value = "zone uid")
	@JsonProperty("zone_id")
	public String getZone_id() {
		return zone_id;
	}

	/**
	 * @param zone_id
	 *            the zone_id to set
	 */
	public void setZone_id(String zone_id) {
		this.zone_id = zone_id;
	}

	@ApiModelProperty(value = "entered")
	@JsonProperty("entered")
	public int getEntered() {
		return entered;
	}

	/**
	 * @param entered
	 *            the entered to set
	 */
	public void setEntered(int entered) {
		this.entered = entered;
	}

	@ApiModelProperty(value = "is restricted")
	@JsonProperty("restricted")
	public int getRestricted() {
		return restricted;
	}

	public void setRestricted(int restricted) {
		this.restricted = restricted;
	}

	@ApiModelProperty(value = "current position")
	@JsonProperty("position")
	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}

	@ApiModelProperty(value = "time is event reg time(type long)")
	@JsonProperty("time")
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@ApiModelProperty(value = "beacon_zone_id")
	@JsonProperty("beacon_zone_id")
	public String getBeacon_zone_id() {
		return beacon_zone_id;
	}

	public void setBeacon_zone_id(String beacon_zone_id) {
		this.beacon_zone_id = beacon_zone_id;
	}

	@ApiModelProperty(value = "authorizied")
	@JsonProperty("authorizied")
	public int getAuthorizied() {
		return authorizied;
	}

	public void setAuthorizied(int authorizied) {
		this.authorizied = authorizied;
	}

	@ApiModelProperty(value = "zone_name")
	@JsonProperty("zone_name")
	public String getZone_name() {
		return zone_name;
	}

	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "event_enter_type")
	@JsonProperty("event_enter_type")
	public int getEvent_enter_type() {
		return event_enter_type;
	}

	public void setEvent_enter_type(int event_enter_type) {
		this.event_enter_type = event_enter_type;
	}

	@ApiModelProperty(value = "worker_uid")
	@JsonProperty("worker_uid")
	public String getWorker_uid() {
		return worker_uid;
	}

	public void setWorker_uid(String worker_uid) {
		this.worker_uid = worker_uid;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AppZoneEventVo {\n");
		sb.append("  type: ").append(type).append("\n");
		sb.append(" ,work_uid: ").append(work_uid).append("\n");
		sb.append(" ,zone_id: ").append(zone_id).append("\n");
		sb.append(" ,device_no: ").append(device_no).append("\n");
		sb.append(" ,restricted: ").append(restricted).append("\n");
		sb.append(" ,position: ").append(position).append("\n");
		sb.append(" ,entered: ").append(entered).append("\n");
		sb.append(" ,time: ").append(time).append("\n");
		sb.append(" ,beacon_zone_id: ").append(beacon_zone_id).append("\n");
		sb.append(" ,authorizied: ").append(authorizied).append("\n");
		sb.append(" ,zone_name: ").append(zone_name).append("\n");
		sb.append(" ,event_enter_type: ").append(event_enter_type).append("\n");
		sb.append(" ,worker_uid: ").append(worker_uid).append("\n");
		sb.append(" ,device_uid: ").append(device_uid).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

	@ApiModelProperty(value = "device_uid")
	@JsonProperty("device_uid")
	public String getDevice_uid() {
		return device_uid;
	}

	public void setDevice_uid(String device_uid) {
		this.device_uid = device_uid;
	}
}

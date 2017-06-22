package com.nemustech.platform.lbs.wwms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "위험지역 비콘관리 목록 VO")
public class BeaconDangerVo {
	private String beacon_uid;
	private String zone_uid;
	private String name;
	private String factory_name;
	private String zone_name;
	private int is_activated;
	private String activated_status;
	private int battery;
	private String battery_ox;
	private String editor_id;
	private String upd_date;

	private String minor;
	private int signal_period;
	private float txpower;
	private String description;

	@ApiModelProperty(value = "beacon_uid")
	@JsonProperty("beacon_uid")
	public String getBeacon_uid() {
		return beacon_uid;
	}

	public void setBeacon_uid(String beacon_uid) {
		this.beacon_uid = beacon_uid;
	}

	@ApiModelProperty(value = "name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(value = "factory_name")
	@JsonProperty("factory_name")
	public String getFactory_name() {
		return factory_name;
	}

	public void setFactory_name(String factory_name) {
		this.factory_name = factory_name;
	}

	@ApiModelProperty(value = "zone_name")
	@JsonProperty("zone_name")
	public String getZone_name() {
		return zone_name;
	}

	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}

	@ApiModelProperty(value = "is_activated")
	@JsonProperty("is_activated")
	public int getIs_activated() {
		return is_activated;
	}

	public void setIs_activated(int is_activated) {
		this.is_activated = is_activated;
	}

	@ApiModelProperty(value = "activated_status")
	@JsonProperty("activated_status")
	public String getActivated_status() {
		return activated_status;
	}

	public void setActivated_status(String activated_status) {
		this.activated_status = activated_status;
	}

	@ApiModelProperty(value = "battery")
	@JsonProperty("battery")
	public int getBattery() {
		return battery;
	}

	public void setBattery(int battery) {
		this.battery = battery;
	}

	@ApiModelProperty(value = "battery_ox")
	@JsonProperty("battery_ox")
	public String getBattery_ox() {
		return battery_ox;
	}

	public void setBattery_ox(String battery_ox) {
		this.battery_ox = battery_ox;
	}

	@ApiModelProperty(value = "editor_id")
	@JsonProperty("editor_id")
	public String getEditor_id() {
		return editor_id;
	}

	public void setEditor_id(String editor_id) {
		this.editor_id = editor_id;
	}

	@ApiModelProperty(value = "upd_date")
	@JsonProperty("upd_date")
	public String getUpd_date() {
		return upd_date;
	}

	public void setUpd_date(String upd_date) {
		this.upd_date = upd_date;
	}

	@ApiModelProperty(value = "minor")
	@JsonProperty("minor")
	public String getMinor() {
		return minor;
	}

	public void setMinor(String minor) {
		this.minor = minor;
	}

	@ApiModelProperty(value = "signal_period")
	@JsonProperty("signal_period")
	public int getSignal_period() {
		return signal_period;
	}

	public void setSignal_period(int signal_period) {
		this.signal_period = signal_period;
	}

	@ApiModelProperty(value = "txpower")
	@JsonProperty("txpower")
	public float getTxpower() {
		return txpower;
	}

	public void setTxpower(float txpower) {
		this.txpower = txpower;
	}

	@ApiModelProperty(value = "description")
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ApiModelProperty(value = "zone_uid")
	@JsonProperty("zone_uid")
	public String getZone_uid() {
		return zone_uid;
	}

	public void setZone_uid(String zone_uid) {
		this.zone_uid = zone_uid;
	}

}

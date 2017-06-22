package com.nemustech.platform.lbs.ngms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "차량운행기록 VO")
public class DrivingEventVo  {
	private String 	event_uid;
	private String 	vehicle_uid;
	private String 	section_uid;
	private String 	vehicle_no;
	private String 	device_no;
	private int 	speed;
	private float 	latitude;
	private float	longitude;
	private float 	x;
	private float 	y;
	private int 	is_over_speed;
	private int 	is_on_road;
	private int 	is_on_restrict_area;
	private String 	reg_date;

/*
	@ApiModelProperty(value ="")
	@JsonProperty("xxxx")
 */
	@ApiModelProperty(value ="")
	@JsonProperty("event_uid")
	public String getEvent_uid() {
		return event_uid;
	}

	public void setEvent_uid(String event_uid) {
		this.event_uid = event_uid;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("vehicle_uid")
	public String getVehicle_uid() {
		return vehicle_uid;
	}

	public void setVehicle_uid(String vehicle_uid) {
		this.vehicle_uid = vehicle_uid;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("section_uid")
	public String getSection_uid() {
		return section_uid;
	}

	public void setSection_uid(String section_uid) {
		this.section_uid = section_uid;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("vehicle_no")
	public String getVehicle_no() {
		return vehicle_no;
	}

	public void setVehicle_no(String vehicle_no) {
		this.vehicle_no = vehicle_no;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("speed")
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("latitude")
	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("longitude")
	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("x")
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("y")
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("is_over_speed")
	public int getIs_over_speed() {
		return is_over_speed;
	}

	public void setIs_over_speed(int is_over_speed) {
		this.is_over_speed = is_over_speed;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("is_on_road")
	public int getIs_on_road() {
		return is_on_road;
	}

	public void setIs_on_road(int is_on_road) {
		this.is_on_road = is_on_road;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("is_on_restrict_area")
	public int getIs_on_restrict_area() {
		return is_on_restrict_area;
	}

	public void setIs_on_restrict_area(int is_on_restrict_area) {
		this.is_on_restrict_area = is_on_restrict_area;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("reg_date")
	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}


	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class DrivingEventVo {\n");
		sb.append("  event_uid: ").append(event_uid).append("\n");
		sb.append(" ,vehicle_uid: ").append(vehicle_uid).append("\n");
		sb.append(" ,section_uid: ").append(section_uid).append("\n");
		sb.append(" ,vehicle_no: ").append(vehicle_no).append("\n");
		sb.append(" ,device_no: ").append(device_no).append("\n");
		sb.append(" ,speed: ").append(speed).append("\n");
		sb.append(" ,latitude: ").append(latitude).append("\n");
		sb.append(" ,longitude: ").append(longitude).append("\n");
		sb.append(" ,x: ").append(x).append("\n");
		sb.append(" ,y: ").append(y).append("\n");
		sb.append(" ,is_over_speed: ").append(is_over_speed).append("\n");
		sb.append(" ,is_on_road: ").append(is_on_road).append("\n");
		sb.append(" ,is_on_restrict_area: ").append(is_on_restrict_area).append("\n");
		sb.append(" ,reg_date: ").append(reg_date).append("\n");
		
		
		sb.append("}\n");
		return sb.toString();
	}


}

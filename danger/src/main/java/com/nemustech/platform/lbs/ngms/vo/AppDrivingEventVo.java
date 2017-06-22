package com.nemustech.platform.lbs.ngms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.Coordinate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "차량운행기록 VO")
public class AppDrivingEventVo  {
	private float 	speed;
	private String 	road;
	private int	speeding;
	private int restricted;
	private Coordinate position;
	private long time;
	private String vehicle_uid;
	private String device_no;
	


/*
	@ApiModelProperty(value ="")
	@JsonProperty("xxxx")
 */
	@ApiModelProperty(value ="speed")
	@JsonProperty("speed")
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	@ApiModelProperty(value ="road section")
	@JsonProperty("road")
	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}
	
	@ApiModelProperty(value ="is over speeding")
	@JsonProperty("speeding")
	public int getSpeeding() {
		return speeding;
	}

	public void setSpeeding(int speeding) {
		this.speeding = speeding;
	}

	@ApiModelProperty(value ="is restricted")
	@JsonProperty("restricted")
	public int getRestricted() {
		return restricted;
	}

	public void setRestricted(int restricted) {
		this.restricted = restricted;
	}

	@ApiModelProperty(value ="current position")
	@JsonProperty("position")
	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}
	
	@ApiModelProperty(value ="vehicle uid")
	@JsonProperty("vehicle_uid")
	public String getVehicle_uid() {
		return vehicle_uid;
	}

	public void setVehicle_uid(String vehicle_uid) {
		this.vehicle_uid = vehicle_uid;
	}
	
	
	@ApiModelProperty(value ="time is event reg time(type long)")
	@JsonProperty("time")
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class DrivingEventVo {\n");
		sb.append("  speed: ").append(speed).append("\n");
		sb.append(" ,road: ").append(road).append("\n");
		sb.append(" ,speeding: ").append(speeding).append("\n");
		sb.append(" ,restricted: ").append(restricted).append("\n");
		sb.append(" ,position: ").append(position).append("\n");	
		sb.append(" ,vehicle_uid: ").append(vehicle_uid).append("\n");	
		sb.append(" ,time: ").append(time).append("\n");	
		sb.append(" ,device_no: ").append(device_no).append("\n");	
		sb.append("}\n");
		return sb.toString();
	}

	
	
	
	


}

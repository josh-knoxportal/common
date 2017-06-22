package com.nemustech.platform.lbs.ngms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "도로기준 차량운행기록 VO")
public class DrivingSectionVo  {
	private String 	vehicle_uid;
	private String 	section_uid;
	private int 	count_over_speed;
	private int 	count_on_restrict_area;
	private String 	from_reg_date;
	private String 	to_reg_date;
	private float	from_x;
	private float	from_y;
	private float	to_x;
	private float	to_y;	
	
	/* [2016/09/22] added */
	private int		speed;
	
/*
	@ApiModelProperty(value ="")
	@JsonProperty("xxxx")
 */

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
	@JsonProperty("count_over_speed")
	public int getCount_over_speed() {
		return count_over_speed;
	}

	public void setCount_over_speed(int count_over_speed) {
		this.count_over_speed = count_over_speed;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("count_on_restrict_area")
	public int getCount_on_restrict_area() {
		return count_on_restrict_area;
	}

	public void setCount_on_restrict_area(int count_on_restrict_area) {
		this.count_on_restrict_area = count_on_restrict_area;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("from_reg_date")
	public String getFrom_reg_date() {
		return from_reg_date;
	}

	public void setFrom_reg_date(String from_reg_date) {
		this.from_reg_date = from_reg_date;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("to_reg_date")
	public String getTo_reg_date() {
		return to_reg_date;
	}

	public void setTo_reg_date(String to_reg_date) {
		this.to_reg_date = to_reg_date;
	}
	

	@ApiModelProperty(value ="")
	@JsonProperty("from_x")
	public float getFrom_x() {
		return from_x;
	}

	public void setFrom_x(float from_x) {
		this.from_x = from_x;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("from_y")
	public float getFrom_y() {
		return from_y;
	}

	public void setFrom_y(float from_y) {
		this.from_y = from_y;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("to_x")
	public float getTo_x() {
		return to_x;
	}

	public void setTo_x(float to_x) {
		this.to_x = to_x;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("to_y")
	public float getTo_y() {
		return to_y;
	}

	public void setTo_y(float to_y) {
		this.to_y = to_y;
	}
	
	
	/* [2016/09/22] added */
	public int getSpeed() {
		return speed;
	}

	/* [2016/09/22] added */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class DrivingSectionVo {\n");
		sb.append("  vehicle_uid: ").append(vehicle_uid).append("\n");
		sb.append(" ,section_uid: ").append(section_uid).append("\n");
		sb.append(" ,count_over_speed: ").append(count_over_speed).append("\n");
		sb.append(" ,count_on_restrict_area: ").append(count_on_restrict_area).append("\n");
		sb.append(" ,from_reg_date: ").append(from_reg_date).append("\n");
		sb.append(" ,to_reg_date: ").append(to_reg_date).append("\n");
		sb.append(" ,from_x: ").append(from_x).append("\n");
		sb.append(" ,from_y: ").append(from_y).append("\n");
		sb.append(" ,to_x: ").append(to_x).append("\n");
		sb.append(" ,to_y: ").append(to_y).append("\n");
		/* [2016/09/22] added */
		sb.append(" ,speed: ").append(speed).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}

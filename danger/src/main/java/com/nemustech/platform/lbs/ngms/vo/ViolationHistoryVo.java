package com.nemustech.platform.lbs.ngms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "차량위반 VO")
public class ViolationHistoryVo  {
	private String 	violation_uid;
	private String 	vehicle_uid;
	private String 	begin_section_uid;
	private String  end_section_uid;
	private String 	come_in_date;
	private String 	go_out_date;
	private int 	violation_type;
	private int 	in_speed;
	private int		avg_speed;
	private int 	peak_speed;

/*
	@ApiModelProperty(value ="")
	@JsonProperty("xxxx")
 */
	@ApiModelProperty(value ="")
	@JsonProperty("violation_uid")
	public String getViolation_uid() {
		return violation_uid;
	}

	public void setViolation_uid(String violation_uid) {
		this.violation_uid = violation_uid;
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
	@JsonProperty("begin_section_uid")
	public String getBegin_section_uid() {
		return begin_section_uid;
	}

	public void setBegin_section_uid(String begin_section_uid) {
		this.begin_section_uid = begin_section_uid;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("end_section_uid")
	public String getEnd_section_uid() {
		return end_section_uid;
	}

	public void setEnd_section_uid(String end_section_uid) {
		this.end_section_uid = end_section_uid;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("come_in_date")
	public String getCome_in_date() {
		return come_in_date;
	}

	public void setCome_in_date(String come_in_date) {
		this.come_in_date = come_in_date;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("go_out_date")
	public String getGo_out_date() {
		return go_out_date;
	}

	public void setGo_out_date(String go_out_date) {
		this.go_out_date = go_out_date;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("violation_type")
	public int getViolation_type() {
		return violation_type;
	}

	public void setViolation_type(int violation_type) {
		this.violation_type = violation_type;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("in_speed")
	public int getIn_speed() {
		return in_speed;
	}

	public void setIn_speed(int in_speed) {
		this.in_speed = in_speed;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("avg_speed")
	public int getAvg_speed() {
		return avg_speed;
	}

	public void setAvg_speed(int avg_speed) {
		this.avg_speed = avg_speed;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("peak_speed")
	public int getPeak_speed() {
		return peak_speed;
	}

	public void setPeak_speed(int peak_speed) {
		this.peak_speed = peak_speed;
	}

	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class ViolationHistoryVo {\n");
		sb.append("  violation_uid: ").append(violation_uid).append("\n");
		sb.append(" ,vehicle_uid: ").append(vehicle_uid).append("\n");
		sb.append(" ,begin_section_uid: ").append(begin_section_uid).append("\n");
		sb.append(" ,end_section_uid: ").append(end_section_uid).append("\n");
		sb.append(" ,come_in_date: ").append(come_in_date).append("\n");
		sb.append(" ,go_out_date: ").append(go_out_date).append("\n");
		sb.append(" ,violation_type: ").append(violation_type).append("\n");
		sb.append(" ,in_speed: ").append(in_speed).append("\n");
		sb.append(" ,avg_speed: ").append(avg_speed).append("\n");
		sb.append(" ,peak_speed: ").append(peak_speed).append("\n");
		
		sb.append("}\n");
		return sb.toString();
	}
}

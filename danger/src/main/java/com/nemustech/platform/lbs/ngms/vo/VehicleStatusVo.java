package com.nemustech.platform.lbs.ngms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 맵에 차량정보를 표시하기 위한 차량 정보
 **/
@ApiModel(description = "차량운행상태 VO")
public class VehicleStatusVo  {
	private String 	vehicle_uid;
	private String 	vehicle_no;
	private String 	device_no;
	private String 	come_in_date;
	private String 	go_out_date;
	private int 	is_out;
	private int		had_over_speed;
	private int		had_on_restrict_area;
	private float 	last_x;
	private float 	last_y;
	private float 	last_latitude;
	private float 	last_longitude;
	private float 	last_speed;
	private int 	last_is_over_speed;
	private int 	last_on_restrict_area;
	
	/* [2016/08/18] added */
	private String	vehicle_type;
	

	@ApiModelProperty(value ="차량 uid")
	@JsonProperty("vehicle_uid")
	public String getVehicle_uid() {
		return vehicle_uid;
	}

	public void setVehicle_uid(String vehicle_uid) {
		this.vehicle_uid = vehicle_uid;
	}
	
	@ApiModelProperty(value ="단말기 번호")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}
	
	@ApiModelProperty(value="차량 번호")
	@JsonProperty("vehicle_no")
	public String getVehicle_no() {
		return vehicle_no;
	}

	public void setVehicle_no(String vehicle_no) {
		this.vehicle_no = vehicle_no;
	}
	
	
	@ApiModelProperty(value="")
	@JsonProperty("come_in_date")
	public String getCome_in_date() {
		return come_in_date;
	}

	public void setCome_in_date(String come_in_date) {
		this.come_in_date = come_in_date;
	}

	@ApiModelProperty(value="")
	@JsonProperty("go_out_date")
	public String getGo_out_date() {
		return go_out_date;
	}

	public void setGo_out_date(String go_out_date) {
		this.go_out_date = go_out_date;
	}

	@ApiModelProperty(value="")
	@JsonProperty("is_out")
	public int getIs_out() {
		return is_out;
	}

	public void setIs_out(int is_out) {
		this.is_out = is_out;
	}
	
	@ApiModelProperty(value="")
	@JsonProperty("had_over_speed")
	public int getHad_over_speed() {
		return had_over_speed;
	}

	public void setHad_over_speed(int had_over_speed) {
		this.had_over_speed = had_over_speed;
	}

	@ApiModelProperty(value="")
	@JsonProperty("had_on_restrict_area")
	public int getHad_on_restrict_area() {
		return had_on_restrict_area;
	}

	public void setHad_on_restrict_area(int had_on_restrict_area) {
		this.had_on_restrict_area = had_on_restrict_area;
	}

	@ApiModelProperty(value="")
	@JsonProperty("last_x")
	public float getLast_x() {
		return last_x;
	}

	public void setLast_x(float last_x) {
		this.last_x = last_x;
	}

	@ApiModelProperty(value="")
	@JsonProperty("last_y")
	public float getLast_y() {
		return last_y;
	}

	public void setLast_y(float last_y) {
		this.last_y = last_y;
	}

	@ApiModelProperty(value="")
	@JsonProperty("last_latitude")
	public float getLast_latitude() {
		return last_latitude;
	}
	
	public void setLast_latitude(float last_latitude) {
		this.last_latitude = last_latitude;
	}

	@ApiModelProperty(value="")
	@JsonProperty("last_longitude")
	public float getLast_longitude() {
		return last_longitude;
	}

	
	public void setLast_longitude(float last_longitude) {
		this.last_longitude = last_longitude;
	}

	@ApiModelProperty(value="")
	@JsonProperty("last_speed")
	public float getLast_speed() {
		return last_speed;
	}

	public void setLast_speed(float last_speed) {
		this.last_speed = last_speed;
	}

	@ApiModelProperty(value="")
	@JsonProperty("last_is_over_speed")
	public int getLast_is_over_speed() {
		return last_is_over_speed;
	}

	public void setLast_is_over_speed(int last_is_over_speed) {
		this.last_is_over_speed = last_is_over_speed;
	}

	@ApiModelProperty(value ="")
	@JsonProperty("last_on_restrict_area")
	public int getLast_on_restrict_area() {
		return last_on_restrict_area;
	}

	public void setLast_on_restrict_area(int last_on_restrict_area) {
		this.last_on_restrict_area = last_on_restrict_area;
	}

	/* [2016/08/18] added */
	public String getVehicle_type() {
		return vehicle_type;
	}

	/* [2016/08/18] added */
	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class VehicleStatusVo {\n");
		sb.append("  vehicle_uid: ").append(""+vehicle_uid).append("\n");
		
		/* [2016/08/18] added */
		sb.append("  vehicle_type: ").append(""+vehicle_type).append("\n");
		
		sb.append(" ,device_no: ").append(""+device_no).append("\n");
		sb.append(" ,vehicle_no: ").append(""+vehicle_no).append("\n");
		sb.append(" ,come_in_date: ").append(""+come_in_date).append("\n");
		sb.append(" ,go_out_date: ").append(""+go_out_date).append("\n");
		sb.append(" ,is_out: ").append(""+is_out).append("\n");
		sb.append(" ,had_over_speed: ").append(""+had_over_speed).append("\n");
		sb.append(" ,had_on_restrict_area: ").append(""+had_on_restrict_area).append("\n");
		sb.append(" ,last_x: ").append(""+last_x).append("\n");
		sb.append(" ,last_y: ").append(""+last_y).append("\n");
		sb.append(" ,last_latitude: ").append(""+last_latitude).append("\n");
		sb.append(" ,last_longitude: ").append(""+last_longitude).append("\n");
		sb.append(" ,last_speed: ").append(""+last_speed).append("\n");
		sb.append(" ,last_is_over_speed: ").append(""+last_is_over_speed).append("\n");
		sb.append(" ,last_on_restrict_area: ").append(""+last_on_restrict_area).append("\n");
		
		sb.append("}\n");
		return sb.toString();
	}

}

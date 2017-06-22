package com.nemustech.platform.lbs.ngms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 네이게이션 차량등록정보 VO
 **/
@ApiModel(description = "네이게이션 차량등록정보  VO")
public class VehicleRegVo  {

	private String vehicle_uid;
	private String device_uid;
	private String device_no;
	private String vehicle_no;
	private int is_assigned;
	private int is_out;
		
	
	@ApiModelProperty(value ="차량 uid")
	@JsonProperty("vehicle_uid")
	public String getVehicle_uid() {
		return vehicle_uid;
	}

	public void setVehicle_uid(String vehicle_uid) {
		this.vehicle_uid = vehicle_uid;
	}
	
	@ApiModelProperty(value ="단말기 uid")
	@JsonProperty("device_uid")
	public String getDevice_uid() {
		return device_uid;
	}

	public void setDevice_uid(String device_uid) {
		this.device_uid = device_uid;
	}
	
	@ApiModelProperty(value ="단말기 번호")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value ="차량 번호")
	@JsonProperty("vehicle_no")	
	public String getVehicle_no() {
		return vehicle_no;
	}

	public void setVehicle_no(String vehicle_no) {
		this.vehicle_no = vehicle_no;
	}
	
	@ApiModelProperty(value ="할당여부")
	@JsonProperty("is_assigned")	
	public int getIs_assigned() {
		return is_assigned;
	}

	public void setIs_assigned(int is_assigned) {
		this.is_assigned = is_assigned;
	}
	
	@ApiModelProperty(value ="운행여부")
	@JsonProperty("is_out")	
	public int getIs_out() {
		return is_out;
	}

	public void setIs_out(int is_out) {
		this.is_out = is_out;
	}
	
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class VehiclRegVo {\n");
		sb.append("  vehicle_uid: ").append(vehicle_uid).append("\n");
		sb.append(" ,device_uid: ").append(device_uid).append("\n");
		sb.append(" ,device_no: ").append(device_no).append("\n");
		sb.append(" ,vehicle_no: ").append(vehicle_no).append("\n");
		sb.append(" ,is_assigned: ").append(is_assigned).append("\n");
		sb.append(" ,is_out: ").append(is_out).append("\n");
		
		sb.append("}\n");
		return sb.toString();
	}

	


	

	
	

	



}

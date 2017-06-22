package com.nemustech.platform.lbs.ngms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 **/
@ApiModel(description = "APP 차량등록/해제 등록 VO")
public class AppVehicleRegVo {

	private String device_uid;
	private String vehicle_uid;
	private String gcm_token;
	private String car;
	private String device_no;
	private int assigned;
	private String car_type;
	private String model;
	private int is_company; // 0 : 개인, 1 : 법인

	@ApiModelProperty(value = "gcm_token")
	@JsonProperty("gcm_token")
	public String getGcm_token() {
		return gcm_token;
	}

	/**
	 * @param gcm_token
	 *            the gcm_token to set
	 */
	public void setGcm_token(String gcm_token) {
		this.gcm_token = gcm_token;
	}

	@ApiModelProperty(value = "car")
	@JsonProperty("car")
	public String getCar() {
		return car;
	}

	/**
	 * @param car
	 *            the car to set
	 */
	public void setCar(String car) {
		this.car = car;
	}

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "assigned")
	@JsonProperty("assigned")
	public int getAssigned() {
		return assigned;
	}

	public void setAssigned(int assigned) {
		this.assigned = assigned;
	}

	@ApiModelProperty(value = "vehicle_uid")
	@JsonProperty("vehicle_uid")
	public String getVehicle_uid() {
		return vehicle_uid;
	}

	public void setVehicle_uid(String vehicle_uid) {
		this.vehicle_uid = vehicle_uid;
	}

	@ApiModelProperty(value = "device_uid")
	@JsonProperty("device_uid")
	public String getDevice_uid() {
		return device_uid;
	}

	public void setDevice_uid(String device_uid) {
		this.device_uid = device_uid;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AppVehicleRegVo {\n");
		sb.append(" ,gcm_token: ").append(gcm_token).append("\n");
		sb.append(" ,car: ").append(car).append("\n");
		sb.append(" ,device_no: ").append(device_no).append("\n");
		sb.append(" ,assigned: ").append(assigned).append("\n");
		sb.append(" ,vehicle_uid: ").append(vehicle_uid).append("\n");
		sb.append(" ,device_uid: ").append(device_uid).append("\n");
		sb.append(" ,car_type: ").append(car_type).append("\n");
		sb.append(" ,model: ").append(model).append("\n");
		sb.append(" ,is_company: ").append(is_company).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

	@ApiModelProperty(value = "car_type")
	@JsonProperty("car_type")
	public String getCar_type() {
		return car_type;
	}

	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}

	@ApiModelProperty(value = "model")
	@JsonProperty("model")
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@ApiModelProperty(value = "is_company")
	@JsonProperty("is_company")
	public int getIs_company() {
		return is_company;
	}

	public void setIs_company(int is_company) {
		this.is_company = is_company;
	}

}

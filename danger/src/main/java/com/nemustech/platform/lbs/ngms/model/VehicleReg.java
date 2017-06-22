package com.nemustech.platform.lbs.ngms.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.VehicleRegVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 차량등록정보 목록
 **/
@ApiModel(description = "차량등록정보모델")
public class VehicleReg extends ResponseData{

	private VehicleRegVo vehicleReg;
	private int checkCode;

	
	 /**
	   * 차량등록정보 정보
	   **/
	@ApiModelProperty(value = "차량등록정보 목록")
	@JsonProperty("vehicleReg")
	public VehicleRegVo getVehicleReg() {
		return vehicleReg;
	}

	public void setVehicleReg(VehicleRegVo vehicleReg) {
		this.vehicleReg = vehicleReg;
	}
	
	 /**
	   * 체크오류코드
	   **/
	@ApiModelProperty(value = "차량등록정보 목록")
	@JsonProperty("checkCode")
	public int getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(int checkCode) {
		this.checkCode = checkCode;
	}
	
}

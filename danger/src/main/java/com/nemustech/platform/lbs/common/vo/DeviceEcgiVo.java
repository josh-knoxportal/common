package com.nemustech.platform.lbs.common.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "API 단말 ECGI 정보")
public class DeviceEcgiVo {
	private String carrier;
	private String ecgi;

	@ApiModelProperty(value = "carrier")
	@JsonProperty("carrier")
	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	@ApiModelProperty(value = "ecgi")
	@JsonProperty("ecgi")
	public String getEcgi() {
		return ecgi;
	}

	public void setEcgi(String ecgi) {
		this.ecgi = ecgi;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

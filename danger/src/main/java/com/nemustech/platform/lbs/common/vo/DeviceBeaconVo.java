package com.nemustech.platform.lbs.common.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "API 단말 비콘 정보")
public class DeviceBeaconVo {
	private int type;
	private int major;
	private int minor;

	@ApiModelProperty(value = "type")
	@JsonProperty("type")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@ApiModelProperty(value = "major")
	@JsonProperty("major")
	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	@ApiModelProperty(value = "minor")
	@JsonProperty("minor")
	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

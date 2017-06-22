package com.nemustech.platform.lbs.ngms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 **/
@ApiModel(description = "APP 디바이스 정보체크 VO")
public class AppDeviceInfoVo {

	private String device_no;
	private String package_name;
	private String app_key;
	private int version;
	private String device_network_type;

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	/**
	 * @param device_no
	 *            the device_no to set
	 */
	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "package_name")
	@JsonProperty("package_name")
	public String getPackage_name() {
		return package_name;
	}

	/**
	 * @param package_name
	 *            the package_name to set
	 */
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	@ApiModelProperty(value = "version")
	@JsonProperty("version")
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	@ApiModelProperty(value = "app_key")
	@JsonProperty("app_key")
	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class " + this.getClass().getName() + " {\n");
		sb.append(" ,version: ").append(version).append("\n");
		sb.append(" ,device_no: ").append(device_no).append("\n");
		sb.append(" ,package_name: ").append(package_name).append("\n");
		sb.append(" ,app_key: ").append(app_key).append("\n");
		sb.append(" ,device_network_type: ").append(device_network_type).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

	@ApiModelProperty(value = "device_network_type")
	@JsonProperty("device_network_type")
	public String getDevice_network_type() {
		return device_network_type;
	}

	public void setDevice_network_type(String device_network_type) {
		this.device_network_type = device_network_type;
	}

}

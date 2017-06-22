package com.nemustech.platform.lbs.common.vo;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;

/**
 * 서버 설정
 */
@Table("tbcm_server_config")
public class ServerConfig extends CommonVo {
	/**
	 * 식별자
	 */
	@Column(primaryKey = true, autoIncrement = true, sequence = "fn_make_uid()")
	protected Long config_uid;

	/**
	 * Alive Report
	 */
	@Column
	protected Integer alive_report;

	/**
	 * GPS on/off
	 */
	@Column
	protected String gps_onoff;

	/**
	 * 현장출입관리 설정
	 */
	@Column
	protected String danger_config;

	/**
	 * 차량안전운행 설정
	 */
	@Column
	protected String vehicle_config;

	public Long getConfig_uid() {
		return config_uid;
	}

	public void setConfig_uid(Long config_uid) {
		this.config_uid = config_uid;
	}

	public Integer getAlive_report() {
		return alive_report;
	}

	public void setAlive_report(Integer alive_report) {
		this.alive_report = alive_report;
	}

	public String getGps_onoff() {
		return gps_onoff;
	}

	public void setGps_onoff(String gps_onoff) {
		this.gps_onoff = gps_onoff;
	}

	public String getDanger_config() {
		return danger_config;
	}

	public void setDanger_config(String danger_config) {
		this.danger_config = danger_config;
	}

	public String getVehicle_config() {
		return vehicle_config;
	}

	public void setVehicle_config(String vehicle_config) {
		this.vehicle_config = vehicle_config;
	}
}

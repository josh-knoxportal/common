package com.nemustech.platform.lbs.wwms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "서버 설정 정보 VO")
public class ServerConfigVo {
	private String aliveReportCycle;
	private String locationMonitoringRule;

	@ApiModelProperty(value = "locationMonitoringRule")
	@JsonProperty("locationMonitoringRule")
	public String getLocationMonitoringRule() {
		return locationMonitoringRule;
	}

	public void setLocationMonitoringRule(String locationMonitoringRule) {
		this.locationMonitoringRule = locationMonitoringRule;
	}

	@ApiModelProperty(value = "aliveReportCycle")
	@JsonProperty("aliveReportCycle")
	public String getAliveReportCycle() {
		return aliveReportCycle;
	}

	public void setAliveReportCycle(String aliveReportCycle) {
		this.aliveReportCycle = aliveReportCycle;
	}

}

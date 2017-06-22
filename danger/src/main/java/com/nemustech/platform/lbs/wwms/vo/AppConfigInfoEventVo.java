package com.nemustech.platform.lbs.wwms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 160725 [개인폰] [신규] 서버에 저장된 단말 설정값들 1 alive 보고 주기 2 위치 모니터링
 */
@ApiModel(description = "APP 보고 설정값 VO")
public class AppConfigInfoEventVo extends ResponseData {

	private String aliveReportCycle;
	private String locationMonitoringRule;

	@ApiModelProperty(value = "alive_report_cycle")
	@JsonProperty("alive_report_cycle")
	public String getAliveReportCycle() {
		return aliveReportCycle;
	}

	public void setAliveReportCycle(String aliveReportCycle) {
		this.aliveReportCycle = aliveReportCycle;
	}

	@ApiModelProperty(value = "location_monitoring_rule")
	@JsonProperty("location_monitoring_rule")
	public String getLocationMonitoringRule() {
		return locationMonitoringRule;
	}

	public void setLocationMonitoringRule(String locationMonitoringRule) {
		this.locationMonitoringRule = locationMonitoringRule;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

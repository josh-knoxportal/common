package com.nemustech.platform.lbs.wwms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 *  160725 [개인폰] [신규] 단말에서 사용자 등록 정보 VO
 *  단말번호, 작업자 회사명 혹은 부서명
 */
@ApiModel(description = "Worker Regisger Vo")
public class AppNewWorkerRegEventVo {

	private String device_uid;
	private String device_no;

	private String worker_company;
	private String worker_company_name;
	private String worker_division;
	private String worker_division_name;

	@ApiModelProperty(value = "device_uid")
	@JsonProperty("device_uid")
	public String getDevice_uid() {
		return device_uid;
	}

	public void setDevice_uid(String device_uid) {
		this.device_uid = device_uid;
	}

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "worker_company")
	@JsonProperty("worker_company")
	public String getWorker_company() {
		return worker_company;
	}

	public void setWorker_company(String worker_company) {
		this.worker_company = worker_company;
	}

	@ApiModelProperty(value = "worker_company_name")
	@JsonProperty("worker_company_name")
	public String getWorker_company_name() {
		return worker_company_name;
	}

	public void setWorker_company_name(String worker_company_name) {
		this.worker_company_name = worker_company_name;
	}

	@ApiModelProperty(value = "worker_company_name")
	@JsonProperty("worker_company_name")
	public String getWorker_division() {
		return worker_division;
	}

	public void setWorker_division(String worker_division) {
		this.worker_division = worker_division;
	}

	@ApiModelProperty(value = "worker_division_name")
	@JsonProperty("worker_division_name")
	public String getWorker_division_name() {
		return worker_division_name;
	}

	public void setWorker_division_name(String worker_division_name) {
		this.worker_division_name = worker_division_name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

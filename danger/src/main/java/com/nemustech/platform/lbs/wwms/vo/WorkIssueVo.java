package com.nemustech.platform.lbs.wwms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.vo.DateVo;

/**
 * 출입감지 현황 목록 정보
 **/
@ApiModel(description = "출입감지 현황 목록 VO")
public class WorkIssueVo extends DateVo {

	private String totalcnt;
	private String worker_name;
	private String device_no;
	private String parter_name;
	private String factory_uid;
	private String zone_uid;
	private int type; // 1 : 인가자 0:비인가자
	private String e_factory_uid;
	private String e_zone_uid;
	private String in_date;
	private String out_date;

	private String work_type;
	private String work_no;
	private String work_name;
	private String starting_date;
	private String complete_date;
	private int worker_count;

	private String factory_name;
	private String zone_name;
	private String e_factory_name;
	private String e_zone_name;
	private String work_type_name;
	private String work_type_img;

	@ApiModelProperty(value = "인가공장 uid")
	@JsonProperty("factory_uid")
	public String getFactory_uid() {
		return factory_uid;
	}

	public void setFactory_uid(String factory_uid) {
		this.factory_uid = factory_uid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTotalcnt() {
		return totalcnt;
	}

	public void setTotalcnt(String totalcnt) {
		this.totalcnt = totalcnt;
	}

	public String getZone_uid() {
		return zone_uid;
	}

	public void setZone_uid(String zone_uid) {
		this.zone_uid = zone_uid;
	}

	public String getWork_no() {
		return work_no;
	}

	public void setWork_no(String work_no) {
		this.work_no = work_no;
	}

	public String getWorker_name() {
		return worker_name;
	}

	public void setWorker_name(String worker_name) {
		this.worker_name = worker_name;
	}

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}

	public String getParter_name() {
		return parter_name;
	}

	public void setParter_name(String parter_name) {
		this.parter_name = parter_name;
	}

	public int getWorker_count() {
		return worker_count;
	}

	public void setWorker_count(int worker_count) {
		this.worker_count = worker_count;
	}

	public String getStarting_date() {
		return starting_date;
	}

	public void setStarting_date(String starting_date) {
		this.starting_date = starting_date;
	}

	public String getDevice_no() {
		return device_no;
	}

	public String getIn_date() {
		return in_date;
	}

	public void setIn_date(String in_date) {
		this.in_date = in_date;
	}

	public String getOut_date() {
		return out_date;
	}

	public void setOut_date(String out_date) {
		this.out_date = out_date;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	public String getComplete_date() {
		return complete_date;
	}

	public String getE_factory_uid() {
		return e_factory_uid;
	}

	public void setE_factory_uid(String e_factory_uid) {
		this.e_factory_uid = e_factory_uid;
	}

	public String getE_zone_uid() {
		return e_zone_uid;
	}

	public void setE_zone_uid(String e_zone_uid) {
		this.e_zone_uid = e_zone_uid;
	}

	public String getE_factory_name() {
		return e_factory_name;
	}

	public void setE_factory_name(String e_factory_name) {
		this.e_factory_name = e_factory_name;
	}

	public String getE_zone_name() {
		return e_zone_name;
	}

	public void setE_zone_name(String e_zone_name) {
		this.e_zone_name = e_zone_name;
	}

	public void setComplete_date(String complete_date) {
		this.complete_date = complete_date;
	}

	public String getFactory_name() {
		return factory_name;
	}

	public void setFactory_name(String factory_name) {
		this.factory_name = factory_name;
	}

	public String getZone_name() {
		return zone_name;
	}

	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}

	public String getWork_type_name() {
		return work_type_name;
	}

	public void setWork_type_name(String work_type_name) {
		this.work_type_name = work_type_name;
	}

	public String getWork_name() {
		return work_name;
	}

	public void setWork_name(String work_name) {
		this.work_name = work_name;
	}

	public String getWork_type_img() {
		return work_type_img;
	}

	public void setWork_type_img(String work_type_img) {
		this.work_type_img = work_type_img;
	}

}

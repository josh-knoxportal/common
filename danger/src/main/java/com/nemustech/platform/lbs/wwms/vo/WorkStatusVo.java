package com.nemustech.platform.lbs.wwms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.vo.DateVo;

/**
 * 작업자별 현황 목록 정보
 **/
@ApiModel(description = "작업자별 현황 목록 VO")
public class WorkStatusVo extends DateVo {

	private String totalcnt;
	private String work_uid;
	private String factory_uid;
	private String zone_uid;
	private String work_no;
	private String name;
	private String work_day;
	private String work_type;
	private String parter_code;
	private String parter_name;
	private String worker_name;
	private int worker_count;

	private String starting_date;
	private String device_uid;
	private String device_no;
	private String complete_date;
	private int is_completed;
	private String last_factory_uid;
	private String last_zone_uid;
	private String last_checking_date;

	private String factory_name;
	private String zone_name;
	private String last_factory_name;
	private String last_zone_name;
	private String work_type_name;
	private String work_type_img;

	@ApiModelProperty(value = "작업 uid")
	@JsonProperty("work_uid")
	public String getWork_uid() {
		return work_uid;
	}

	public void setWork_uid(String work_uid) {
		this.work_uid = work_uid;
	}

	public String getFactory_uid() {
		return factory_uid;
	}

	public void setFactory_uid(String factory_uid) {
		this.factory_uid = factory_uid;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWork_day() {
		return work_day;
	}

	public void setWork_day(String work_day) {
		this.work_day = work_day;
	}

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}

	public String getParter_code() {
		return parter_code;
	}

	public void setParter_code(String parter_code) {
		this.parter_code = parter_code;
	}

	public String getParter_name() {
		return parter_name;
	}

	public void setParter_name(String parter_name) {
		this.parter_name = parter_name;
	}

	public String getWorker_name() {
		return worker_name;
	}

	public void setWorker_name(String worker_name) {
		this.worker_name = worker_name;
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

	public String getDevice_uid() {
		return device_uid;
	}

	public void setDevice_uid(String device_uid) {
		this.device_uid = device_uid;
	}

	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	public String getComplete_date() {
		return complete_date;
	}

	public void setComplete_date(String complete_date) {
		this.complete_date = complete_date;
	}

	public int getIs_completed() {
		return is_completed;
	}

	public void setIs_completed(int is_completed) {
		this.is_completed = is_completed;
	}

	public String getLast_factory_uid() {
		return last_factory_uid;
	}

	public void setLast_factory_uid(String last_factory_uid) {
		this.last_factory_uid = last_factory_uid;
	}

	public String getLast_zone_uid() {
		return last_zone_uid;
	}

	public void setLast_zone_uid(String last_zone_uid) {
		this.last_zone_uid = last_zone_uid;
	}

	public String getLast_checking_date() {
		return last_checking_date;
	}

	public void setLast_checking_date(String last_checking_date) {
		this.last_checking_date = last_checking_date;
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

	public String getLast_factory_name() {
		return last_factory_name;
	}

	public void setLast_factory_name(String last_factory_name) {
		this.last_factory_name = last_factory_name;
	}

	public String getLast_zone_name() {
		return last_zone_name;
	}

	public void setLast_zone_name(String last_zone_name) {
		this.last_zone_name = last_zone_name;
	}

	public String getTotalcnt() {
		return totalcnt;
	}

	public void setTotalcnt(String totalcnt) {
		this.totalcnt = totalcnt;
	}

	public String getWork_type_name() {
		return work_type_name;
	}

	public void setWork_type_name(String work_type_name) {
		this.work_type_name = work_type_name;
	}

	public String getWork_type_img() {
		return work_type_img;
	}

	public void setWork_type_img(String work_type_img) {
		this.work_type_img = work_type_img;
	}

}

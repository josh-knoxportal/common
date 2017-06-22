package com.nemustech.platform.lbs.wwms.vo;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.vo.DateVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 작업자 등록,수정,삭제 정보
 **/
@ApiModel(description = "작업자 등록,수정,삭제 정보 VO")
public class WorkVo extends DateVo {

	private String work_uid;
	private String factory_uid;
	private String zone_uid;
	private String work_day; // 삭제된 항목
	private String work_no;
	private String name;
	private String parter_code;
	private String parter_name;
	private String worker_name;
	private int worker_count;
	private String zone_type;
	private String starting_date;
	private String complete_date;
	private String work_type;
	private int is_completed;
	private String last_checking_date;
	private String creator_id;
	private String editor_id;
	private String work_make_type;

	private List<String> type_list;

	@ApiModelProperty(value = "작업 uid")
	@JsonProperty("work_uid")
	public String getWork_uid() {
		return work_uid;
	}

	public void setWork_uid(String work_uid) {
		this.work_uid = work_uid;
	}

	public String getZone_type() {
		return zone_type;
	}

	public void setZone_type(String zone_type) {
		this.zone_type = zone_type;
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

	public String getComplete_date() {
		return complete_date;
	}

	public void setComplete_date(String complete_date) {
		this.complete_date = complete_date;
	}

	public List<String> getType_list() {
		return type_list;
	}

	public void setType_list(List<String> type_list) {
		this.type_list = type_list;
	}

	public String getWork_day() {
		return work_day;
	}

	public void setWork_day(String work_day) {
		this.work_day = work_day;
	}

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}

	public String getEditor_id() {
		return editor_id;
	}

	public void setEditor_id(String editor_id) {
		this.editor_id = editor_id;
	}

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}

	public int getIs_completed() {
		return is_completed;
	}

	public void setIs_completed(int is_completed) {
		this.is_completed = is_completed;
	}

	public String getLast_checking_date() {
		return last_checking_date;
	}

	public void setLast_checking_date(String last_checking_date) {
		this.last_checking_date = last_checking_date;
	}

	public String getWork_make_type() {
		return work_make_type;
	}

	public void setWork_make_type(String work_make_type) {
		this.work_make_type = work_make_type;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

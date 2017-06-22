package com.nemustech.platform.lbs.wwms.vo;

import io.swagger.annotations.ApiModel;

import com.nemustech.platform.lbs.common.vo.SearchVo;

@ApiModel(description = "작업 현황 목록 검색")
public class WorkMntRequestVo extends SearchVo {
	private static final String DAYFORMAT = "YYYY-MM-DD";

	/* */
	private String worker_name = "";
	private String device_no = "";
	private String parter_name = "";
	private String parter_code = "";
	private String name = "";
	private String work_no = "";
	private String order_type;
	private String order_desc_asc;
	private String work_uid;
	private String from = "";
	private String to = "";

	public String getWorker_name() {
		return worker_name;
	}

	public void setWorker_name(String worker_name) {
		this.worker_name = worker_name;
	}

	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	public String getParter_name() {
		return parter_name;
	}

	public void setParter_name(String parter_name) {
		this.parter_name = parter_name;
	}

	public String getParter_code() {
		return parter_code;
	}

	public void setParter_code(String parter_code) {
		this.parter_code = parter_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWork_no() {
		return work_no;
	}

	public void setWork_no(String work_no) {
		this.work_no = work_no;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public String getOrder_desc_asc() {
		return order_desc_asc;
	}

	public void setOrder_desc_asc(String order_desc_asc) {
		this.order_desc_asc = order_desc_asc;
	}

	public String getWork_uid() {
		return work_uid;
	}

	public void setWork_uid(String work_uid) {
		this.work_uid = work_uid;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		if (DAYFORMAT.length() == from.length()) {
			this.from = from;
		} else {
			this.from = "";
		}
	}

	public String getTo() {
		return to;
	}

	/* */
	public void setTo(String to) {
		if (DAYFORMAT.length() == to.length()) {
			this.to = to + " 23:59:59.999";
		} else {
			this.to = "";
		}
	}
}

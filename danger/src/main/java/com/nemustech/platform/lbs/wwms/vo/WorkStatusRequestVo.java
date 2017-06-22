package com.nemustech.platform.lbs.wwms.vo;

import io.swagger.annotations.ApiModel;

import com.nemustech.platform.lbs.common.vo.SearchVo;

/**
 * 검색 VO
 **/
@ApiModel(description = "작업자별 현황 목록 검색")
public class WorkStatusRequestVo extends SearchVo {
	private static final String DAYFORMAT = "YYYY-MM-DD";

	/* */
	private String from = "";
	private String to = "";
	private String order_type;
	private String order_desc_asc;
	private String worker_name = "";
	private String device_no = "";

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

}
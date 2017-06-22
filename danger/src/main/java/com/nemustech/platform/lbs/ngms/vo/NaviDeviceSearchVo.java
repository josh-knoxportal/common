package com.nemustech.platform.lbs.ngms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.vo.SearchVo;

@ApiModel(description = "단말기 관리 검색조건 VO")
public class NaviDeviceSearchVo extends SearchVo {
	private String order_type;
	private String order_desc_asc;
	private String device_no = "";
	private String assigned_vehicle_no = "";
	private String search_filter_popup = "";

	@ApiModelProperty(value = "order_type")
	@JsonProperty("order_type")
	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	@ApiModelProperty(value = "order_desc_asc")
	@JsonProperty("order_desc_asc")
	public String getOrder_desc_asc() {
		return order_desc_asc;
	}

	public void setOrder_desc_asc(String order_desc_asc) {
		this.order_desc_asc = order_desc_asc;
	}

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "assigned_vehicle_no")
	@JsonProperty("assigned_vehicle_no")
	public String getAssigned_vehicle_no() {
		return assigned_vehicle_no;
	}

	public void setAssigned_vehicle_no(String assigned_vehicle_no) {
		this.assigned_vehicle_no = assigned_vehicle_no;
	}

	@ApiModelProperty(value = "search_filter_popup")
	@JsonProperty("search_filter_popup")
	public String getSearch_filter_popup() {
		return search_filter_popup;
	}

	public void setSearch_filter_popup(String search_filter_popup) {
		this.search_filter_popup = search_filter_popup;
	}

}

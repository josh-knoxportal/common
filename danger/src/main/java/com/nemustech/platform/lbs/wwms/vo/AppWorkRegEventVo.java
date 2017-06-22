package com.nemustech.platform.lbs.wwms.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 **/
@ApiModel(description = "APP 작업등록 이벤트 VO")
public class AppWorkRegEventVo {

	private String factory_uid;
	private String zone_uid;

	private String work_uid; // return 값
	private String device_no;

	private String work_no;

	private String zone_type; // zone, factory
	private String zone_id;
	private String worker_name;
	private String worker_count;
	private List<String> work_types;

	private String work_make_type; // 0:웹, 1: 단말, 2: LPMS

	@ApiModelProperty(value = "zone_type")
	@JsonProperty("zone_type")
	public String getZone_type() {
		return zone_type;
	}

	/**
	 * @param zone_type
	 *            the zone_type to set
	 */
	public void setZone_type(String zone_type) {
		this.zone_type = zone_type;
	}

	@ApiModelProperty(value = "존 UID")
	@JsonProperty("zone_id")
	public String getZone_id() {
		return zone_id;
	}

	@JsonProperty("factory_uid")
	public String getFactory_uid() {
		return factory_uid;
	}

	/**
	 * @param factory_uid
	 *            the factory_uid to set
	 */
	public void setFactory_uid(String factory_uid) {
		this.factory_uid = factory_uid;
	}

	/**
	 * @param zone_id
	 *            the zone_id to set
	 */
	public void setZone_id(String zone_id) {
		this.zone_id = zone_id;
	}

	@ApiModelProperty(value = "작업자 이름")
	@JsonProperty("worker_name")
	public String getWorker_name() {
		return worker_name;
	}

	/**
	 * @param worker_name
	 *            the worker_name to set
	 */
	public void setWorker_name(String worker_name) {
		this.worker_name = worker_name;
	}

	@ApiModelProperty(value = "작업자 수")
	@JsonProperty("worker_count")
	public String getWorker_count() {
		return worker_count;
	}

	/**
	 * @param worker_count
	 *            the worker_count to set
	 */
	public void setWorker_count(String worker_count) {
		this.worker_count = worker_count;
	}

	@ApiModelProperty(value = "작업유형 목록")
	@JsonProperty("work_types")
	public List<String> getWork_types() {
		return work_types;
	}

	/**
	 * @param work_types
	 *            the work_types to set
	 */
	public void setWork_types(List<String> work_types) {
		this.work_types = work_types;
	}

	@JsonProperty("work_uid")
	public String getWork_uid() {
		return work_uid;
	}

	public void setWork_uid(String work_uid) {
		this.work_uid = work_uid;
	}

	@ApiModelProperty(value = "device_no")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	/**
	 * @param device_no
	 *            the device_no to set
	 */
	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@JsonProperty("work_no")
	public String getWork_no() {
		return work_no;
	}

	public void setWork_no(String work_no) {
		this.work_no = work_no;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class " + this.getClass().getName() + " {\n");
		sb.append(" ,work_uid: ").append(work_uid).append("\n");
		sb.append(" ,device_no: ").append(device_no).append("\n");
		sb.append(" ,zone_type: ").append(zone_type).append("\n");
		sb.append(" ,worker_name: ").append(worker_name).append("\n");
		sb.append(" ,worker_count: ").append(worker_count).append("\n");
		sb.append(" ,work_types: ").append(work_types).append("\n");
		sb.append(" ,factory_uid: ").append(factory_uid).append("\n");
		sb.append(" ,zone_uid: ").append(zone_uid).append("\n");
		sb.append(" ,zone_id: ").append(zone_id).append("\n");
		sb.append(" ,work_make_type: ").append(work_make_type).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

	@JsonProperty("zone_uid")
	public String getZone_uid() {
		return zone_uid;
	}

	public void setZone_uid(String zone_uid) {
		this.zone_uid = zone_uid;
	}

	@JsonProperty("work_make_type")
	public String getWork_make_type() {
		return work_make_type;
	}

	public void setWork_make_type(String work_make_type) {
		this.work_make_type = work_make_type;
	}

}

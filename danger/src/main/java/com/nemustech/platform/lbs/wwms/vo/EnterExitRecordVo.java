package com.nemustech.platform.lbs.wwms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EnterExitRecordVo {
	private String record_uid;
	private String device_no;
	private String work_uid;
	private String worker_uid;
	private String factory_uid;
	private String zone_uid;
	private int is_restricted;
	private String enter_date;
	private String exit_date;
	private String reg_date;
	private String upd_date;
	private int type;

	public String getRecord_uid() {
		return record_uid;
	}

	public void setRecord_uid(String record_uid) {
		this.record_uid = record_uid;
	}

	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	public String getWork_uid() {
		return work_uid;
	}

	public void setWork_uid(String work_uid) {
		this.work_uid = work_uid;
	}

	public String getWorker_uid() {
		return worker_uid;
	}

	public void setWorker_uid(String worker_uid) {
		this.worker_uid = worker_uid;
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

	public int getIs_restricted() {
		return is_restricted;
	}

	public void setIs_restricted(int is_restricted) {
		this.is_restricted = is_restricted;
	}

	public String getEnter_date() {
		return enter_date;
	}

	public void setEnter_date(String enter_date) {
		this.enter_date = enter_date;
	}

	public String getExit_date() {
		return exit_date;
	}

	public void setExit_date(String exit_date) {
		this.exit_date = exit_date;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getUpd_date() {
		return upd_date;
	}

	public void setUpd_date(String upd_date) {
		this.upd_date = upd_date;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}

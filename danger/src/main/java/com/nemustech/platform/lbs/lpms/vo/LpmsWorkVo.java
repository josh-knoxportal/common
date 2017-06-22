package com.nemustech.platform.lbs.lpms.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

@XmlRootElement(name = "Data")
public class LpmsWorkVo {

	private String request_no;
	private String wr_title;
	private String work_plant;
	private String work_plant_desc;
	private String work_gubun;
	private String vendor_code;
	private String vendor_desc;
	private String work_detail;
	private String work_type1;
	private String work_type2;
	private String work_type3;
	private String work_type4;
	private String work_type5;
	private String work_type6;
	private String work_type7;
	private String work_type8;
	private String work_type9;
	private String man_cnt;
	private String work_date;
	private String team_code;
	private String team_desc;
	private String worker_name;

	private String work_uid;
	private String factory_uid;

	@XmlElement(name = "REQUEST_NO")
	public String getRequest_no() {
		return request_no;
	}

	public void setRequest_no(String request_no) {
		this.request_no = request_no;
	}

	@XmlElement(name = "WR_TITLE")
	public String getWr_title() {
		return wr_title;
	}

	public void setWr_title(String wr_title) {
		this.wr_title = wr_title;
	}

	@XmlElement(name = "WORK_PLANT")
	public String getWork_plant() {
		return work_plant;
	}

	public void setWork_plant(String work_plant) {
		this.work_plant = work_plant;
	}

	@XmlElement(name = "WORK_PLANT_DESC")
	public String getWork_plant_desc() {
		return work_plant_desc;
	}

	public void setWork_plant_desc(String work_plant_desc) {
		this.work_plant_desc = work_plant_desc;
	}

	@XmlElement(name = "WORK_GUBUN")
	public String getWork_gubun() {
		return work_gubun;
	}

	public void setWork_gubun(String work_gubun) {
		this.work_gubun = work_gubun;
	}

	@XmlElement(name = "VENDOR_CODE")
	public String getVendor_code() {
		return vendor_code;
	}

	public void setVendor_code(String vendor_code) {
		this.vendor_code = vendor_code;
	}

	@XmlElement(name = "VENDOR_DESC")
	public String getVendor_desc() {
		return vendor_desc;
	}

	public void setVendor_desc(String vendor_desc) {
		this.vendor_desc = vendor_desc;
	}

	@XmlElement(name = "WORK_DETAIL")
	public String getWork_detail() {
		return work_detail;
	}

	public void setWork_detail(String work_detail) {
		this.work_detail = work_detail;
	}

	@XmlElement(name = "WORK_TYPE1")
	public String getWork_type1() {
		return work_type1;
	}

	public void setWork_type1(String work_type1) {
		this.work_type1 = work_type1;
	}

	@XmlElement(name = "WORK_TYPE2")
	public String getWork_type2() {
		return work_type2;
	}

	public void setWork_type2(String work_type2) {
		this.work_type2 = work_type2;
	}

	@XmlElement(name = "WORK_TYPE3")
	public String getWork_type3() {
		return work_type3;
	}

	public void setWork_type3(String work_type3) {
		this.work_type3 = work_type3;
	}

	@XmlElement(name = "WORK_TYPE4")
	public String getWork_type4() {
		return work_type4;
	}

	public void setWork_type4(String work_type4) {
		this.work_type4 = work_type4;
	}

	@XmlElement(name = "WORK_TYPE5")
	public String getWork_type5() {
		return work_type5;
	}

	public void setWork_type5(String work_type5) {
		this.work_type5 = work_type5;
	}

	@XmlElement(name = "WORK_TYPE6")
	public String getWork_type6() {
		return work_type6;
	}

	public void setWork_type6(String work_type6) {
		this.work_type6 = work_type6;
	}

	@XmlElement(name = "WORK_TYPE7")
	public String getWork_type7() {
		return work_type7;
	}

	public void setWork_type7(String work_type7) {
		this.work_type7 = work_type7;
	}

	@XmlElement(name = "WORK_TYPE8")
	public String getWork_type8() {
		return work_type8;
	}

	public void setWork_type8(String work_type8) {
		this.work_type8 = work_type8;
	}

	@XmlElement(name = "WORK_TYPE9")
	public String getWork_type9() {
		return work_type9;
	}

	public void setWork_type9(String work_type9) {
		this.work_type9 = work_type9;
	}

	@XmlElement(name = "MAN_CNT")
	public String getMan_cnt() {
		return man_cnt;
	}

	public void setMan_cnt(String man_cnt) {
		this.man_cnt = man_cnt;
	}

	@XmlElement(name = "WORK_DATE")
	public String getWork_date() {
		return work_date;
	}

	public void setWork_date(String work_date) {
		this.work_date = work_date;
	}

	@XmlElement(name = "TEAM_CODE")
	public String getTeam_code() {
		return team_code;
	}

	public void setTeam_code(String team_code) {
		this.team_code = team_code;
	}

	@XmlElement(name = "TEAM_DESC")
	public String getTeam_desc() {
		return team_desc;
	}

	public void setTeam_desc(String team_desc) {
		this.team_desc = team_desc;
	}

	@XmlElement(name = "WORKER_NAME")
	public String getWorker_name() {
		return worker_name;
	}

	public void setWorker_name(String worker_name) {
		this.worker_name = worker_name;
	}

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

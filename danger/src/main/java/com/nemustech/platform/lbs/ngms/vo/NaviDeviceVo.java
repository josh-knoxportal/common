package com.nemustech.platform.lbs.ngms.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 네이게이션 차량등록정보 VO
 **/
@ApiModel(description = "네이게이션 차량등록정보  VO")
public class NaviDeviceVo {

	private String device_uid;
	private String device_no;
	private String model;
	private int is_assigned;
	private String reg_date;
	private String checked_uid_str;
	private List<String> checked_uid_list;
	private String checked_uid_str_block;
	private List<String> checked_uid_block_list;
	private String assigned_vehicle_no;
	private String assigned_vehicle_type;
	private String assigned_vehicle_regdate;

	private String last_vehicle_uid;
	private String push_id;
	private String access_token;
	private int is_used;
	private int is_block;
	private String upd_date;
	private int is_company;
	private int is_entered;

	@ApiModelProperty(value = "차량 uid")
	@JsonProperty("device_uid")
	public String getDevice_uid() {
		return device_uid;
	}

	public void setDevice_uid(String device_uid) {
		this.device_uid = device_uid;
	}

	@ApiModelProperty(value = "단말기 번호")
	@JsonProperty("device_no")
	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	@ApiModelProperty(value = "단말기 모델명")
	@JsonProperty("model")
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DeviceVo {\n");
		sb.append("  device_uid: ").append(device_uid).append("\n");
		sb.append(" ,device_no: ").append(device_no).append("\n");
		sb.append(" ,assigned_vehicle_no").append(assigned_vehicle_no).append("\n");
		sb.append(" ,assigned_vehicle_type").append(assigned_vehicle_type).append("\n");
		sb.append(" ,assigned_vehicle_regdate").append(assigned_vehicle_regdate).append("\n");
		sb.append(" ,model: ").append(model).append("\n");
		sb.append(" ,is_assigned: ").append(is_assigned).append("\n");
		sb.append(" ,reg_date: ").append(reg_date).append("\n");
		sb.append(" ,is_block: ").append(is_block).append("\n");
		sb.append(" ,upd_date: ").append(upd_date).append("\n");
		sb.append(" ,is_company: ").append(is_company).append("\n");
		sb.append(" ,is_entered: ").append(is_entered).append("\n");

		sb.append("}\n");
		return sb.toString();
	}

	@ApiModelProperty(value = "단말기 사용여부")
	@JsonProperty("is_assigned")
	public int getIs_assigned() {
		return is_assigned;
	}

	public void setIs_assigned(int is_assigned) {
		this.is_assigned = is_assigned;
	}

	@ApiModelProperty(value = "단말기 등록일")
	@JsonProperty("reg_date")
	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	@ApiModelProperty(value = "checked_uid_str")
	@JsonProperty("checked_uid_str")
	public String getChecked_uid_str() {
		return checked_uid_str;
	}

	public void setChecked_uid_str(String checked_uid_str) {
		this.checked_uid_str = checked_uid_str;
		String[] arrUids = checked_uid_str.split(",");
		this.checked_uid_list = new ArrayList<String>();
		if (arrUids != null) {
			for (int i = 0; i < arrUids.length; i++) {
				this.checked_uid_list.add(arrUids[i]);
			}
		}
	}

	@ApiModelProperty(value = "checked_uid_list")
	@JsonProperty("checked_uid_list")
	public List<String> getChecked_uid_list() {
		return checked_uid_list;
	}

	public void setChecked_uid_list(List<String> checked_uid_list) {
		this.checked_uid_list = checked_uid_list;
	}

	@ApiModelProperty(value = "checked_uid_str_block")
	@JsonProperty("checked_uid_str_block")
	public String getChecked_uid_str_block() {
		return checked_uid_str_block;
	}

	public void setChecked_uid_str_block(String checked_uid_str_block) {
		this.checked_uid_str_block = checked_uid_str_block;
		String[] arrUids = checked_uid_str_block.split(",");
		this.checked_uid_block_list = new ArrayList<String>();
		if (arrUids != null) {
			for (int i = 0; i < arrUids.length; i++) {
				this.checked_uid_block_list.add(arrUids[i]);
			}
		}
	}

	@ApiModelProperty(value = "checked_uid_block_list")
	@JsonProperty("checked_uid_block_list")
	public List<String> getChecked_uid_block_list() {
		return checked_uid_block_list;
	}

	public void setChecked_uid_block_list(List<String> checked_uid_block_list) {
		this.checked_uid_block_list = checked_uid_block_list;
	}

	@ApiModelProperty(value = "할당된 차량번호")
	@JsonProperty("assigned_vehicle_no")
	public String getAssigned_vehicle_no() {
		return assigned_vehicle_no;
	}

	public void setAssigned_vehicle_no(String assigned_vehicle_no) {
		this.assigned_vehicle_no = assigned_vehicle_no;
	}

	@ApiModelProperty(value = "할당된 차량유형")
	@JsonProperty("assigned_vehicle_type")
	public String getAssigned_vehicle_type() {
		return assigned_vehicle_type;
	}

	public void setAssigned_vehicle_type(String assigned_vehicle_type) {
		this.assigned_vehicle_type = assigned_vehicle_type;
	}

	@ApiModelProperty(value = "last_vehicle_uid")
	@JsonProperty("last_vehicle_uid")
	public String getLast_vehicle_uid() {
		return last_vehicle_uid;
	}

	public void setLast_vehicle_uid(String last_vehicle_uid) {
		this.last_vehicle_uid = last_vehicle_uid;
	}

	@ApiModelProperty(value = "push_id")
	@JsonProperty("push_id")
	public String getPush_id() {
		return push_id;
	}

	public void setPush_id(String push_id) {
		this.push_id = push_id;
	}

	@ApiModelProperty(value = "access_token")
	@JsonProperty("access_token")
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public int getIs_used() {
		return is_used;
	}

	public void setIs_used(int is_used) {
		this.is_used = is_used;
	}

	@ApiModelProperty(value = "단말기 차단여부")
	@JsonProperty("is_block")
	public int getIs_block() {
		return is_block;
	}

	public void setIs_block(int is_block) {
		this.is_block = is_block;
	}

	@ApiModelProperty(value = "단말기 수정일")
	@JsonProperty("upd_date")
	public String getUpd_date() {
		return upd_date;
	}

	public void setUpd_date(String upd_date) {
		this.upd_date = upd_date;
	}

	@ApiModelProperty(value = "단말 구분")
	@JsonProperty("is_company")
	public int getIs_company() {
		return is_company;
	}

	public void setIs_company(int is_company) {
		this.is_company = is_company;
	}

	@ApiModelProperty(value = "출입상태 구분")
	@JsonProperty("is_entered")
	public int getIs_entered() {
		return is_entered;
	}

	public void setIs_entered(int is_entered) {
		this.is_entered = is_entered;
	}

	@ApiModelProperty(value = "차량 할당일자")
	@JsonProperty("assigned_vehicle_regdate")
	public String getAssigned_vehicle_regdate() {
		return assigned_vehicle_regdate;
	}

	public void setAssigned_vehicle_regdate(String assigned_vehicle_regdate) {
		this.assigned_vehicle_regdate = assigned_vehicle_regdate;
	}

}

package com.nemustech.platform.lbs.web.vehicle.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 어드민 계정정보 VO
 **/
@ApiModel(description = "어드민 계정정보 연산 VO")
public class AdminAccountCudVo extends AdminAccountVo {

	private String current_password;
	private String new_password;

	private String checked_uid_str;
	private List<String> checked_uid_list;
	
	private String opcode_cu;

	@ApiModelProperty(value = "current_password")
	@JsonProperty("current_password")
	public String getCurrent_password() {
		return current_password;
	}

	public void setCurrent_password(String current_password) {
		this.current_password = current_password;
	}

	@ApiModelProperty(value = "new_password")
	@JsonProperty("new_password")
	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}

	@ApiModelProperty(value = "checked_uid_list")
	@JsonProperty("checked_uid_list")
	public List<String> getChecked_uid_list() {
		return checked_uid_list;
	}

	public void setChecked_uid_list(List<String> checked_uid_list) {
		this.checked_uid_list = checked_uid_list;
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
	
	@ApiModelProperty(value = "opcode_cu")
	@JsonProperty("opcode_cu")
	public String getOpcode_cu() {
		return opcode_cu;
	}

	public void setOpcode_cu(String opcode_cu) {
		this.opcode_cu = opcode_cu;
	}

}

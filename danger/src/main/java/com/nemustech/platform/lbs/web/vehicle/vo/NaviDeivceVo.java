package com.nemustech.platform.lbs.web.vehicle.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 어드민 계정정보 VO
 **/
@ApiModel(description = "어드민 계정정보 VO")
public class NaviDeivceVo {

	private String account_uid;
	private String user_id;
	private String system_type;
	private String name;
	private String email;
	private String reg_date;
	private String password;
	private String new_password;
	private String department;
	private String checked_uid_str;
	private List<String> checked_uid_list;
	private int is_admin;
	
	@ApiModelProperty(value = "account_uid")
	@JsonProperty("account_uid")
	public String getAccount_uid() {
		return account_uid;
	}

	public void setAccount_uid(String account_uid) {
		this.account_uid = account_uid;
	}

	@ApiModelProperty(value = "user_id")
	@JsonProperty("user_id")
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	@ApiModelProperty(value = "system_type")
	@JsonProperty("system_type")
	public String getSystem_type() {
		return system_type;
	}

	public void setSystem_type(String system_type) {
		this.system_type = system_type;
	}

	@ApiModelProperty(value = "name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(value = "email")
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ApiModelProperty(value = "reg_date")
	@JsonProperty("reg_date")
	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	@ApiModelProperty(value = "password")
	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ApiModelProperty(value = "new_password")
	@JsonProperty("new_password")
	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}

	@ApiModelProperty(value = "department")
	@JsonProperty("department")
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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

	@ApiModelProperty(value = "is_admin")
	@JsonProperty("is_admin")
	public int getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(int is_admin) {
		this.is_admin = is_admin;
	}
}

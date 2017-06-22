package com.nemustech.platform.lbs.web.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 어드민 계정정보 VO
 **/
@ApiModel(description = "어드민 계정정보 VO")
public class AdminAccountVo {

	private String account_uid;
	private int system_type;
	private String user_id;
	private String password;
	private String new_password;
	private String name;
	private String department;
	private String email;
	private int is_use;
	private int is_admin;
	private String reg_date;
	private String upd_date;

	@ApiModelProperty(value = "account_uid")
	@JsonProperty("account_uid")
	public String getAccount_uid() {
		return account_uid;
	}

	public void setAccount_uid(String account_uid) {
		this.account_uid = account_uid;
	}

	@ApiModelProperty(value = "system_type")
	@JsonProperty("system_type")
	public int getSystem_type() {
		return system_type;
	}

	public void setSystem_type(int system_type) {
		this.system_type = system_type;
	}

	@ApiModelProperty(value = "user_id")
	@JsonProperty("user_id")
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	@ApiModelProperty(value = "name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(value = "department")
	@JsonProperty("department")
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@ApiModelProperty(value = "email")
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ApiModelProperty(value = "is_use")
	@JsonProperty("is_use")
	public int getIs_use() {
		return is_use;
	}

	public void setIs_use(int is_use) {
		this.is_use = is_use;
	}

	@ApiModelProperty(value = "is_admin")
	@JsonProperty("is_admin")
	public int getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(int is_admin) {
		this.is_admin = is_admin;
	}

	@ApiModelProperty(value = "reg_date")
	@JsonProperty("reg_date")
	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	@ApiModelProperty(value = "upd_date")
	@JsonProperty("upd_date")
	public String getUpd_date() {
		return upd_date;
	}

	public void setUpd_date(String upd_date) {
		this.upd_date = upd_date;
	}

}

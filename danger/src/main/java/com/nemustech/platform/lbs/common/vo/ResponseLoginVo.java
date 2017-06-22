package com.nemustech.platform.lbs.common.vo;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "로그인 성공 후 세션 저장정보 VO")
public class ResponseLoginVo {

	private String account_uid;
	private String user_id;
	private String password;
	private String name;
	private String department;
	private String email;

	private String system_type;
	private int is_use;
	private int is_admin;

	private String last_login_date;
	private String access_token;

	private String reg_date;
	private int fail_count;

	public String getAccount_uid() {
		return account_uid;
	}

	public void setAccount_uid(String account_uid) {
		this.account_uid = account_uid;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSystem_type() {
		return system_type;
	}

	public void setSystem_type(String system_type) {
		this.system_type = system_type;
	}

	public int getIs_use() {
		return is_use;
	}

	public void setIs_use(int is_use) {
		this.is_use = is_use;
	}

	public int getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(int is_admin) {
		this.is_admin = is_admin;
	}

	public String getLast_login_date() {
		return last_login_date;
	}

	public void setLast_login_date(String last_login_date) {
		this.last_login_date = last_login_date;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getFail_count() {
		return fail_count;
	}

	public void setFail_count(int fail_count) {
		this.fail_count = fail_count;
	}

}

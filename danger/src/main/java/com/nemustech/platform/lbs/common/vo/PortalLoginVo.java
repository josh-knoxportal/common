package com.nemustech.platform.lbs.common.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PortalLoginVo {

	private String decryptEmpNo;
	private String decryptMail;
	private String decryptUserId;

	private String encryptUserId;

	private String emp_no;
	private String email;
	private String user_id;

	private String system_type;
	private String access_token;

	public String getDecryptEmpNo() {
		return decryptEmpNo;
	}

	public void setDecryptEmpNo(String decryptEmpNo) {
		this.decryptEmpNo = decryptEmpNo;
	}

	public String getDecryptMail() {
		return decryptMail;
	}

	public void setDecryptMail(String decryptMail) {
		this.decryptMail = decryptMail;
	}

	public String getDecryptUserId() {
		return decryptUserId;
	}

	public void setDecryptUserId(String decryptUserId) {
		this.decryptUserId = decryptUserId;
	}

	public String getEmp_no() {
		return emp_no;
	}

	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getSystem_type() {
		return system_type;
	}

	public void setSystem_type(String system_type) {
		this.system_type = system_type;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getEncryptUserId() {
		return encryptUserId;
	}

	public void setEncryptUserId(String encryptUserId) {
		this.encryptUserId = encryptUserId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

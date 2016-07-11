package org.oh.web.model;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;

/**
 * 공통 모델
 * 
 * @author skoh
 */
@Table
public class Common extends Default {
	public static final String DEFAULT_DATE_MYSQL = "DATE_FORMAT (NOW(), '%Y%m%d%H%i%s')";
	public static final String DEFAULT_DATE_ORACLE = "TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')";
	public static final String DEFAULT_DATE_SQLSERVER = "";

	/**
	 * 등록자 아이디
	 */
	@Column
	protected String reg_id;

	/**
	 * 등록 일시
	 */
	@Column
	protected String reg_dt;

	/**
	 * 수정자 아이디
	 */
	@Column
	protected String mod_id;

	/**
	 * 수정 일시
	 */
	@Column
	protected String mod_dt;

	public String getReg_id() {
		return reg_id;
	}

	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getMod_id() {
		return mod_id;
	}

	public void setMod_id(String mod_id) {
		this.mod_id = mod_id;
	}

	public String getMod_dt() {
		return mod_dt;
	}

	public void setMod_dt(String mod_dt) {
		this.mod_dt = mod_dt;
	}
}

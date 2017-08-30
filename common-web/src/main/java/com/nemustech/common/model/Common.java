package com.nemustech.common.model;

import org.mybatisorm.EntityManager;
import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.common.page.Paging;
import com.nemustech.common.service.CommonService;

/**
 * 공통 모델
 * 
 * @author skoh
 */
@Table
public class Common extends Paging {
	@Autowired
	protected EntityManager entityManager;

	/**
	 * 등록자 아이디
	 */
	@Column
	protected String reg_id;

	/**
	 * 등록 일시
	 */
	@Column(defaultValue = CommonService.DEFAULT_DATE_CHAR_ORACLE) // #{commonService.getDefaultDateValue()}")
	protected String reg_dt;

	/**
	 * 수정자 아이디
	 */
	@Column
	protected String mod_id;

	/**
	 * 수정 일시
	 */
	@Column(defaultValue = CommonService.DEFAULT_DATE_CHAR_ORACLE) // #{commonService.getDefaultDateValue()}", defaultUpdate = true)
	protected String mod_dt;

	public Common() {
	}

	public Common(String reg_id, String mod_id) {
		this(reg_id, null, mod_id, null);
	}

	public Common(String reg_id, String reg_dt, String mod_id, String mod_dt) {
		this.reg_id = reg_id;
		this.reg_dt = reg_dt;
		this.mod_id = mod_id;
		this.mod_dt = mod_dt;
	}

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

	/**
	 * 쓰기 전용 : 입력(write)만 가능
	 * 
	 * @author skoh
	 */
	@Table
	public class CommonWrite extends Common {
		@Override
		@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
		public String getReg_id() {
			return super.getReg_id();
		}

		@Override
		@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
		public String getReg_dt() {
			return super.getReg_dt();
		}

		@Override
		@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
		public String getMod_id() {
			return super.getMod_id();
		}

		@Override
		@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
		public String getMod_dt() {
			return super.getMod_dt();
		}
	}
}

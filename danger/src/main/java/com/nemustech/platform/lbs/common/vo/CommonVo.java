package com.nemustech.platform.lbs.common.vo;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.Table;

import com.nemustech.common.page.Paging;

/**
 * 공통 모델
 */
@Table
public class CommonVo extends Paging {
	@Column
	protected String reg_date;

	@Column
	protected String upd_date;

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
}

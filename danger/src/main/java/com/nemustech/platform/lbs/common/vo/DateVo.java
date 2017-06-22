// -*- Java -*-
/*  $Id: $  */
/*===========================================================================

	LoginVo.java

	$Author: sangho.lee $
	$URL: $
	$Revision: $
	$Date: $


	  Date: Oct 5, 2015 18:04:13
	Author: sangho.lee

===========================================================================*/
/*  $Source: $  */
/*  $Header: $  */
package com.nemustech.platform.lbs.common.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author wwweojin@gmail.com
 * @version r1 ($Id$)
 * @since r1
 */
public class DateVo {
	private Date reg_date;
	private Date upd_date;

	@ApiModelProperty(value = "reg_date")
	@JsonProperty("reg_date")
	public Date getReg_date() {
		return reg_date;
	}

	/**
	 * @param reg_date
	 *            the reg_date to set
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}

	@ApiModelProperty(value = "upd_date")
	@JsonProperty("upd_date")
	public Date getUpd_date() {
		return upd_date;
	}

	/**
	 * @param upd_date
	 *            the upd_date to set
	 */
	public void setUpd_date(Date upd_date) {
		this.upd_date = upd_date;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DateVo {");
		sb.append(" reg_date: \"").append(reg_date).append('"');
		sb.append(" upd_date: \"").append(upd_date).append('"');

		sb.append("}\n");
		return sb.toString();
	}

}

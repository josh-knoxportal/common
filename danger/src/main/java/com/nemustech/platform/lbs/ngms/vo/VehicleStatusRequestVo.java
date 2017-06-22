package com.nemustech.platform.lbs.ngms.vo;


import com.nemustech.platform.lbs.common.util.StringUtil;

import io.swagger.annotations.ApiModel;


/**
 * 검색 VO
 **/
@ApiModel(description = "차량상태 목록 검색")
public class VehicleStatusRequestVo  {
	private static final String DAYFORMAT = "YYYY-MM-DD";
	
	/* */
	private int    is_current = 0;
	private String from = "";
	private String to = "";
	private int    violation_type = 0;
	private String vehicle_no = "";

	/* */
	public String getVehicle_no() {
		return vehicle_no;
	}

	/* */
	public void setVehicle_no(String vehicle_no) {
		this.vehicle_no = vehicle_no;
	}

	public int getIs_current() {
		return is_current;
	}

	public void setIs_current(int is_current) {
		this.is_current = is_current;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		if (DAYFORMAT.length() == from.length()) {
			this.from = from;
		} else {
			this.from = "";
		}
	}

	public String getTo() {
		return to;
	}

	/* */
	public void setTo(String to) {
		if (DAYFORMAT.length() == to.length()) {
			this.to = to + " 23:59:59.999";
		} else {
			this.to = "";
		}
	}

	public int getViolation_type() {
		return violation_type;
	}

	public void setViolation_type(int violation_type) {
		this.violation_type = violation_type;
	}

	
	/* */
	public boolean isValid() {
		if (is_current == 0) {
			if (StringUtil.isEmpty(from) ||
					StringUtil.isEmpty(to)) {
				return false;
			}
		}
		
		return true;
	}
}
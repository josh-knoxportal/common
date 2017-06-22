package com.nemustech.platform.lbs.lpms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class LpmsWorkTypeVo {
	private String work_uid;
	private String work_type;

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}

	public String getWork_uid() {
		return work_uid;
	}

	public void setWork_uid(String work_uid) {
		this.work_uid = work_uid;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

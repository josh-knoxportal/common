package com.nemustech.platform.lbs.common.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 설정
 */
public class SettingVo {
	protected String name;

	protected String value;

	protected String time;

	public SettingVo(String name, String value, String time) {
		super();
		this.name = name;
		this.value = value;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}

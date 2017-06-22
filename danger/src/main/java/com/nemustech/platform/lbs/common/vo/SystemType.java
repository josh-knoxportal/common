package com.nemustech.platform.lbs.common.vo;

public enum SystemType {

	DANGER(1, "위험지역"), VEHICLE(2, "위험차량");

	private final int value;
	private final String label;

	private SystemType(int value, String label) {
		this.value = value;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

}

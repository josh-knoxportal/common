package com.nemustech.platform.lbs.lpms.vo;

public enum WorkType {

	FIRE("fire", "화기"), AIRTIGHT("airtight", "밀폐공간"), DANGERA("dangerA", "일반 위험A"), ELECTRICITY("electricity",
			"전기"), RADIATION("radiation", "방사선"), HEAVY("heavy",
					"중장비"), EXCAVATION("excavation", "굴착"), DANGERB("dangerB", "일반 위험B"), HEIGHT("height", "고소");
	private final String value;
	private final String label;

	private WorkType(String value, String label) {
		this.value = value;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}
}

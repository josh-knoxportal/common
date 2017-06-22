package com.nemustech.platform.lbs.common.tokenizer;

public class Device {
	private String device_id="";
	private String model;
	private String os; // ios or android
	private String os_version;
	private long expire_time;
	private long now;
	
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getOs_version() {
		return os_version;
	}
	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}
	public long getExpire_time() {
		return expire_time;
	}
	public void setExpire_time(long expire_time) {
		this.expire_time = expire_time;
	}
	public long getNow() {
		return now;
	}
	public void setNow(long now) {
		this.now = now;
	}
}

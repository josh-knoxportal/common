package com.nemustech.common.model;

import java.io.Serializable;
import java.util.Date;

import com.nemustech.common.util.StringUtil;

/**
 * 서버
 * 
 * @author skoh
 */
public class Server implements Serializable {
	/**
	 * IP
	 */
	protected String ip;

	/**
	 * Port
	 */
	protected int port;

	/**
	 * profile
	 */
	protected String profile;

	/**
	 * 최근 수행시간
	 */
	protected Date last_date;

	public Server() {
	}

	public Server(String ip, int port, String profile, Date last_date) {
		this.ip = ip;
		this.port = port;
		this.profile = profile;
		this.last_date = last_date;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Date getLast_date() {
		return last_date;
	}

	public void setLast_date(Date last_date) {
		this.last_date = last_date;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this);
	}
}
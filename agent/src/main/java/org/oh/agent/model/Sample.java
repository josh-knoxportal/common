package org.oh.agent.model;

import java.io.Serializable;

public class Sample implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userid;
	private String username;
	
	public Sample() {
	}

	public Sample(String userid, String username) {
		this.userid = userid;
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Sample [userid=" + userid + ", username=" + username + "]";
	}
}

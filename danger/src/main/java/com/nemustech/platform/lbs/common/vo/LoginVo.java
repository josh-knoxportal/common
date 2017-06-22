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

/**
 * 
 * @author wwweojin@gmail.com
 * @version r1 ($Id$)
 * @since r1
 */
public class LoginVo {
	private String system_type;
	private String user_id;
	private String password;
	private String access_token;
	
	
	/**
	 * Default constructor for JSON.
	 */
	public LoginVo() {}
	
	public LoginVo(String user_id, String password) {
		this.user_id = user_id;
		this.password = password;
	}
	
	public LoginVo(String system_type, String user_id, String password) {
		this.system_type = system_type;
		this.user_id = user_id;
		this.password = password;
	}


	/**
	 * @return the system_type
	 */
	public String getSystem_type() {
		return system_type;
	}

	/**
	 * @param system_type the system_type to set
	 */
	public void setSystem_type(String system_type) {
		this.system_type = system_type;
	}

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class LoginVo {");
		sb.append(" system_type: \"").append(system_type).append('"');
		sb.append(" user_id: \"").append(user_id).append('"');
		sb.append(",password: \"").append(password).append('"');
		sb.append(",access_token: \"").append(access_token).append('"');
		
		sb.append("}\n");
		return sb.toString();
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}


	
}

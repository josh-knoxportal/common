package com.nemustech.platform.lbs.common.vo;


import java.util.List;

import io.swagger.annotations.ApiModel;



/**
 * 검색 VO
 **/
@ApiModel(description = "Push 보내기 VO 정보")
public class PushDeviceNoSendVo  {
	
	private List<String> phoneList ;
	private String msg ;
	private String user_id ;
	/**
	 * @return the device_no
	 */
	public List<String> getPhoneList() {
		return phoneList;
	}
	/**
	 * @param device_no the device_no to set
	 */
	public void setPhoneList(List<String> phoneList) {
		this.phoneList = phoneList;
	}
	
	
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * @return the usr_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param usr_id the usr_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class "+this.getClass().getName()+" {\n");
		sb.append("  user_id: ").append(user_id).append("\n");
		sb.append(" ,phoneList: ").append(phoneList).append("\n");
		sb.append(" ,msg: ").append(msg).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

	
	
	
}

package com.nemustech.platform.lbs.common.vo;


import java.util.List;

import io.swagger.annotations.ApiModel;



/**
 * 검색 VO
 **/
@ApiModel(description = "Push 이력관리 VO 정보")
public class PushHistoryVo  {
	
	private String push_uid ;
	private String system_type ;
	private String action;
	private String user_id ;
	private String message;
	
	private List<String> targetList;
	
	/**
	 * @return the targetList
	 */
	public List<String> getTargetList() {
		return targetList;
	}
	/**
	 * @param targetList the targetList to set
	 */
	public void setTargetList(List<String> targetList) {
		this.targetList = targetList;
	}
	/**
	 * @return the push_uid
	 */
	public String getPush_uid() {
		return push_uid;
	}
	/**
	 * @param push_uid the push_uid to set
	 */
	public void setPush_uid(String push_uid) {
		this.push_uid = push_uid;
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class "+this.getClass().getName()+" {\n");
		sb.append("  push_uid: ").append(push_uid).append("\n");
		sb.append(" ,system_type: ").append(system_type).append("\n");
		sb.append(" ,action: ").append(action).append("\n");
		sb.append(" ,user_id: ").append(user_id).append("\n");
		sb.append(" ,message: ").append(message).append("\n");
		sb.append(" ,targetList: ").append(targetList).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
	

	
	
	
}

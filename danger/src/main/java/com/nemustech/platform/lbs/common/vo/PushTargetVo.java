package com.nemustech.platform.lbs.common.vo;


import io.swagger.annotations.ApiModel;



/**
 * 검색 VO
 **/
@ApiModel(description = "Push 이력관리 VO 정보")
public class PushTargetVo  {
	
	private String push_uid ;
	private String device_no ;
	private String status ;
	/**
	 * @return the device_no
	 */
	public String getDevice_no() {
		return device_no;
	}
	/**
	 * @param device_no the device_no to set
	 */
	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class "+this.getClass().getName()+" {\n");
		sb.append("  push_uid: ").append(push_uid).append("\n");
		sb.append(" ,device_no: ").append(device_no).append("\n");
		sb.append(" ,status: ").append(status).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

	
	
	
}

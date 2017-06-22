package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "APP GPS /BLE on/off 이벤트 등록 VO")
public class AppGpsBleEventVo  {
	
	private String 	work_uid;
	private String 	type;
	private int 	enable;
	private long 	time;

	



	@ApiModelProperty(value ="work_uid")
	@JsonProperty("work_uid")
	public String getWork_uid() {
		return work_uid;
	}

	/**
	 * @param work_uid the work_uid to set
	 */
	public void setWork_uid(String work_uid) {
		this.work_uid = work_uid;
	}

	@ApiModelProperty(value ="type")
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	@ApiModelProperty(value ="enable")
	@JsonProperty("enable")
	public int getEnable() {
		return enable;
	}

	/**
	 * @param enable the enable to set
	 */
	public void setEnable(int enable) {
		this.enable = enable;
	}

		
	@ApiModelProperty(value ="time")
	@JsonProperty("time")
	public long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}



	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class AppGpsBleEventVo {\n");
		sb.append(" ,work_uid: ").append(work_uid).append("\n");
		sb.append(" ,type: ").append(type).append("\n");
		sb.append(" ,enable: ").append(enable).append("\n");
		sb.append(" ,time: ").append(time).append("\n");

		
		sb.append("}\n");
		return sb.toString();
	}

	

	
}

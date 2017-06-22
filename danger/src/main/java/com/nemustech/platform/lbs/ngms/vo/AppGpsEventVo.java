package com.nemustech.platform.lbs.ngms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "APP GPS on/off 이벤트 등록 VO")
public class AppGpsEventVo  {
	
	private String 	vehicle_uid;
	private String  gps;
	private long 	time;

	



	@ApiModelProperty(value ="vehicle_uid")
	@JsonProperty("vehicle_uid")
	public String getVehicle_uid() {
		return vehicle_uid;
	}

	public void setVehicle_uid(String vehicle_uid) {
		this.vehicle_uid = vehicle_uid;
	}
	
	
	
	@ApiModelProperty(value ="gps on/off")
	@JsonProperty("gps")
	public String getGps() {
		return gps;
	}

	/**
	 * @param gps the gps to set
	 */
	public void setGps(String gps) {
		if("disabled".equals(gps))
			gps = "0";
		else
			gps = "1";
		
		this.gps = gps;
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
		sb.append("class AppGpsEventVo {\n");
		sb.append(" ,gps: ").append(gps).append("\n");
		sb.append(" ,time: ").append(time).append("\n");

		
		sb.append("}\n");
		return sb.toString();
	}

	

	
}

package com.nemustech.platform.lbs.ngms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "APP 차량위반 이벤트 등록 VO")
public class AppViolationEventVo  {
	

	private String  event;
	private String 	vehicle_uid;
	private String  point;
	private String  begin_road;
	private String  end_road;
	
	private long 	begin_time;
	private long 	end_time;
	
	private float 	speed;
	private float 	max;
	private float	avg;
	



/*
	@ApiModelProperty(value ="")
	@JsonProperty("xxxx")
	0 : unknown (null)\n1 : 속도위반\n2: 출입제한구역
 */
	@ApiModelProperty(value ="violation event")
	@JsonProperty("event")
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		
		if("speeding".equals(event))
			this.event = "1" ;
		else if("restricted".equals(event))
			this.event = "2" ;
		else
			this.event = "0";
	}

	@ApiModelProperty(value ="vehicle_uid")
	@JsonProperty("vehicle_uid")
	public String getVehicle_uid() {
		return vehicle_uid;
	}

	public void setVehicle_uid(String vehicle_uid) {
		this.vehicle_uid = vehicle_uid;
	}
	

	@ApiModelProperty(value ="point")
	@JsonProperty("point")
	public String getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(String point) {
		this.point = point;
	}

	
	

	@ApiModelProperty(value ="begin road")
	@JsonProperty("begin_road")
	public String getBegin_road() {
		return begin_road;
	}

	/**
	 * @param begin_road the begin_road to set
	 */
	public void setBegin_road(String begin_road) {
		this.begin_road = begin_road;
	}

	@ApiModelProperty(value ="end road")
	@JsonProperty("end_road")
	public String getEnd_road() {
		if(end_road == null || "".equals(end_road))
			end_road = begin_road;
		return end_road;
	}


	@ApiModelProperty(value ="begin_time")
	@JsonProperty("begin_time")
	public long getBegin_time() {
		return begin_time;
	}

	/**
	 * @param begin_time the begin_time to set
	 */
	public void setBegin_time(long begin_time) {
		this.begin_time = begin_time;
	}

	@ApiModelProperty(value ="end_time")
	@JsonProperty("end_time")
	public long getEnd_time() {
		if(end_time == 0)
			end_time = begin_time;
		return end_time;
	}

	
	/**
	 * @param end_time the end_time to set
	 */
	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	@ApiModelProperty(value ="speed")
	@JsonProperty("speed")
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}


	@ApiModelProperty(value ="avg")
	@JsonProperty("avg")
	public float getAvg() {
		if(avg == 0)
			avg = speed;
		
		return avg;
	}

	/**
	 * @param avg the avg to set
	 */
	public void setAvg(float avg) {
		this.avg = avg;
	}
	
	@ApiModelProperty(value ="max")
	@JsonProperty("max")
	public float getMax() {
		if(max == 0)
			max = speed;
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}



	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class ViolationHistoryVo {\n");
		sb.append(" ,point: ").append(point).append("\n");
		sb.append(" ,begin_road: ").append(begin_road).append("\n");
		sb.append(" ,end_road: ").append(end_road).append("\n");
		sb.append(" ,begin_time: ").append(begin_time).append("\n");
		sb.append(" ,end_time: ").append(end_time).append("\n");
		sb.append(" ,speed: ").append(speed).append("\n");
		sb.append(" ,avg: ").append(avg).append("\n");
		sb.append(" ,max: ").append(max).append("\n");
		
		sb.append("}\n");
		return sb.toString();
	}

	

	
}

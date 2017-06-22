package com.nemustech.platform.lbs.ngms.vo;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 네이게이션 차량등록정보 VO
 **/
@ApiModel(description = "차선 속도제한 VO")
public class LineLimitVo  {

	private int line;
	private int speed_limit;
	private float threshold;	
	private Date upd_date;

	
	@ApiModelProperty(value ="수정일시")
	@JsonProperty("upd_date")
	public Date getUpd_date() {
		return upd_date;
	}

	/**
	 * @param upd_date the upd_date to set
	 */
	public void setUpd_date(Date upd_date) {
		this.upd_date = upd_date;
	}

	@ApiModelProperty(value ="차선")
	@JsonProperty("line")
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
	
	@ApiModelProperty(value ="차선별 속도제한")
	@JsonProperty("speed_limit")
	public int getSpeed_limit() {
		return speed_limit;
	}

	public void setSpeed_limit(int speed_limit) {
		this.speed_limit = speed_limit;
	}
	
	@ApiModelProperty(value ="속도 임계치 persentage")
	@JsonProperty("threshold")
	public float getThreshold() {
		return threshold;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class DeviceVo {\n");
		sb.append("  line: ").append(line).append("\n");
		sb.append(" ,speed_limit: ").append(speed_limit).append("\n");
		sb.append(" ,threshold: ").append(threshold).append("\n");
		
		sb.append("}\n");
		return sb.toString();
	}

	

	
	
	

	



}

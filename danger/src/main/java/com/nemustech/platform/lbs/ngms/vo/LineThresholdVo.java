package com.nemustech.platform.lbs.ngms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 네이게이션 차량등록정보 VO
 **/
@ApiModel(description = "차선 속도제한 VO")
public class LineThresholdVo  {

	private int line;
	private float size;

	
	@ApiModelProperty(value ="차선")
	@JsonProperty("line")
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
	
	
	
	@ApiModelProperty(value ="속도 임계치 persentage")
	@JsonProperty("size")
	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class DeviceVo {\n");
		sb.append("  line: ").append(line).append("\n");
		sb.append(" ,size: ").append(size).append("\n");
		
		sb.append("}\n");
		return sb.toString();
	}

	



}

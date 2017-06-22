package com.nemustech.platform.lbs.ngms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 **/
@ApiModel(description = "Road Section VO")
public class RoadSectionVo {
	private String section_uid;
	private int line;
	private int oneway_type;
	private int speed_limit;
	private int is_restrict_area;
	private float x1;
	private float y1;
	private float x2;
	private float y2;
	private String restrict_area; // 160804 [개인폰] [추가] 차량유형별 제한도로

	/*
	 * @ApiModelProperty(value ="")
	 * 
	 * @JsonProperty("xxxx")
	 */

	@ApiModelProperty(value = "")
	@JsonProperty("section_uid")
	public String getSection_uid() {
		return section_uid;
	}

	public void setSection_uid(String section_uid) {
		this.section_uid = section_uid;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("line")
	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("oneway_type")
	public int getOneway_type() {
		return oneway_type;
	}

	public void setOneway_type(int oneway_type) {
		this.oneway_type = oneway_type;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("speed_limit")
	public int getSpeed_limit() {
		return speed_limit;
	}

	public void setSpeed_limit(int speed_limit) {
		this.speed_limit = speed_limit;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("is_restrict_area")
	public int getIs_restrict_area() {
		return is_restrict_area;
	}

	public void setIs_restrict_area(int is_restrict_area) {
		this.is_restrict_area = is_restrict_area;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("x1")
	public float getX1() {
		return x1;
	}

	public void setX1(float x1) {
		this.x1 = x1;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("y1")
	public float getY1() {
		return y1;
	}

	public void setY1(float y1) {
		this.y1 = y1;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("x2")
	public float getX2() {
		return x2;
	}

	public void setX2(float x2) {
		this.x2 = x2;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("y2")
	public float getY2() {
		return y2;
	}

	public void setY2(float y2) {
		this.y2 = y2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class RoadSectionVo {\n");
		sb.append("  section_uid: ").append(section_uid).append("\n");
		sb.append(" ,line: ").append(line).append("\n");
		sb.append(" ,oneway_type: ").append(oneway_type).append("\n");
		sb.append(" ,speed_limit: ").append(speed_limit).append("\n");
		sb.append(" ,is_restrict_area: ").append(is_restrict_area).append("\n");
		sb.append(" ,x1: ").append(x1).append("\n");
		sb.append(" ,y1: ").append(y1).append("\n");
		sb.append(" ,x2: ").append(x2).append("\n");
		sb.append(" ,y2: ").append(y2).append("\n");

		sb.append("}\n");
		return sb.toString();
	}

	@ApiModelProperty(value = "restrict_area")
	@JsonProperty("restrict_area")
	public String getRestrict_area() {
		return restrict_area;
	}

	public void setRestrict_area(String restrict_area) {
		this.restrict_area = restrict_area;
	}

}

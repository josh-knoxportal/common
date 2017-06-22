package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 작업유형별 작업 현황
 **/
@ApiModel(description = "작업유형별 작업 현황")
public class MapWorkStatusOnWorkTypeVo  {
	private String work_type = "";
	private String work_type_name = "";
	
	/* 작업자 */
	private int	   work_cnt = 0;
	private int	   people_cnt = 0;
	
	/* 작업 */
	private int 	work_completed_cnt = 0;
	private int 	work_working_cnt = 0;
	private int 	people_completed_cnt = 0;
	private int		people_working_cnt = 0;
	
	
	@ApiModelProperty(value ="work_type")
	@JsonProperty("work_type")
	public String getWork_type() {
		return work_type;
	}
	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}
	
	
	@ApiModelProperty(value ="work_type_name")
	@JsonProperty("work_type_name")
	public String getWork_type_name() {
		return work_type_name;
	}
	public void setWork_type_name(String work_type_name) {
		this.work_type_name = work_type_name;
	}
	
	
	@ApiModelProperty(value ="people_working_cnt")
	@JsonProperty("people_working_cnt")
	public int getPeople_working_cnt() {
		return people_working_cnt;
	}
	public void setPeople_working_cnt(int people_working_cnt) {
		this.people_working_cnt = people_working_cnt;
	}
	
	
	@ApiModelProperty(value ="people_completed_cnt")
	@JsonProperty("people_completed_cnt")
	public int getPeople_completed_cnt() {
		return people_completed_cnt;
	}
	public void setPeople_completed_cnt(int people_completed_cnt) {
		this.people_completed_cnt = people_completed_cnt;
	}
	
	
	@ApiModelProperty(value ="work_working_cnt")
	@JsonProperty("work_working_cnt")
	public int getWork_working_cnt() {
		return work_working_cnt;
	}
	public void setWork_working_cnt(int work_working_cnt) {
		this.work_working_cnt = work_working_cnt;
	}
	
	
	@ApiModelProperty(value ="work_completed_cnt")
	@JsonProperty("work_completed_cnt")
	public int getWork_completed_cnt() {
		return work_completed_cnt;
	}
	public void setWork_completed_cnt(int work_completed_cnt) {
		this.work_completed_cnt = work_completed_cnt;
	}
	
	
	@ApiModelProperty(value ="work_cnt")
	@JsonProperty("work_cnt")
	public int getWork_cnt() {
		return work_cnt;
	}
	public void setWork_cnt(int work_cnt) {
		this.work_cnt = work_cnt;
	}
	
	
	@ApiModelProperty(value ="people_cnt")
	@JsonProperty("people_cnt")
	public int getPeople_cnt() {
		return people_cnt;
	}
	public void setPeople_cnt(int people_cnt) {
		this.people_cnt = people_cnt;
	}
	
	
	
}
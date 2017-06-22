package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 단위공장별 작업 현황
 **/
@ApiModel(description = "단위공장별 작업 현황")
public class MapWorkStatusOnFactoriesVo  {
	private String 	factory_uid = "";
	private String 	factory_name = "";
	
	/* 작업자 */
	private int	   	people_authorized_cnt = 0;
	private int	   	people_unauthorized_cnt = 0;
	private int	   	work_cnt = 0;
	
	/* 작업 */
	private int 	work_completed_cnt = 0;
	private int 	work_working_cnt = 0;
	private int 	people_completed_cnt = 0;
	private int		people_working_cnt = 0;
	
	
	@ApiModelProperty(value ="factory_uid")
	@JsonProperty("factory_uid")
	public String getFactory_uid() {
		return factory_uid;
	}
	public void setFactory_uid(String factory_uid) {
		this.factory_uid = factory_uid;
	}
	
	
	@ApiModelProperty(value ="factory_name")
	@JsonProperty("factory_name")
	public String getFactory_name() {
		return factory_name;
	}
	public void setFactory_name(String factory_name) {
		this.factory_name = factory_name;
	}
	
	
	@ApiModelProperty(value ="people_authorized_cnt")
	@JsonProperty("people_authorized_cnt")
	public int getPeople_authorized_cnt() {
		return people_authorized_cnt;
	}
	public void setPeople_authorized_cnt(int people_authorized_cnt) {
		this.people_authorized_cnt = people_authorized_cnt;
	}
	
	
	@ApiModelProperty(value ="people_unauthorized_cnt")
	@JsonProperty("people_unauthorized_cnt")
	public int getPeople_unauthorized_cnt() {
		return people_unauthorized_cnt;
	}
	public void setPeople_unauthorized_cnt(int people_unauthorized_cnt) {
		this.people_unauthorized_cnt = people_unauthorized_cnt;
	}
	
	@ApiModelProperty(value ="work_cnt")
	@JsonProperty("work_cnt")
	public int getWork_cnt() {
		return work_cnt;
	}
	public void setWork_cnt(int work_cnt) {
		this.work_cnt = work_cnt;
	}
	
	/* [2016/08/19] added */
	@ApiModelProperty(value ="work_completed_cnt")
	@JsonProperty("work_completed_cnt")
	public int getWork_completed_cnt() {
		return work_completed_cnt;
	}
	public void setWork_completed_cnt(int work_completed_cnt) {
		this.work_completed_cnt = work_completed_cnt;
	}
	
	@ApiModelProperty(value ="work_working_cnt")
	@JsonProperty("work_working_cnt")
	public int getWork_working_cnt() {
		return work_working_cnt;
	}
	public void setWork_working_cnt(int work_working_cnt) {
		this.work_working_cnt = work_working_cnt;
	}
	
	
	@ApiModelProperty(value ="people_completed_cnt")
	@JsonProperty("people_completed_cnt")
	public int getPeople_completed_cnt() {
		return people_completed_cnt;
	}
	public void setPeople_completed_cnt(int people_completed_cnt) {
		this.people_completed_cnt = people_completed_cnt;
	}
	
	
	@ApiModelProperty(value ="people_working_cnt")
	@JsonProperty("people_working_cnt")
	public int getPeople_working_cnt() {
		return people_working_cnt;
	}
	public void setPeople_working_cnt(int people_working_cnt) {
		this.people_working_cnt = people_working_cnt;
	}
	
	
	
	
	
}
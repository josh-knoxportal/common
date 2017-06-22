package com.nemustech.platform.lbs.wwms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 **/
@ApiModel(description = "작업번호 VO")
public class CodeWorkNoVo {
	private String work_uid = "";
	private String work_no = "";

	// 160803 [개인폰] [수정] 작업번호조회 API 작업명 추가
	private String work_name = "";
	private String work_zone_name = "";
	private String work_type_name = "";

	@ApiModelProperty(value = "work_uid")
	@JsonProperty("work_uid")
	public String getWork_uid() {
		return work_uid;
	}

	/**
	 * @param work_uid
	 *            the work_uid to set
	 */
	public void setWork_uid(String work_uid) {
		this.work_uid = work_uid;
	}

	@ApiModelProperty(value = "work_no")
	@JsonProperty("work_no")
	public String getWork_no() {
		return work_no;
	}

	/**
	 * @param work_num
	 *            the work_num to set
	 */
	public void setWork_no(String work_no) {
		this.work_no = work_no;
	}

	@ApiModelProperty(value = "work_name")
	@JsonProperty("work_name")
	public String getWork_name() {
		return work_name;
	}

	public void setWork_name(String work_name) {
		this.work_name = work_name;
	}

	@ApiModelProperty(value = "work_zone_name")
	@JsonProperty("work_zone_name")
	public String getWork_zone_name() {
		return work_zone_name;
	}

	public void setWork_zone_name(String work_zone_name) {
		this.work_zone_name = work_zone_name;
	}

	@ApiModelProperty(value = "work_type_name")
	@JsonProperty("work_type_name")
	public String getWork_type_name() {
		return work_type_name;
	}

	public void setWork_type_name(String work_type_name) {
		this.work_type_name = work_type_name;
	}

}

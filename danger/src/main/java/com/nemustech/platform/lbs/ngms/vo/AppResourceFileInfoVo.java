package com.nemustech.platform.lbs.ngms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] APP 도로 지도 리소스 정보 VO
 */
@ApiModel(description = "APP 도로 지도 리소스 정보 VO")
public class AppResourceFileInfoVo {

	private String file_name;
	private String file_type;

	@ApiModelProperty(value = "file_type")
	@JsonProperty("file_type")
	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	@ApiModelProperty(value = "file_name")
	@JsonProperty("file_name")
	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

}

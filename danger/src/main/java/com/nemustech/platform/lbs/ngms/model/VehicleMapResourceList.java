package com.nemustech.platform.lbs.ngms.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.AppResourceFileInfoVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 도로 지도 리소스 정보 List
 */
@ApiModel(description = "도로 지도 리소스 정보 List VO")
public class VehicleMapResourceList extends ResponseData {

	private String resource_path;
	private List<AppResourceFileInfoVo> appResourceFileInfoVo;

	@ApiModelProperty(value = "resource_path")
	@JsonProperty("resource_path")
	public void setResource_path(String resource_path) {
		this.resource_path = resource_path;
	}

	public String getResource_path() {
		return resource_path;
	}

	@ApiModelProperty(value = "file_list")
	@JsonProperty("file_list")
	public List<AppResourceFileInfoVo> getAppResourceFileInfoVo() {
		return appResourceFileInfoVo;
	}

	public void setAppResourceFileInfoVo(List<AppResourceFileInfoVo> appResourceFileInfoVo) {
		this.appResourceFileInfoVo = appResourceFileInfoVo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

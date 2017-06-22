package com.nemustech.platform.lbs.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 생성된 upload image 정보
 **/
@ApiModel(description = "생성된 upload file 정보")
public class UploadFileVo  {

	private String file_url = "";

	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("file_url")
	public String getFile_url() {
		return file_url;
	}
	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class UploadFile {\n");

		sb.append("  file_url: ").append(file_url).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJsonObj() {
		JSONObject jobj = new JSONObject();
		
		jobj.put("file_url", this.getFile_url());
		
		return jobj;
	}
}

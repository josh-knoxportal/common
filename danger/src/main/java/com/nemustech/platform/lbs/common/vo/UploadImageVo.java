package com.nemustech.platform.lbs.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 생성된 upload image 정보
 **/
@ApiModel(description = "생성된 upload image 정보")
public class UploadImageVo extends DefaultVo {

	private static final long serialVersionUID = -4896459194567883072L;
	private String image_url = "";

	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("image_url")
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class UploadImage {\n");

		sb.append("  cimage_url: ").append(image_url).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJsonObj() {
		JSONObject jobj = new JSONObject();
		
		jobj.put("image_url", this.getImage_url());
		
		return jobj;
	}
}

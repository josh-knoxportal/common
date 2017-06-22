package com.nemustech.platform.lbs.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "로그인 리턴정보 VO")
public class ResponseLogin extends ResponseData {

	private ResponseLoginVo responseLoginVo;
	private String forward_url;

	@ApiModelProperty(value = "로그인 리턴정보")
	@JsonProperty("responseLoginVo")
	public ResponseLoginVo getResponseLoginVo() {
		return responseLoginVo;
	}

	public void setResponseLoginVo(ResponseLoginVo responseLoginVo) {
		this.responseLoginVo = responseLoginVo;
	}

	@ApiModelProperty(value = "로그인 성공후 이동할 페이지 경로")
	@JsonProperty("forward_url")
	public String getForward_url() {
		return forward_url;
	}

	public void setForward_url(String forward_url) {
		this.forward_url = forward_url;
	}
}

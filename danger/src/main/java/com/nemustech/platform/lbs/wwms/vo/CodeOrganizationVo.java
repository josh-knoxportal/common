package com.nemustech.platform.lbs.wwms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * 160804 [개인폰] [신규] 부서/협력업체 정보
 */
@ApiModel(description = "부서/협력업체 코드 VO")
public class CodeOrganizationVo {

	private String code = "";
	private String name = "";
	private String type = "";
	private String type_name = "";
	private int order_no = 0;

	@ApiModelProperty(value = "code")
	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ApiModelProperty(value = "name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(value = "type")
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ApiModelProperty(value = "type_name")
	@JsonProperty("type_name")
	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	@ApiModelProperty(value = "order_no")
	@JsonProperty("order_no")
	public int getOrder_no() {
		return order_no;
	}

	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

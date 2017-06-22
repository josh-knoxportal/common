package com.nemustech.platform.lbs.ngms.model;



import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.LineThresholdVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 차량등록정보 목록
 **/
@ApiModel(description = "맵부가정보 다운로드")
public class NaviExtra extends ResponseData{

	private List<LineThresholdVo> line_size_threshold;
	 /**
	   * 차량등록정보 정보
	   **/
	@ApiModelProperty(value = "도로크기 임계치 ")
	@JsonProperty("line_size_threshold")
	public List<LineThresholdVo> getline_size_threshold() {
		return line_size_threshold;
	}

	public void setLine_size_threshold(List<LineThresholdVo> limit) {
		this.line_size_threshold = limit;
	}

	

	

	
	

	



}

package com.nemustech.platform.lbs.wwms.vo;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 위험지역 단위공장 설정
 **/
@ApiModel(description = "위험지역 단위공장 설정")
public class MapFactoryRestrictUpdateRequestVo  {	
	private String		 factory_uids = "";
	/* local */
	private List<String> factoryList;
	
	public List<String> getFadctoryList() {
		return factoryList;
	}
	public void setFactoryList(List<String> factoryList) {
		this.factoryList = factoryList;
	}
	

	
	@ApiModelProperty(value ="위험지역 단위공장 식별자 리스트( , 구분)")
	@JsonProperty("factory_uids")
	public String getFactory_uids() {
		return factory_uids;
	}
	public void setFactory_uids(String factory_uids) {
		this.factoryList = new ArrayList<String>();
		this.factory_uids = factory_uids;
		String[] arrUids = factory_uids.split(",");		
		if (arrUids != null) {
			for(int i = 0; i < arrUids.length; i++) {
				this.factoryList.add(arrUids[i]);
			}
		}
	}	
	
	/* */
	public boolean isEmpty() {
		if (this.factoryList == null || this.factoryList.size() == 0) return true;
		return false;
	}
	
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class SpeedLimitRequestVo {\n");
		sb.append("  factory_uids: ").append(factory_uids).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
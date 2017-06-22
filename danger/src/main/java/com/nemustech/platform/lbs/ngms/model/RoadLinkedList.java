package com.nemustech.platform.lbs.ngms.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.RoadLinkedVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 네이게이션맵 정보
 **/
@ApiModel(description = "네이게이션맵 정보")
public class RoadLinkedList extends ResponseData{
	
	private List<RoadLinkedVo> connections;
	
		
	@ApiModelProperty(value = "")
	@JsonProperty("count")
	public int getCount(){
		if(connections != null)
			return connections.size();
		else
			return 0;
	}

	@ApiModelProperty(value = "도로연결정보를 가져온다")
	@JsonProperty("connections")
	public List<RoadLinkedVo> getConnections() {
		return connections;
	}

	
	public void setConnections(List<RoadLinkedVo> connections) {
		this.connections = connections;
	}
	
	
	
	

}

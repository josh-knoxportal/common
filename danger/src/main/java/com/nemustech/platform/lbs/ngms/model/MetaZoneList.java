package com.nemustech.platform.lbs.ngms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;
import com.nemustech.platform.lbs.ngms.vo.MetaZoneVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * // 160804 [개인폰] [추가] [안전운행 16] Meta MAP GPS Zone 정보 조회
 **/
@ApiModel(description = "MetaZoneList")
public class MetaZoneList extends ResponseData {

	private List<MetaZoneVo> list;

	@ApiModelProperty(value = "totalCnt")
	@JsonProperty("totalCnt")
	public int getCount() {
		if (list != null)
			return list.size();
		else
			return 0;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("meta_zones")
	public List<MetaZoneVo> getList() {
		return list;
	}

	public void setList(List<MetaZoneVo> list) {
		this.list = list;
	}
}

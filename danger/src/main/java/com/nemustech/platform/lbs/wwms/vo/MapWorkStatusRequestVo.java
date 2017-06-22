package com.nemustech.platform.lbs.wwms.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.util.StringUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 검색 VO
 **/
@ApiModel(description = "위험지역별 작업자 현황 요청 파라미터")
public class MapWorkStatusRequestVo  {
	private static String TYPE_ALL 		= "all";
	private static String TYPE_FACTORY  = "factory";
	private static String TYPE_ZONE		= "zone";
	private static String TYPE_WORKTYPE	= "worktype";
	
	/* */
	private String type 		= "";
	private String uid 			= "";
	private String work_type 	= "";
	
	@ApiModelProperty(value = "type")
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	@ApiModelProperty(value = "uid")
	@JsonProperty("uid")
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	
	@ApiModelProperty(value = "work_type")
	@JsonProperty("work_type")
	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}
	
	
	/* */
	public boolean isValid() {	
		if (!TYPE_ALL.equals(this.type) &&
				!TYPE_FACTORY.equals(this.type) &&
				!TYPE_ZONE.equals(this.type)) return false;
		if (TYPE_ALL.equals(this.type)) {
			this.uid = "";
		} else {
			if (StringUtil.isEmpty(uid)) return false;
		}
		
		return true;
	}
	
	
	/* */
	public boolean isOnFactory() {
		if (TYPE_ALL.equals(this.type) || TYPE_FACTORY.equals(this.type)) return true;		
		return false;
	}
	
	public boolean isOnZone() {
		if (TYPE_ALL.equals(this.type) || TYPE_ZONE.equals(this.type)) return true;		
		return false;
	}
	
	/* */
	public String showType() {
		if (TYPE_FACTORY.equals(this.type)) return TYPE_FACTORY;
		if (TYPE_ZONE.equals(this.type)) return TYPE_ZONE;

		/* */
		if (StringUtil.isEmpty(this.work_type)) return TYPE_ALL;
		
		return TYPE_WORKTYPE;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("class "+this.getClass().getName()+" {\n");
		sb.append("  type: ").append(type).append("\n");
		sb.append(" ,uid: ").append(uid).append("\n");
		sb.append(" ,work_type: ").append(work_type).append("\n");
		sb.append("}\n");
		
		return sb.toString();
	}
}
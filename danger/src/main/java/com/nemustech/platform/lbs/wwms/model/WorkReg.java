package com.nemustech.platform.lbs.wwms.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.ResponseData;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 **/
@ApiModel(description = "Work Reg Response")
public class WorkReg extends ResponseData{
	
	private String work_uid;
	private String zone_type;
	private String zone_id;
	private String beacon_zone_id;
	
	

	@ApiModelProperty(value = "zone_type")
	@JsonProperty("zone_type")
	public String getZone_type() {
		return zone_type;
	}

	/**
	 * @param zone_type the zone_type to set
	 */
	public void setZone_type(String zone_type) {
		this.zone_type = zone_type;
	}

	@ApiModelProperty(value = "zone_id")
	@JsonProperty("zone_id")
	public String getZone_id() {
		return zone_id;
	}

	/**
	 * @param zone_id the zone_id to set
	 */
	public void setZone_id(String zone_id) {
		this.zone_id = zone_id;
	}

	@ApiModelProperty(value = "work_uid")
	@JsonProperty("work_uid")	
	public String getWork_uid() {
		return work_uid;
	}

	public void setWork_uid(String work_uid) {
		this.work_uid = work_uid;
	}

	@ApiModelProperty(value = "beacon_zone_id")
	@JsonProperty("beacon_zone_id")		
	public String getBeacon_zone_id() {
		return beacon_zone_id;
	}

	public void setBeacon_zone_id(String beacon_zone_id) {
		this.beacon_zone_id = beacon_zone_id;
	}

	
}

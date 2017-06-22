package com.nemustech.platform.lbs.common.vo;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nemustech.platform.lbs.common.model.Coordinate;
import com.nemustech.platform.lbs.common.model.CoordinateOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 네이게이션맵 VO
 **/
@ApiModel(description = " 공자 Map VO")
public class MapVo  {

	private String map_uid;
	
	private double latitude1 ;
	private double longitude1;
	
	private double latitude2 ;
	private double longitude2;
	
	private double latitude3 ;
	private double longitude3;
	
	private double latitude4 ;
	private double longitude4;
	
	private double center_latitude;
	private double center_longitude;
	
	private int width;
	private int height;
	
	private Date reg_date;
	private Date upd_date;
	
	@ApiModelProperty(value ="center_latitude")
	@JsonProperty("center_latitude")	
	public double getCenter_latitude() {
		return center_latitude;
	}
	
	
	public void setCenter_latitude(double center_latitude) {
		this.center_latitude = center_latitude;
	}
	
	@ApiModelProperty(value ="center_longitude")
	@JsonProperty("center_longitude")	
	public double getCenter_longitude() {
		return center_longitude;
	}

	public void setCenter_longitude(double center_longitude) {
		this.center_longitude = center_longitude;
	}
	
	@ApiModelProperty(value ="width")
	@JsonProperty("width")	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	@ApiModelProperty(value ="height")
	@JsonProperty("height")	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@ApiModelProperty(value = "네이게이션맵 uid")
	@JsonProperty("map_uid")
	public String getMap_uid() {
		return map_uid;
	}

	public void setMap_uid(String map_uid) {
		this.map_uid = map_uid;
	}
	
	
	@ApiModelProperty(value ="latitude1")
	@JsonProperty("latitude1")	
	public double getLatitude1() {
		return latitude1;
	}

	public void setLatitude1(double latitude1) {
		this.latitude1 = latitude1;
	}
	
	@ApiModelProperty(value ="longitude1")
	@JsonProperty("longitude1")	
	public double getLongitude1() {
		return longitude1;
	}

	public void setLongitude1(double longitude1) {
		this.longitude1 = longitude1;
	}
	
	@ApiModelProperty(value ="latitude2")
	@JsonProperty("latitude2")	
	public double getLatitude2() {
		return latitude2;
	}

	public void setLatitude2(double latitude2) {
		this.latitude2 = latitude2;
	}
	
	@ApiModelProperty(value ="longitude2")
	@JsonProperty("longitude2")	
	public double getLongitude2() {
		return longitude2;
	}

	public void setLongitude2(double longitude2) {
		this.longitude2 = longitude2;
	}

	@ApiModelProperty(value ="latitude3")
	@JsonProperty("latitude3")	
	public double getLatitude3() {
		return latitude3;
	}

	public void setLatitude3(double latitude3) {
		this.latitude3 = latitude3;
	}
	
	@ApiModelProperty(value ="longitude3")
	@JsonProperty("longitude3")	
	public double getLongitude3() {
		return longitude3;
	}

	public void setLongitude3(double longitude3) {
		this.longitude3 = longitude3;
	}
	
	@ApiModelProperty(value ="latitude4")
	@JsonProperty("latitude4")	
	public double getLatitude4() {
		return latitude4;
	}

	public void setLatitude4(double latitude4) {
		this.latitude4 = latitude4;
	}
	
	@ApiModelProperty(value ="longitude4")
	@JsonProperty("longitude4")	
	public double getLongitude4() {
		return longitude4;
	}

	public void setLongitude4(double longitude4) {
		this.longitude4 = longitude4;
	}
	
	
	@ApiModelProperty(value ="reg_date")
	@JsonProperty("reg_date")	
	public Date getReg_date() {
		return reg_date;
	}


	/**
	 * @param reg_date the reg_date to set
	 */
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}

	@ApiModelProperty(value ="upd_date")
	@JsonProperty("upd_date")	
	public Date getUpd_date() {
		return upd_date;
	}


	/**
	 * @param upd_date the upd_date to set
	 */
	public void setUpd_date(Date upd_date) {
		this.upd_date = upd_date;
	}


	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class NavigationMapVO {\n");
		sb.append(" , map_uid: ").append(map_uid).append("\n");
		
		sb.append("}\n");
		return sb.toString();
	}
	
	
	
	public Coordinate getCenter(){
		Coordinate center = new Coordinate();
		center.setLat(this.getCenter_latitude());
		center.setLng(this.getCenter_longitude());		
		int w = this.getWidth();
		int h = this.getHeight();		
		double center_x = (0+w)/2;
		double center_y = (0+h)/2;		
		center.setX(center_x);
		center.setY(center_y);		
		return center;
		
	}
	
	public List<CoordinateOrder> getPoints(){
		List<CoordinateOrder> points = new ArrayList<CoordinateOrder>();
		CoordinateOrder center = new CoordinateOrder();
		center.setLat(this.getLatitude1());
		center.setLng(this.getLongitude1());
		center.setX(0);
		center.setY(0);
		center.setOrder(1);
		points.add(center);
		
		center = new CoordinateOrder();
		center.setLat(this.getLatitude2());
		center.setLng(this.getLongitude2());
		center.setX(this.getWidth());
		center.setY(0);
		
		//center.setX(0);
		//center.setY(this.getHeight());
		
		center.setOrder(2);
		points.add(center);
		
		center = new CoordinateOrder();
		center.setLat(this.getLatitude3());
		center.setLng(this.getLongitude3());
		center.setX(this.getWidth());
		center.setY(this.getHeight());
		//center.setX(this.getWidth());
		//center.setY(this.getHeight());
		
		center.setOrder(3);
		points.add(center);
		
		center = new CoordinateOrder();
		center.setLat(this.getLatitude4());
		center.setLng(this.getLongitude4());
		center.setX(0);
		center.setY(this.getHeight());
		//center.setX(this.getWidth());
		//center.setY(0);
		
		center.setOrder(4);
		points.add(center);
		
		
		return points;
		
	}
	

	



}

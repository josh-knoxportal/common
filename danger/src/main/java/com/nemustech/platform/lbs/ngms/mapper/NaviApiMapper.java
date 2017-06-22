package com.nemustech.platform.lbs.ngms.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.common.vo.DateVo;
import com.nemustech.platform.lbs.ngms.vo.AppDrivingEventVo;
import com.nemustech.platform.lbs.ngms.vo.AppGpsEventVo;
import com.nemustech.platform.lbs.ngms.vo.AppVehicleRegVo;
import com.nemustech.platform.lbs.ngms.vo.AppViolationEventVo;
import com.nemustech.platform.lbs.ngms.vo.LineThresholdVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceVo;


@Repository(value = "NaviApiMapper")
public interface NaviApiMapper {
	public List<LineThresholdVo> selectLineThresholdList()  throws Exception;
	public List<String> selectPassRoadSectionList()  throws Exception;

	//public List<RoadLinkedVo> selectRoadLinkedList(String section_uid)  throws Exception;

	public List<String> selectRoadLinkedList(String section_uid)  throws Exception;
	
	public int	insertAppDrivingEvent(AppDrivingEventVo appDrivingEventVo) throws Exception;
	public int  cntGoOutVehicle(String vehicle_uid) throws Exception;
	public int	updateLastDrivingEvent(AppDrivingEventVo appDrivingEventVo) throws Exception; 
	

	public DateVo getLastModifiedLineLimit() throws Exception;
	public DateVo getLastModifiedRoadSection() throws Exception;
	public DateVo getLastModifiedCrossSection() throws Exception;
	
	public int insertAppViolationEvent(AppViolationEventVo appViolationEventVo) throws Exception;
	public int updateAppViolationEvent(AppViolationEventVo appViolationEventVo) throws Exception;
	
	public int updateGpsEvent(AppGpsEventVo appGpsEventVo) throws Exception;
	
	public int updateAppDeviceAssigned(AppVehicleRegVo appVehicleRegVo) throws Exception;
	public int insertAppVehicleReg(AppVehicleRegVo appVehicleRegVo) throws Exception;
	
	public String selectAssigendVehicleNo(String device_no) throws Exception;
	
	public int cntStayVehicle(AppVehicleRegVo appVehicleRegVo) throws Exception;
	
	public int updateAppDeviceRelease(AppVehicleRegVo appVehicleRegVo) throws Exception;
	public int updateAppVehicleRelease(AppVehicleRegVo appVehicleRegVo) throws Exception;	
	public NaviDeviceVo selectDeviceInfo(String device_no) throws Exception;
	public int  updateAppDeviceAssignedLast(AppVehicleRegVo appVehicleRegVo) throws Exception;
	
	
	
	
	
	
	
	
}

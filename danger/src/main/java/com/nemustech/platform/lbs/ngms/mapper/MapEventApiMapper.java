package com.nemustech.platform.lbs.ngms.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.ngms.vo.DrivingEventExcelVo;
import com.nemustech.platform.lbs.ngms.vo.DrivingEventVo;
import com.nemustech.platform.lbs.ngms.vo.RestrictAreaRequestVo;
import com.nemustech.platform.lbs.ngms.vo.RoadSectionListVo;
import com.nemustech.platform.lbs.ngms.vo.RoadSectionVo;
import com.nemustech.platform.lbs.ngms.vo.SpeedLimitRequestVo;
import com.nemustech.platform.lbs.ngms.vo.VehicleStatusRequestVo;
import com.nemustech.platform.lbs.ngms.vo.VehicleStatusVo;
import com.nemustech.platform.lbs.ngms.vo.ViolationHistoryVo;

@Repository(value = "MapEventApiMapper")
public interface MapEventApiMapper {
	public List<VehicleStatusVo> selectVehicleStatusList(VehicleStatusRequestVo requestVo) throws Exception;
	public List<ViolationHistoryVo> selectViolationHistoryList(String vehicleUid) throws Exception;
	public List<DrivingEventVo> selectDrivingEventList(String vehicleUid) throws Exception;
	public List<RoadSectionVo> selectRoadSectionList(RoadSectionListVo requestVo) throws Exception;
	public List<DrivingEventExcelVo> selectDrivingEventExcelList(VehicleStatusRequestVo requestVo) throws Exception;
	public int updateSpeedLimit(SpeedLimitRequestVo requestVo) throws Exception;
	public int updateRestrictArea(RestrictAreaRequestVo requestVo) throws Exception;
}

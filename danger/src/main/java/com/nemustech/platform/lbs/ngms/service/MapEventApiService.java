package com.nemustech.platform.lbs.ngms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.platform.lbs.common.util.StringUtil;
import com.nemustech.platform.lbs.ngms.mapper.MapEventApiMapper;
import com.nemustech.platform.lbs.ngms.vo.DrivingEventExcelVo;
import com.nemustech.platform.lbs.ngms.vo.DrivingEventVo;
import com.nemustech.platform.lbs.ngms.vo.DrivingSectionVo;
import com.nemustech.platform.lbs.ngms.vo.RestrictAreaRequestVo;
import com.nemustech.platform.lbs.ngms.vo.RoadSectionListVo;
import com.nemustech.platform.lbs.ngms.vo.RoadSectionVo;
import com.nemustech.platform.lbs.ngms.vo.SpeedLimitRequestVo;
import com.nemustech.platform.lbs.ngms.vo.VehicleStatusRequestVo;
import com.nemustech.platform.lbs.ngms.vo.VehicleStatusVo;
import com.nemustech.platform.lbs.ngms.vo.ViolationHistoryVo;;

@Service(value = "mapEventApiService")
public class MapEventApiService {

	@Autowired
	private MapEventApiMapper mapEventApiMapper;

	public List<VehicleStatusVo> vehicleStatusList(VehicleStatusRequestVo requestVo) throws Exception {
		return mapEventApiMapper.selectVehicleStatusList(requestVo);
	}

	public List<ViolationHistoryVo> violationHistoryList(String vehicleUid) throws Exception {
		return mapEventApiMapper.selectViolationHistoryList(vehicleUid);
	}

	public List<DrivingEventVo> drivingEventList(String vehicleUid) throws Exception {
		return mapEventApiMapper.selectDrivingEventList(vehicleUid);
	}

	public List<DrivingEventExcelVo> drivingEventExcelList(VehicleStatusRequestVo requestVo) throws Exception {
		return mapEventApiMapper.selectDrivingEventExcelList(requestVo);
	}

	/* [2016/05/17] added by capsy */
	public List<DrivingSectionVo> drivingSectionList(String vehicleUid) throws Exception {
		List<DrivingSectionVo> sectionList = new ArrayList<DrivingSectionVo>();
		List<DrivingEventVo> eventList = mapEventApiMapper.selectDrivingEventList(vehicleUid);

		/* */
		String lastSectionUid = "";
		if (eventList != null && eventList.size() > 0) {

			/* */
			for (int idx = 0; idx < eventList.size(); idx++) {
				DrivingEventVo event = eventList.get(idx);

				if (StringUtil.isEmpty(event.getSection_uid()))
					continue;

				/* */
				if (event.getSection_uid().equals(lastSectionUid) == false) {
					DrivingSectionVo section = new DrivingSectionVo();

					section.setVehicle_uid(event.getVehicle_uid());
					section.setSection_uid(event.getSection_uid());
					section.setCount_over_speed(event.getIs_over_speed());
					section.setCount_on_restrict_area(event.getIs_on_restrict_area());
					section.setFrom_reg_date(event.getReg_date());
					section.setTo_reg_date(event.getReg_date());

					section.setFrom_x(event.getX());
					section.setFrom_y(event.getY());
					section.setTo_x(event.getX());
					section.setTo_y(event.getY());

					/* [2016/09/22] added */
					section.setSpeed(event.getSpeed());
					
					/* */
					sectionList.add(section);

					/* */
					lastSectionUid = event.getSection_uid();
				} else {
					try {
						DrivingSectionVo section = sectionList.get(sectionList.size() - 1);

						section.setCount_over_speed(section.getCount_over_speed() + event.getIs_over_speed());
						section.setCount_on_restrict_area(
								section.getCount_on_restrict_area() + event.getIs_on_restrict_area());

						/* */
						section.setTo_reg_date(event.getReg_date());
						section.setTo_x(event.getX());
						section.setTo_y(event.getY());
						
						/* [2016/09/22] added 
						 * 가장 수치가 높은 속도로 조정 
						 */
						if (section.getSpeed() < event.getSpeed()) {
							section.setSpeed(event.getSpeed());
						}					
					} catch (Exception e) {
						/* ignore */
					}
				}
			}
		}

		return sectionList;
	}

	/* @Cacheable(value = "roadCache") */
	public List<RoadSectionVo> roadSectionList(RoadSectionListVo requestVo) throws Exception {
		return mapEventApiMapper.selectRoadSectionList(requestVo);
	}

	/* */
	public void updateSpeedLimit(SpeedLimitRequestVo requestVo) throws Exception {
		if (!requestVo.isEmpty()) {
			mapEventApiMapper.updateSpeedLimit(requestVo);
		}
	}

	/*
	 * @Transactional(isolation=Isolation.DEFAULT,
	 * propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	 */
	public void updateRestrictArea(RestrictAreaRequestVo requestVo) throws Exception {
		if (!requestVo.isEmpty()) {
			mapEventApiMapper.updateRestrictArea(requestVo);
		}
	}
}
package com.nemustech.platform.lbs.ngms.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.platform.lbs.common.util.StringUtil;
import com.nemustech.platform.lbs.common.vo.DateVo;
import com.nemustech.platform.lbs.ngms.mapper.NaviDeviceApiMapper;
import com.nemustech.platform.lbs.ngms.mapper.VehicleApiMapper;
import com.nemustech.platform.lbs.ngms.mapper.VehicleAppApiMapper;
import com.nemustech.platform.lbs.ngms.vo.AppResourceFileInfoVo;
import com.nemustech.platform.lbs.ngms.vo.AppVehicleRegVo;
import com.nemustech.platform.lbs.ngms.vo.MetaZoneVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceVo;
import com.nemustech.platform.lbs.ngms.vo.VehicleRegVo;
import com.nemustech.platform.lbs.wwms.vo.AppPwdEventVo;;

@Service
public class VehicleAppApiService {

	private static final Logger logger = LoggerFactory.getLogger(VehicleAppApiService.class);

	@Autowired
	private NaviDeviceApiMapper naviDeviceApiMapper;

	@Autowired
	private VehicleAppApiMapper VehicleAppApiMapper;

	@Autowired
	private VehicleApiMapper vehicleApiMapper;

	// 160804 [개인폰] [신규] [안전운행13] FCM_token 갱신
	public int updateVehicleDevicePushId(AppPwdEventVo appPwdEventVo) throws Exception {
		logger.debug("updateVehicleDevicePushId ...");
		int result = VehicleAppApiMapper.updateVehicleDevicePushId(appPwdEventVo);
		return result;
	}

	// 160804 [개인폰] [신규] [안전운행 16] Meta Zone 정보 조회
	public List<MetaZoneVo> selectMetaZoneList() throws Exception {
		return VehicleAppApiMapper.selectMetaZoneList();
	}

	// 160804 [개인폰] [신규] [안전운행10] 도로 지도 리소스 정보 조회
	public List<AppResourceFileInfoVo> getAppResourceList() throws Exception {
		return VehicleAppApiMapper.selectAppResourceList();
	}

	// 160804 [개인폰] [신규] [안전운행 11] 개인 단말 차량 등록
	public int createPersonDeviceRegister(AppVehicleRegVo appVehicleRegVo) throws Exception {

		int result = 0;

		// -998 : 차량번호 입력하지 않음
		if (StringUtil.isEmpty(appVehicleRegVo.getCar())) {
			return -998;
		}

		NaviDeviceVo naviDeviceVo = new NaviDeviceVo();
		naviDeviceVo.setDevice_no(appVehicleRegVo.getDevice_no());
		naviDeviceVo = naviDeviceApiMapper.selectNaviDevice(naviDeviceVo);

		// 1. if tbvc_device exist
		if ("".equals(naviDeviceVo) || null == naviDeviceVo) {

			// VehicleRegVo vehicleVo = new VehicleRegVo();
			// vehicleVo.setVehicle_no(appVehicleRegVo.getVehicle_uid());
			// vehicleVo.setDevice_no(naviDeviceVo.getDevice_no());
			//
			// logger.info("VehicleRegVo [{}]", vehicleVo.toString());
			//
			// // -999 : 다른 단말이 이미 할당한 경우, 중복할된 차량번호, tbvc_device
			// int cnt = vehicleApiMapper.selectVehicleRegCheck(vehicleVo);
			// if (cnt > 0)
			// return -999;
			//
			// // -888 : 운행중인 차량번호, tbvc_vehicle
			// cnt = vehicleApiMapper.selectVehiclDrivingCheck(vehicleVo);
			// if (cnt > 0) {
			// logger.info("cnt: " + cnt);
			// return -888;
			// }

			naviDeviceVo = new NaviDeviceVo();
			naviDeviceVo.setDevice_no(appVehicleRegVo.getDevice_no());
			naviDeviceVo.setAssigned_vehicle_no(appVehicleRegVo.getCar());
			naviDeviceVo.setAssigned_vehicle_type(appVehicleRegVo.getCar_type());
			naviDeviceVo.setIs_company(0); // personal
			naviDeviceVo.setIs_assigned(1); // vehicle assigned
			naviDeviceVo.setModel(appVehicleRegVo.getModel());

			// tbvc_device insert
			result = naviDeviceApiMapper.insertAppPersonDeviceRegister(naviDeviceVo);
			if (result < 0) {
				return result;
			}

		} else {

			// -100 : is block ?
			if (1 == naviDeviceVo.getIs_block()) {
				return -100;
			}

			// -999 : 같은 번호로 이미 할당된 경우, 중복할된 차량번호, tbvc_device
			if (!StringUtil.isEmpty(naviDeviceVo.getAssigned_vehicle_no())) {
				if (naviDeviceVo.getAssigned_vehicle_no().equals(appVehicleRegVo.getCar())) {
					return -999;
				}
			}

			VehicleRegVo vehicleVo = new VehicleRegVo();
			vehicleVo.setVehicle_no(naviDeviceVo.getAssigned_vehicle_no());
			vehicleVo.setDevice_no(naviDeviceVo.getDevice_no());
			vehicleVo.setDevice_uid(naviDeviceVo.getDevice_uid());

			logger.info("VehicleRegVo [{}]", vehicleVo.toString());

			// -999 : 다른 단말이 이미 할당한 경우, 중복할된 차량번호, tbvc_device
			int cnt = vehicleApiMapper.selectVehicleRegCheck(vehicleVo);
			if (cnt > 0)
				return -999;

			// -888 : 운행중인 차량번호, tbvc_vehicle
			cnt = vehicleApiMapper.selectVehiclDrivingCheck(vehicleVo);
			if (cnt > 0) {
				logger.info("cnt: " + cnt);
				return -888;
			}

			// is release ? how
			if (0 == naviDeviceVo.getIs_used()) {

			}

			naviDeviceVo = new NaviDeviceVo();
			naviDeviceVo.setDevice_no(appVehicleRegVo.getDevice_no());
			naviDeviceVo.setAssigned_vehicle_no(appVehicleRegVo.getCar());
			naviDeviceVo.setAssigned_vehicle_type(appVehicleRegVo.getCar_type());
			naviDeviceVo.setIs_company(0); // personal
			naviDeviceVo.setIs_assigned(1); // vehicle assigned
			naviDeviceVo.setModel(appVehicleRegVo.getModel());
			result = naviDeviceApiMapper.updateAppPersonDeviceRegister(naviDeviceVo);
			if (result < 0) {
				return result;
			}

		}

		return result;
	}

	// 1608 [신규] [공통] 리소스 정보의 최신 갱신 여부
	public boolean getIsLastModifiedResource(String if_modified_since) throws Exception {
		DateVo dateVo = VehicleAppApiMapper.getIsLastModifiedResource();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);
		logger.info("date : " + param);
		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	// 1608 [신규] [공통] MetaZone 정보의 최신 갱신 여부
	public boolean getIsLastModifiedMetaZone(String if_modified_since) throws Exception {
		DateVo dateVo = VehicleAppApiMapper.getIsLastModifiedMetaZone();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);
		logger.info("date : " + param);
		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

}

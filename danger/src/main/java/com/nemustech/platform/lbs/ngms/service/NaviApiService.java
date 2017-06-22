package com.nemustech.platform.lbs.ngms.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nemustech.platform.lbs.common.service.PushService;
import com.nemustech.platform.lbs.common.util.SessionUtil;
import com.nemustech.platform.lbs.common.vo.DateVo;
import com.nemustech.platform.lbs.ngms.mapper.NaviApiMapper;
import com.nemustech.platform.lbs.ngms.mapper.VehicleApiMapper;
import com.nemustech.platform.lbs.ngms.vo.AppDrivingEventVo;
import com.nemustech.platform.lbs.ngms.vo.AppGpsEventVo;
import com.nemustech.platform.lbs.ngms.vo.AppPersonDeviceVehicleUnRegVo;
import com.nemustech.platform.lbs.ngms.vo.AppVehicleRegVo;
import com.nemustech.platform.lbs.ngms.vo.AppViolationEventVo;
import com.nemustech.platform.lbs.ngms.vo.LineThresholdVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceVo;
import com.nemustech.platform.lbs.ngms.vo.RoadLinkedVo;

@Service(value = "naviApiService")
public class NaviApiService {

	private static final Logger logger = LoggerFactory.getLogger(NaviApiService.class);

	@Autowired
	private NaviApiMapper naviApiMapper;

	@Autowired
	private VehicleApiMapper vehicleApiMapper;

	@Autowired
	private PushService pushService;

	public boolean getIsLastModifiedExtra(String if_modified_since) throws Exception {
		DateVo dateVo = naviApiMapper.getLastModifiedLineLimit();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);

		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	public boolean getIsLastModifiedLoadSection(String if_modified_since) throws Exception {
		DateVo dateVo = naviApiMapper.getLastModifiedRoadSection();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);

		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	public boolean getIsLastModifiedCrossSection(String if_modified_since) throws Exception {
		DateVo dateVo = naviApiMapper.getLastModifiedCrossSection();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);

		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	// @Cacheable(value = "lineThresholdCache")
	public List<LineThresholdVo> selectLineThresholdList() throws Exception {
		logger.info("selectLineLimitList ...");
		return naviApiMapper.selectLineThresholdList();

	}

	public List<RoadLinkedVo> selectRoadLinkedList() throws Exception {
		logger.info("selectRoadLinkedList ...");
		List<RoadLinkedVo> list = new ArrayList<RoadLinkedVo>();
		List<String> roadList = naviApiMapper.selectPassRoadSectionList();
		if (roadList != null) {
			for (String section_uid : roadList) {
				RoadLinkedVo map = new RoadLinkedVo();
				List<String> sub_section_uid = naviApiMapper.selectRoadLinkedList(section_uid);
				map.setMaster_section_uid(section_uid);
				map.setSub_section_uid(sub_section_uid);
				list.add(map);
			}
		}

		return list;
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public int insertAppDrivingEvent(AppDrivingEventVo appDrivingEventVo) throws Exception {
		logger.info("insertAppDrivingEvent ...");
		int result = naviApiMapper.insertAppDrivingEvent(appDrivingEventVo);
		result = naviApiMapper.updateLastDrivingEvent(appDrivingEventVo);
		// udpate 가 안될 경우 오류코드정의
		return result;

	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public int appViolationEvent(AppViolationEventVo appViolationEventVo) throws Exception {
		logger.info("appViolationEvent ...");
		// appViolationEventVo
		int result = 0;
		if ("ended".equals(appViolationEventVo.getPoint())) {
			result = naviApiMapper.updateAppViolationEvent(appViolationEventVo);
		}
		// update 인데도 데이터가 없다면 insert 한다.
		if (result == 0) {
			result = naviApiMapper.insertAppViolationEvent(appViolationEventVo);
			// naviApiMapper 로 확인한 다음 존재하면 skip
		}

		return result;

	}

	public int updateGpsEvent(AppGpsEventVo appGpsEventVo) throws Exception {
		logger.info("updateGpsEvent ...");
		int result = naviApiMapper.updateGpsEvent(appGpsEventVo);
		// udpate 가 안될 경우 오류코드정의
		return result;

	}

	public int cntGoOutVehicle(String vehicle_uid) throws Exception {
		logger.info("cntGoOutVehicle ...");
		int result = naviApiMapper.cntGoOutVehicle(vehicle_uid);
		return result;

	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public int appRegVehicle(AppVehicleRegVo appVehicleRegVo) throws Exception {
		logger.info("appRegVehicle ...");
		int result = 0;
		// 차량등록 시나리오
		// 1. tbvc_device 테이블에 차량이 할당되어 있는지 확인한다. (이미 웹에서 등록되어 확인하는 경우, 앱에서
		// 차량번호까지 입력하는 경우)
		// device=01012341234, car=12345
		// 1.1 할당되지 않는 차량이라면 에러리턴 - 현재단말기는 번호랑 매핑이 되어 있습니다. 아니면 할당된 정보가
		// 없습니다.
		// --> // 일단 앱에서 할당된 내역을 확인하는지 앱에서 바로 등록하는 assigned 필드 추가.

		// 2. 할당이 되어 있으면 vehicle table에 insert
		// 2.1 이미 vehicle table에 데이터가 등록이 되어 있으면 (device테이블에 이미 폰과 차량이 할당이 되어
		// 있음) .. 이것도오류임.
		// 3. device_table 에 last_vehicle_id , push_id update
		// 4 성공리턴

		String assignedVehicleNo = naviApiMapper.selectAssigendVehicleNo(appVehicleRegVo.getDevice_no());
		if (appVehicleRegVo.getAssigned() == 1) {
			if (assignedVehicleNo == null || "".equals(assignedVehicleNo)) {
				// return -901; //할당된 차량정보가 없습니다.
				return -901;
			}
			if (!assignedVehicleNo.equals(appVehicleRegVo.getCar())) {
				// return -902; //할당된 챠량정보가 일치하지 않습니다.
				return -902;
			}
		} else {
			if (assignedVehicleNo != null && !"".equals(assignedVehicleNo)) {
				if (!appVehicleRegVo.getCar().equals(assignedVehicleNo))
					return -903; // return -903; //이미 할당된 단말기 입니다.
			}
		}

		// vehicle table insert

		int stayVehicleCnt = naviApiMapper.cntStayVehicle(appVehicleRegVo);
		if (stayVehicleCnt > 0) {
			return -903; // return -904; //이미 할당된 단말기 입니다.
		}

		String vehicle_uid = vehicleApiMapper.selectUid();
		appVehicleRegVo.setVehicle_uid(vehicle_uid);
		result = naviApiMapper.insertAppVehicleReg(appVehicleRegVo);

		// 할당이 안된거라 생각하고 device table에 update
		if (assignedVehicleNo == null) {
			naviApiMapper.updateAppDeviceAssigned(appVehicleRegVo);
		} else {
			naviApiMapper.updateAppDeviceAssignedLast(appVehicleRegVo);
		}

		return result;

	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public int appUnRegVehicle(List<String> deviceNoList, String request_type) throws Exception {
		int result = 0;
		for (String deviceNo : deviceNoList) {
			AppVehicleRegVo appVehicleRegVo = new AppVehicleRegVo();
			appVehicleRegVo.setDevice_no(deviceNo);
			result = appUnRegVehicle(appVehicleRegVo, request_type);
		}
		return result;
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public int appUnRegVehicle(AppVehicleRegVo appVehicleRegVo, String request_type) throws Exception {
		logger.info("appUnRegVehicle ...");
		int result = 0;
		// 차량해제 시나리오
		// 1. tbvc_device 테이블에 차량이 할당되어 있는지 확인한다.
		// 1.1 할당되지 않는 차량이라면 에러리턴 - 현재단말기는 번호랑 매핑이 되어 있습니다. 아니면 할당된 정보가
		// 없습니다.
		// 2. vehicle 에 데이터가 있는지 확인한다.
		// 2.1 만약 데이터가 없다면 할당만 하고 등록은 안된 상태로..패스??
		// 2.2 데이터가 존재한다면 is_out = 1, go_out_date = now() 로 업데이트 한다.

		String assignedVehicleNo = naviApiMapper.selectAssigendVehicleNo(appVehicleRegVo.getDevice_no());

		// 앱인 경우만 시도
		if ("1".equals(request_type)) {

			if (assignedVehicleNo == null || "".equals(assignedVehicleNo)) {
				// return -901; //할당된 차량정보가 없습니다.
				return -901;
			}
			if (!assignedVehicleNo.equals(appVehicleRegVo.getCar())) {
				// return -902; //할당된 챠량정보가 일치하지 않습니다.
				return -902;
			}

			// 앱에서 vehicle_uid 제대로 넘어오지 않을 경우 .
			if (appVehicleRegVo.getVehicle_uid() == null || "".equals(appVehicleRegVo.getVehicle_uid())) {
				logger.info("앱에서 vehicle_uid가 넘어오지 않았습니다.:: " + appVehicleRegVo);
				NaviDeviceVo deviceVo = naviApiMapper.selectDeviceInfo(appVehicleRegVo.getDevice_no());
				appVehicleRegVo.setVehicle_uid(deviceVo.getLast_vehicle_uid());
				appVehicleRegVo.setIs_company(deviceVo.getIs_company());
			}

		} else {
			// web에서 시도하는 경우 vehicle_uid 알아와서..
			NaviDeviceVo deviceVo = naviApiMapper.selectDeviceInfo(appVehicleRegVo.getDevice_no());
			appVehicleRegVo.setIs_company(deviceVo.getIs_company());

			String vehicle_uid = deviceVo.getLast_vehicle_uid();
			if (vehicle_uid != null && !"".equals(vehicle_uid)) {

				appVehicleRegVo.setCar(deviceVo.getAssigned_vehicle_no());
				int cntStayVehicle = naviApiMapper.cntStayVehicle(appVehicleRegVo);
				// 값이 있으면 push 대상이라고 봐야함.
				if (cntStayVehicle > 0) {
					// 여기에다 Push서버 구현해야함.
					// WebUtils.getSessionAttribute(request, name)
					// user_id 값 가져오는거 정의해야 됨.
					String user_id = "admin";
					if (SessionUtil.isLogin()) {
						user_id = SessionUtil.getAccountInfo().getUser_id();
					}
					pushService.pushSend("1", "unregister", appVehicleRegVo.getDevice_no(), "차량해제", user_id);

					logger.info("pushService ...success");

					// appVehicleRegVo.setVehicle_uid(vehicle_uid);
				}

				logger.info("setVehicle_uid ...success :" + vehicle_uid);
				appVehicleRegVo.setVehicle_uid(vehicle_uid);
			}
			/*
			 * String user_id = "admin2"; if(SessionUtil.isLogin()){ user_id =
			 * SessionUtil.getAccountInfo().getUser_id(); } //push서버로 보내기 모든걸
			 * push 서버로 보낼 필요는 없음 pushService.pushSend("1","unregister",
			 * appVehicleRegVo.getDevice_no(), "차량해제", user_id);
			 */
		}

		// 1. update device
		result = naviApiMapper.updateAppDeviceRelease(appVehicleRegVo);

		logger.info("updateAppDeviceRelease ...result :" + result);

		if (appVehicleRegVo.getVehicle_uid() != null && !"".equals(appVehicleRegVo.getVehicle_uid())) {

			// 2. update vechicle
			result = naviApiMapper.updateAppVehicleRelease(appVehicleRegVo);
			logger.info("updateAppVehicleRelease ...success :" + appVehicleRegVo.getVehicle_uid() + "|....");

		} else {
			logger.info("updateAppVehicleRelease pass ...appVehicleRegVo.getVehicle_uid() null - ["
					+ appVehicleRegVo.getVehicle_uid() + "|....");
		}

		return result;

	}

	public NaviDeviceVo selectDeviceInfo(String device_no) throws Exception {
		logger.debug("selectDeviceInfo ... : " + device_no);
		return naviApiMapper.selectDeviceInfo(device_no);
	}

	// 160804 [개인폰] [신규] [안전운행14] 개인 단말 차량 해제
	public int appUnRegVehicle(AppPersonDeviceVehicleUnRegVo appPersonDeviceVehicleUnRegVo, String request_type)
			throws Exception {
		int result = 0;

		AppVehicleRegVo appVehicleRegVo = new AppVehicleRegVo();
		appVehicleRegVo.setCar(appPersonDeviceVehicleUnRegVo.getCar());
		appVehicleRegVo.setDevice_no(appPersonDeviceVehicleUnRegVo.getDevice_no());
		appVehicleRegVo.setIs_company(1);

		String assignedVehicleNo = naviApiMapper.selectAssigendVehicleNo(appVehicleRegVo.getDevice_no());

		if (StringUtils.isEmpty(assignedVehicleNo)) {
			// return -901; //할당된 차량정보가 없습니다.
			return -901;
		}

		if (!assignedVehicleNo.equals(appVehicleRegVo.getCar())) {
			// return -902; //할당된 챠량정보가 일치하지 않습니다.
			return -902;
		}

		// get vehicle_uid
		logger.info("앱에서 vehicle_uid가 넘어오지 않았습니다.:: " + appVehicleRegVo);
		NaviDeviceVo deviceVo = naviApiMapper.selectDeviceInfo(appVehicleRegVo.getDevice_no());
		appVehicleRegVo.setVehicle_uid(deviceVo.getLast_vehicle_uid());

		// 1. update device
		result = naviApiMapper.updateAppDeviceRelease(appVehicleRegVo);

		logger.info("appUnRegVehicle ...result :" + result);

		if (!StringUtils.isEmpty(appVehicleRegVo.getVehicle_uid())) {

			// 2. update vechicle
			result = naviApiMapper.updateAppVehicleRelease(appVehicleRegVo);
			logger.info("appUnRegVehicle ...success :" + appVehicleRegVo.getVehicle_uid() + "|....");

		} else {
			logger.info("appUnRegVehicle pass ...appVehicleRegVo.getVehicle_uid() null - ["
					+ appVehicleRegVo.getVehicle_uid() + "|....");
		}

		return result;
	}

}

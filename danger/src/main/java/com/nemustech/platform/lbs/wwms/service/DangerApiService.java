package com.nemustech.platform.lbs.wwms.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nemustech.platform.lbs.common.util.StringUtil;
import com.nemustech.platform.lbs.common.vo.DateVo;
import com.nemustech.platform.lbs.ngms.vo.AppDeviceInfoVo;
import com.nemustech.platform.lbs.wwms.mapper.DangerApiMapper;
import com.nemustech.platform.lbs.wwms.mapper.DeviceDangerApiMapper;
import com.nemustech.platform.lbs.wwms.vo.AppGpsBleEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppPwdEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppWorkRegEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppWorkUnRegEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppZoneEventVo;
import com.nemustech.platform.lbs.wwms.vo.BeaconZoneVo;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerVo;
import com.nemustech.platform.lbs.wwms.vo.EnterExitRecordVo;
import com.nemustech.platform.lbs.wwms.vo.GpsZoneVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerMntVo;

@Service(value = "dangerApiService")
public class DangerApiService {

	private static final Logger logger = LoggerFactory.getLogger(DangerApiService.class);

	@Autowired
	private DangerApiMapper dangerApiMapper;

	@Autowired
	private DeviceDangerApiMapper deviceDangerApiMapper;

	public Map<String, Object> getAlram() throws Exception {
		Map<String, Object> map = dangerApiMapper.selectAlram();
		return map;
	}

	public boolean getIsLastModifiedGpsZone(String if_modified_since) throws Exception {
		DateVo dateVo = dangerApiMapper.getLastModifiedGpsZone();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);
		logger.info("date : " + param);
		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	public boolean getIsLastModifiedBeaconZone(String if_modified_since) throws Exception {
		DateVo dateVo = dangerApiMapper.getLastModifiedBeaconZone();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);
		logger.info("date : " + param);
		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	public List<GpsZoneVo> selectGpsZoneList() throws Exception {
		return dangerApiMapper.selectGpsZoneList();
	}

	public List<BeaconZoneVo> selectBeaconZoneList() throws Exception {
		return dangerApiMapper.selectBeaconZoneList();
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public int insertAppZoneEvent(AppZoneEventVo appZoneEventVo, int type) throws Exception {
		logger.info("insertAppZoneEvent ...");
		int result = 0;
		String worker_uid = null;

		if (type == 0) {
			worker_uid = dangerApiMapper.selectDeviceWorkerUidByDeviceUid(appZoneEventVo.getDevice_uid());
			appZoneEventVo.setWorker_uid(worker_uid);
		}

		if ("gps".equals(appZoneEventVo.getType()))
			result = dangerApiMapper.insertAppGpsEvent(appZoneEventVo);
		else
			result = dangerApiMapper.insertAppBeaconEvent(appZoneEventVo);

		// 작업할당인 경우 type = 1
		if (type == 1) {
			result = dangerApiMapper.updateLastZoneEvent(appZoneEventVo);
		} else {
			result = dangerApiMapper.updateWorkerAssignLastZoneEvent(appZoneEventVo);
		}

		// 입출입 관련 테이블
		EnterExitRecordVo enterExitRecord = new EnterExitRecordVo();
		enterExitRecord.setDevice_no(appZoneEventVo.getDevice_no());
		if (type == 0) {
			enterExitRecord.setWorker_uid(appZoneEventVo.getWorker_uid());
			logger.debug("enterExitRecord.setWorker_uid [{}]", appZoneEventVo.getWorker_uid());
		} else {
			enterExitRecord.setWork_uid(appZoneEventVo.getWork_uid());
			logger.debug("enterExitRecord.setWorker_uid [{}]", appZoneEventVo.getWork_uid());
		}

		enterExitRecord.setFactory_uid(appZoneEventVo.getZone_id());
		enterExitRecord.setZone_uid(appZoneEventVo.getBeacon_zone_id());
		enterExitRecord.setIs_restricted(appZoneEventVo.getRestricted());

		logger.debug(enterExitRecord.toString());

		// 동일 단말, 동일 구역, 인가/비인가, 입/출입 동일한 경우가 있는 숫자
		Integer nCount = dangerApiMapper.selectCountEnterExitRecord(enterExitRecord);

		// if enter else exit
		if (appZoneEventVo.getEntered() == 1) {
			if (nCount > 0) {
				logger.debug(" pass : enter = 0 ");
				return result;
			} else {
				logger.debug(" update : enter = 0 ");
				// update exit date
				result = dangerApiMapper.updateEnterExitRecord(enterExitRecord);

				logger.debug(" update : enter = 0 , update before date [{}]", result);

				// insert
				result = dangerApiMapper.insertEnterExitRecord(enterExitRecord);
				logger.debug(" update : enter = 0 , insert new data [{}]", result);
			}
		} else {
			logger.debug(" exit 1 --> update exit_date is null");
			result = dangerApiMapper.updateEnterExitRecord(enterExitRecord);
		}

		// device - last_server_time update
		result = dangerApiMapper.updateDeviceLastServerAccessTime(appZoneEventVo.getDevice_no());

		return result;
	}

	// [출입감지 08] 작업자 출입 이벤트
	public void setAppZoneEventVo(AppZoneEventVo appZoneEventVo) throws Exception {

		// 작업번호에 해당하는 작업이 있는지 확인
		WorkerMntVo workerMntVo = dangerApiMapper.selectWork(appZoneEventVo.getWork_uid());
		logger.info("setAppZoneEventVo  intput parameter AppZoneEventVo : " + appZoneEventVo);

		// 할당된 작업정보가 있는 경우
		if (workerMntVo != null) {

			logger.info("WorkerMntVo1 : " + workerMntVo.getZone_uid());
			logger.info("WorkerMntVo 2: " + workerMntVo.getFactory_uid());

			// work_uid exist && GPS Zone
			if ("gps".equals(appZoneEventVo.getType())) {

				if (appZoneEventVo.getZone_id().equals(workerMntVo.getFactory_uid())) {
					appZoneEventVo.setAuthorizied(1);
				} else if (appZoneEventVo.getRestricted() == 0) {
					appZoneEventVo.setZone_name(dangerApiMapper.selectFactoryName(appZoneEventVo.getZone_id()));
				}

			} else { // work_uid exist && beacon Zone

				if (appZoneEventVo.getBeacon_zone_id().equals(workerMntVo.getZone_uid())) {
					appZoneEventVo.setAuthorizied(1);
				} else if (appZoneEventVo.getRestricted() == 0) {
					appZoneEventVo.setZone_name(dangerApiMapper.selectZoneName(appZoneEventVo.getBeacon_zone_id()));
				}

			}
		} else {

			// worker_uid[ not work_uid ] && gps zone
			// 작업번호가 없는 경우는 gps/zone 이벤트에서 보내준 restricted 값을 기준으로 한다.
			if ("gps".equals(appZoneEventVo.getType())) {
				if (appZoneEventVo.getRestricted() == 1) {
					appZoneEventVo.setAuthorizied(1);
				} else if (appZoneEventVo.getRestricted() == 0) {
					appZoneEventVo.setZone_name(dangerApiMapper.selectFactoryName(appZoneEventVo.getZone_id()));
				}
			} else {
				if (appZoneEventVo.getRestricted() == 1) {
					appZoneEventVo.setAuthorizied(1);
				} else if (appZoneEventVo.getRestricted() == 0) {
					appZoneEventVo.setZone_name(dangerApiMapper.selectZoneName(appZoneEventVo.getBeacon_zone_id()));
				}
			}

		}

	}

	public int cntWorking(String work_uid) throws Exception {
		logger.info("cntWorking ...");
		int result = dangerApiMapper.cntWorking(work_uid);
		return result;

	}

	public int updateGpsEvent(AppGpsBleEventVo appGpsBleEventVo) throws Exception {
		logger.info("updateGpsEvent ...");
		int result = dangerApiMapper.updateGpsBleEvent(appGpsBleEventVo);
		return result;

	}

	// [출입감지 10] 작업 해제(종료)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public int appUnRegWork(AppWorkUnRegEventVo appWorkUnRegEventVo, String request_type) throws Exception {
		logger.info("appUnRegVehicle ...");
		int result = 0;

		// 작업 해제 시나리오
		// 1. tbda_work 테이블에 작업이 할당되어 있는지 확인한다.
		// 1.1 할당되지 않는 작업이라면 에러리턴 - 할당된 정보가 없습니다.
		// 1.2 tbda_device 의 할당된 work_uid 와 일치한지 확인한다. - 일치한 작업정보가 없다.

		// int cnt =
		// dangerApiMapper.cntWorkerAssignedByDevice(appWorkUnRegEventVo.getDevice_no());
		// if (cnt == 0) {
		// return -901; // 할당된 작업정보가 없습니다.
		// }

		// worker_assinged, device
		result = dangerApiMapper.updateAssignedDeviceRelease(appWorkUnRegEventVo);
		result = dangerApiMapper.updateDeviceRelease(appWorkUnRegEventVo);

		int completeCount = dangerApiMapper.selectIsCompletedAssignedWork(appWorkUnRegEventVo);
		if (completeCount == 1) {
			logger.info("work real completed  -> update work table");
			result = dangerApiMapper.updateNWorkUnReg(appWorkUnRegEventVo);
		}

		// 법인폰인 경우 공장/존 안에서 작업을 시작하면 enter 이벤트는 들어오나
		// 작업 종료이후에는 exit 이벤트 발생하지 않음. 서버에서 단말구분으로
		// eer_exit_date를 갱신함
		result = dangerApiMapper.updateEnterExitRecordByCompanyDevice(appWorkUnRegEventVo);
		return result;

	}

	// [출입감지 07] 작업 등록 (시작)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public int appRegWork(AppWorkRegEventVo appWorkRegEventVo, String request_type) throws Exception {
		logger.info("appRegWork ... :" + appWorkRegEventVo);
		int result = 0;
		int nCount = 0;

		String device_no = appWorkRegEventVo.getDevice_no();

		/*
		 * /// 0909 해당 단말번호로 기존 작업중인것이 있으면 기존꺼는 삭제한다.
		 */
		int cnt = dangerApiMapper.cntWorkerAssignedByDevice(device_no);
		if (cnt > 0) {
			AppWorkUnRegEventVo appWorkUnRegEventVo = new AppWorkUnRegEventVo();
			appWorkUnRegEventVo.setDevice_no(device_no);

			// worker_assinged, device
			result = dangerApiMapper.updateAssignedDeviceReleaseByWorkRegister(device_no);
			result = dangerApiMapper.updateDeviceReleaseByWorkRegister(device_no);

			logger.info("work real completed  -> update work table");
			result = dangerApiMapper.updateNWorkUnRegByWorkRegister();

			// update enter_exit_record
			result = dangerApiMapper.updateEnterExitRecodeByWorkRegister(device_no);
		}

		// 작업 등록 시나리오
		if (!StringUtil.isEmpty(appWorkRegEventVo.getWork_uid())) {

			// 없는 단말기 번호인지 확인하는 로직도 필요함
			// 1. 작업번호가 있는 경우(work_uid)
			// 1.1 해당작업번가 table에 존재하는지 확인. 만약 존재하지 않는다면 오류메시지 : 존재하지 않거나 이미 완료된
			// 작업번호입니다.
			// 1.2 work 테이블에 update , device 테이블에 작업할당으로 update

			// int cntRegWork =
			// dangerApiMapper.cntAppWorkReg(appWorkRegEventVo.getWork_uid());
			// if (cntRegWork == 0) {
			// return -901; // 존재하지 않거나 이미 완료된 작업번호입니다.
			// }

			logger.info("appWorkRegEventVo.getWork_uid():" + appWorkRegEventVo.getWork_uid());

			WorkerMntVo vo = dangerApiMapper.selectWork(appWorkRegEventVo.getWork_uid());

			logger.info("WorkerMntVo:" + vo.getZone_uid());

			if (vo.getZone_uid() != null && !"".equals(vo.getZone_uid())) {
				appWorkRegEventVo.setZone_id(vo.getZone_uid());
				appWorkRegEventVo.setZone_type("beacon");
			} else {
				appWorkRegEventVo.setFactory_uid(vo.getFactory_uid());
				appWorkRegEventVo.setZone_type("gps");
			}

			// 첫번째 작업자인 경우 작업 테이블에 대표자로 매핑
			// 두번째부터는 필요없음
			if (StringUtils.isEmpty(vo.getDevice_no())) {
				result = dangerApiMapper.updateAppWorkReg(appWorkRegEventVo);
				logger.info("updateAppWorkReg [{}]", result);
			}

			// 기존 끝나지 않은 작업할당은 종료해야함
			result = deviceDangerApiMapper.updateNewWorkerAssignedDeviceRelease(appWorkRegEventVo.getDevice_no());
			logger.info("updateWorerAssignedDeviceRelease [{}]", result);

		} else {
			// 2. 작업번호가 없는 경우(앱에서 작업직접등록함)
			// 2.1 해당 단말기에 할당된 작업이 없는지 확인한다. 만약 할당된 작업이 존재한다면 오류메시지 : 이미 할당된 작업이
			// 있는 단말기 입니다.
			// 2.2 work 테이블에 insert 한다. device table update
			// int cntRegDevice =
			// dangerApiMapper.cntAppWorkRegDevice(appWorkRegEventVo.getDevice_no());
			//
			// if (cntRegDevice > 0) {
			// return -902; // 이미 할당된 작업이 있는 단말기 입니다.
			// }

			String work_uid = dangerApiMapper.selectUid();
			appWorkRegEventVo.setWork_uid(work_uid);

			logger.info("appWorkRegEventVoappWorkRegEventVoappWorkRegEventVo:" + appWorkRegEventVo);
			String name = "";
			if ("zone".equals(appWorkRegEventVo.getZone_type())) {
				name = dangerApiMapper.selectZoneName(appWorkRegEventVo.getZone_id());
				appWorkRegEventVo.setZone_type("beacon");
				appWorkRegEventVo.setZone_uid(appWorkRegEventVo.getZone_id());
			} else {
				name = dangerApiMapper.selectFactoryName(appWorkRegEventVo.getZone_id());
				appWorkRegEventVo.setFactory_uid(appWorkRegEventVo.getZone_id());
				appWorkRegEventVo.setZone_type("gps");
			}
			logger.info("factory name :" + name);
			String work_no = dangerApiMapper.makeWorkNo(name);
			logger.info(work_uid + " make work_no : " + work_no);
			appWorkRegEventVo.setWork_no(work_no);

			// app 등록시 값을 설정해줌
			if ("1".equals(request_type)) {
				appWorkRegEventVo.setWork_make_type(request_type);
			}

			// 새 작업 정보 등록
			dangerApiMapper.insertAppWorkReg(appWorkRegEventVo);
			dangerApiMapper.insertAppWorkType(appWorkRegEventVo);
		}

		// woker_assigned
		nCount = dangerApiMapper.selectCountWorkAssignedTypeTwo(appWorkRegEventVo.getDevice_no());
		if (nCount > 0) {
			logger.debug("insert worker_assigned == 2");
			result = dangerApiMapper.updateWorkerAssigned(appWorkRegEventVo);
		} else {
			logger.debug("insert worker_assigned != 2");
			result = dangerApiMapper.insertWorkerAssinged(appWorkRegEventVo);
		}

		// 단말 할당 갱신
		result = dangerApiMapper.updateAppWorkRegDevice(appWorkRegEventVo);

		logger.debug("appWorkRegEventVo.getDevice_no() [" + appWorkRegEventVo.getDevice_no() + "]");
		// eer update
		// 작업할당 없이 공장/존진입후 작업시작하면 eer 테이블은 worker_uid 만 가져서 작업할당이 않된것처럼 보임
		int newCount = dangerApiMapper.selectCountEnterExitRecordByDeviceNO(appWorkRegEventVo.getDevice_no());
		logger.debug("exist eer count [" + newCount + "]");

		if (newCount > 0) {

			int is_restricted = 0;

			EnterExitRecordVo oldEerVo = dangerApiMapper
					.selectEnterExitRecordByDeviceNo(appWorkRegEventVo.getDevice_no());
			logger.debug("exist eer Vo [{}]", oldEerVo.toString());

			EnterExitRecordVo eerVo = new EnterExitRecordVo();
			eerVo.setDevice_no(appWorkRegEventVo.getDevice_no());
			eerVo.setWork_uid(appWorkRegEventVo.getWork_uid());

			// factory_uid
			if (!StringUtil.isEmpty(oldEerVo.getFactory_uid())) {
				eerVo.setFactory_uid(oldEerVo.getFactory_uid());
				if ("gps".equals(appWorkRegEventVo.getZone_type())) {
					if (StringUtils.equals(oldEerVo.getFactory_uid(), appWorkRegEventVo.getFactory_uid())
							|| oldEerVo.getFactory_uid().equals(appWorkRegEventVo.getFactory_uid())
							|| oldEerVo.getFactory_uid() == appWorkRegEventVo.getFactory_uid()) {
						logger.debug("eer factory_uid = work reg factory_uid, is_restricted = 1");
						is_restricted = 1;
					}
				}
			} else if (!StringUtil.isEmpty(oldEerVo.getZone_uid())) {
				eerVo.setZone_uid(oldEerVo.getZone_uid());
				if ("beacon".equals(appWorkRegEventVo.getZone_type())) {
					if (StringUtils.equals(oldEerVo.getZone_uid(), appWorkRegEventVo.getZone_uid())
							|| oldEerVo.getZone_uid().equals(appWorkRegEventVo.getZone_uid())
							|| oldEerVo.getZone_uid() == appWorkRegEventVo.getZone_uid()) {
						logger.debug("eer getZone_uid = work reg getZone_uid, is_restricted = 1");
						is_restricted = 1;
					}
				}
			}

			int device_factory_uid = dangerApiMapper.selectDeviceFactoryUidByDeviceNo(appWorkRegEventVo.getDevice_no());
			logger.debug("device_factory_uid [{}]", device_factory_uid);
			if (device_factory_uid == 999) {
				is_restricted = 1;
			}

			eerVo.setIs_restricted(is_restricted);

			logger.debug("Enter Exit Record exitdate update now()");
			result = dangerApiMapper.updateExitDateByCurDate(appWorkRegEventVo.getDevice_no());

			logger.debug("insert eer Vo [{}]", eerVo.toString());
			result = dangerApiMapper.insertEnterExitRecord(eerVo);
		}

		return result;
	}

	public int updatePushId(AppPwdEventVo appPwdEventVo) throws Exception {
		logger.debug("updateAppDevicePushId ...");
		int result = dangerApiMapper.updateAppDevicePushId(appPwdEventVo);
		return result;
	}

	public DeviceDangerVo selectDeviceInfo(String device_no) throws Exception {
		logger.debug("selectDeviceInfo ...");
		return dangerApiMapper.selectDeviceInfo(device_no);
	}

	public AppWorkRegEventVo selectWorkInfo(String work_uid) throws Exception {
		logger.debug("selectWorkInfo ...");
		return dangerApiMapper.selectWorkInfo(work_uid);
	}

	public List<AppZoneEventVo> selectRestrictedList() throws Exception {
		return dangerApiMapper.selectRestrictedList();
	}

	@Transactional
	public int updateDeviceNetworkType(AppDeviceInfoVo appDeviceInfoVo) throws Exception {
		return dangerApiMapper.updateDeviceNetworkType(appDeviceInfoVo);
	}

}

package com.nemustech.platform.lbs.wwms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nemustech.platform.lbs.wwms.mapper.DangerApiMapper;
import com.nemustech.platform.lbs.wwms.mapper.DangerAppApiMapper;
import com.nemustech.platform.lbs.wwms.mapper.DeviceDangerApiMapper;
import com.nemustech.platform.lbs.wwms.vo.AppAliveReportEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppServerAccessEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppWorkerRegEventVo;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerCudVo;
import com.nemustech.platform.lbs.wwms.vo.EnterExitRecordVo;

@Service(value = "DangerAppApiService")
public class DangerAppApiService {

	private static final Logger logger = LoggerFactory.getLogger(DangerAppApiService.class);

	@Autowired
	private DangerApiMapper dangerApiMapper;

	@Autowired
	private DangerAppApiMapper dangerAppApiMapper;

	@Autowired
	private DeviceDangerApiMapper deviceDangerApiMapper;

	// 160803 [개인폰] [신규] alvie report event insert
	@Transactional
	public int insertAppAliveReportInfo(AppAliveReportEventVo appAliveReportEventVo) throws Exception {
		logger.debug("inserttAppAliveReportInfo");
		Integer nCount = 0;
		int result = 0;

		// alive reoprt 0 : 단말계속보내줌, 1: 단말이 플랜트내에서 일시중지, 2 : 플랜트 밖을 완전히 나가는 경우
		/*
		 * 1. a_r 해당일에 처음 온 경우, a_r insert, status 2 1-1 같은 있으면 device time
		 * update
		 * 
		 * 2. work start -> status 2 -> 0, 이것은 등록에서 함.
		 *
		 */
		result = dangerAppApiMapper.insertAppAliveReportInfo(appAliveReportEventVo);
		result = dangerAppApiMapper.updateAppAliveReportInfo(appAliveReportEventVo);

		// 해당 단말이 해당일에 worker_assigned 에 존재 하는 지 확인
		nCount = dangerAppApiMapper.selectCountWorkerAssignedByDeviceNo(appAliveReportEventVo.getDevice_no());
		if (nCount > 0) {
			logger.debug("device worker assigned exist");
		}

		String worker_uid = dangerAppApiMapper.selectDeviceInfoByDeviceNo(appAliveReportEventVo.getDevice_no());
		logger.debug("selectDeviceInfoByDeviceNo [{}]", worker_uid);

		appAliveReportEventVo.setWorker_uid(worker_uid);
		nCount = dangerAppApiMapper.selectCountWorkerAssignedByDeviceNoWorkerUid(appAliveReportEventVo);
		if (nCount > 0) {
			logger.debug("device worker assigned exist");
		} else {
			logger.debug("device worker assigned insert start");
			// 1. insert worker_assigned
			AppWorkerRegEventVo appWorkRegEventVo = new AppWorkerRegEventVo();
			appWorkRegEventVo.setDevice_no(appAliveReportEventVo.getDevice_no());
			result = dangerAppApiMapper.insertWorkerAssinged(appWorkRegEventVo);
		}

		// 2. update worker_assigned, 날이 바뀌면 전날의 alive_report 작업은 종료 처리해야함.
		// 않그러면 두줄 생김. 이전날, 당일 생성된 alive_report
		nCount = 0;
		nCount = dangerAppApiMapper.selectCountYesterDayAliveReport(appAliveReportEventVo.getDevice_no());
		if (nCount > 0) {
			result = dangerAppApiMapper.updateAppYesterDayAliveReportEnd(appAliveReportEventVo.getDevice_no());
		}

		// 3. alive report 가 플랜트를 벗어나서 완전히 종료하는 하는 경우
		if (appAliveReportEventVo.getEnd_yn() == 2) {
			result = dangerAppApiMapper.updateExitPlantAliveReportEnd(appAliveReportEventVo.getDevice_no());

			// 161010 enter_exit_record table에 해당 단말에 대한 exit_date가 null인 경우가
			// 있으면 현재시간으로 갱신, 나간것으로 갱신
			EnterExitRecordVo enterExitRecord = new EnterExitRecordVo();
			enterExitRecord.setDevice_no(appAliveReportEventVo.getDevice_no());
			result = dangerApiMapper.updateEnterExitRecord(enterExitRecord);
		}

		// 161010 단말 테이블 최근서버접속타임 갱신
		result = dangerApiMapper.updateDeviceLastServerAccessTime(appAliveReportEventVo.getDevice_no());

		return result;
	}

	// 160804 [개인폰] [신규] 작업자 등록
	@Transactional
	public int insertAppWorker(AppWorkerRegEventVo appWorkerRegEventVo) throws Exception {

		int result = 0;

		String worker_uid = dangerApiMapper.selectUid();
		appWorkerRegEventVo.setWorker_uid(worker_uid);
		/*
		 * 1. device_no = assigned(1)
		 */
		Integer count = deviceDangerApiMapper.selectCountByDangerDeviceNo(appWorkerRegEventVo.getDevice_no());

		// exist, update tbda_device
		if (count > 0) {

			// 1-1. 신규 사용자 등록으로 가정, 기존 사용자 정보가 있어도 tbda_device update
			result = deviceDangerApiMapper.updatePersonDevice(appWorkerRegEventVo);

			// 150901 신규 단말 등록시, 해당 번호의 작업 종료가 남아 있으면 강제 종료
			// 1-2. 혹시 해당 단말 번호로 작업할당된것이 있으면 작업 모두 종료
			// worker_assinged, work
			result = deviceDangerApiMapper.updateNewWorkerAssignedDeviceRelease(appWorkerRegEventVo.getDevice_no());
			result = deviceDangerApiMapper.updateNewWorkerNWorkUnReg(appWorkerRegEventVo.getDevice_no());
		} else {
			// insert new device, person
			DeviceDangerCudVo deviceDangerVo = new DeviceDangerCudVo();
			deviceDangerVo.setDevice_no(appWorkerRegEventVo.getDevice_no());
			deviceDangerVo.setWorker_uid(worker_uid);
			deviceDangerVo.setAccount_name(appWorkerRegEventVo.getWorker_name());
			deviceDangerVo.setNetwork_type(appWorkerRegEventVo.getDevice_network_type());
			result = deviceDangerApiMapper.insertDeviceDangerWorker(deviceDangerVo);
		}

		/*
		 * 2. insert tbda_worker
		 */
		result = dangerAppApiMapper.insertAppWorker(appWorkerRegEventVo);

		return result;
	}

	// 160804 [개인폰] [신규] 단말 서버 연결 체크
	@Transactional
	public int updateAppServerAccessTime(AppServerAccessEventVo appServerAccessEventVo) throws Exception {
		return dangerAppApiMapper.updateAppServerAccessTime(appServerAccessEventVo);
	}

}

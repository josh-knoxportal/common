package com.nemustech.platform.lbs.wwms.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.common.vo.DateVo;
import com.nemustech.platform.lbs.ngms.vo.AppDeviceInfoVo;
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

@Repository(value = "DangerApiMapper")
public interface DangerApiMapper {
	public DateVo getLastModifiedGpsZone() throws Exception;

	public DateVo getLastModifiedBeaconZone() throws Exception;

	public List<GpsZoneVo> selectGpsZoneList() throws Exception;

	public List<BeaconZoneVo> selectBeaconZoneList() throws Exception;

	public int insertAppBeaconEvent(AppZoneEventVo appZoneEventVo) throws Exception;

	public int insertAppGpsEvent(AppZoneEventVo appZoneEventVo) throws Exception;

	public int updateLastZoneEvent(AppZoneEventVo appZoneEventVo) throws Exception;

	public int cntWorking(String work_uid) throws Exception;

	public int updateGpsBleEvent(AppGpsBleEventVo appGpsBleEventVo) throws Exception;

	public int updateWorkUnReg(AppWorkUnRegEventVo appWorkUnRegEventVo) throws Exception;

	public int updateDeviceRelease(AppWorkUnRegEventVo appWorkUnRegEventVo) throws Exception;

	public int insertAppWorkReg(AppWorkRegEventVo appWorkRegEventVo) throws Exception;

	public int insertAppWorkType(AppWorkRegEventVo appWorkRegEventVo) throws Exception;

	public int deleteAppWorkType(AppWorkRegEventVo appWorkRegEventVo) throws Exception;

	public int updateAppWorkRegDevice(AppWorkRegEventVo appWorkRegEventVo) throws Exception;

	public int cntAppWorkReg(String work_uid) throws Exception;

	public int cntAppWorkRegDevice(String devie_no) throws Exception;

	public int updateAppWorkReg(AppWorkRegEventVo appWorkRegEventVo) throws Exception;

	public String selectFactoryName(String factory_uid) throws Exception;

	public String selectZoneName(String zone_uid) throws Exception;

	public String makeWorkNo(String name) throws Exception;

	public String selectUid() throws Exception;

	public WorkerMntVo selectWork(String work_uid) throws Exception;

	public Map<String, Object> selectAlram() throws Exception;

	public int updateAppDevicePushId(AppPwdEventVo appPwdEventVo) throws Exception;

	public DeviceDangerVo selectDeviceInfo(String device_no) throws Exception;

	public AppWorkRegEventVo selectWorkInfo(String work_no) throws Exception;

	// [개인폰] [추가]
	public int insertWorkerAssinged(AppWorkRegEventVo appWorkRegEventVo) throws Exception;

	// [개인폰] [추가] 작업종료시 할당작업자 테이블 갱신
	public int updateAssignedDeviceRelease(AppWorkUnRegEventVo appWorkUnRegEventVo) throws Exception;

	// 단말에 할당된 작업 개수
	public int cntWorkerAssignedByDevice(String device_no) throws Exception;

	public int selectIsCompletedAssignedWork(AppWorkUnRegEventVo appWorkUnRegEventVo) throws Exception;

	public int updateNWorkUnReg(AppWorkUnRegEventVo appWorkUnRegEventVo) throws Exception;

	// 160903
	public int selectCountWorkAssignedTypeTwo(String device_no) throws Exception;

	// 160903
	public int updateWorkerAssigned(AppWorkRegEventVo appWorkRegEventVo) throws Exception;

	public int updateWorkerAssignLastZoneEvent(AppZoneEventVo appZoneEventVo) throws Exception;

	public String selectDeviceWorkerUidByDeviceUid(String device_uid) throws Exception;

	public int insertEnterExitRecord(EnterExitRecordVo enterExitRecord) throws Exception;

	public Integer selectCountEnterExitRecord(EnterExitRecordVo enterExitRecord) throws Exception;

	public int updateEnterExitRecord(EnterExitRecordVo enterExitRecord) throws Exception;

	// 160909
	public List<AppZoneEventVo> selectRestrictedList() throws Exception;

	public int updateAssignedDeviceReleaseByWorkRegister(String device_no) throws Exception;

	public int updateDeviceReleaseByWorkRegister(String device_no) throws Exception;

	public int updateNWorkUnRegByWorkRegister() throws Exception;

	public int updateEnterExitRecodeByWorkRegister(String device_no) throws Exception;

	public int updateDeviceNetworkType(AppDeviceInfoVo appDeviceInfoVo) throws Exception;

	// 160619
	public int selectCountEnterExitRecordByDeviceNO(String device_no) throws Exception;

	public EnterExitRecordVo selectEnterExitRecordByDeviceNo(String device_no) throws Exception;

	public int updateExitDateByCurDate(String device_no) throws Exception;

	public int selectDeviceFactoryUidByDeviceNo(String device_no) throws Exception;

	public int updateEnterExitRecordByCompanyDevice(AppWorkUnRegEventVo appWorkUnRegEventVo) throws Exception;

	public int updateDeviceLastServerAccessTime(String device_no) throws Exception;

}

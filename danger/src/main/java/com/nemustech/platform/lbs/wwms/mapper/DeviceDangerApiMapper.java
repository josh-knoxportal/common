package com.nemustech.platform.lbs.wwms.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.common.vo.SearchVo;
import com.nemustech.platform.lbs.wwms.vo.AppWorkerRegEventVo;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerCudVo;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerPasswordVo;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerVo;

@Repository
public interface DeviceDangerApiMapper {

	List<DeviceDangerVo> selectDeviceDangerList(SearchVo searchVo) throws Exception;

	int selectDeviceDangerListTotalCount(SearchVo searchVo) throws Exception;

	DeviceDangerCudVo selectDeviceDanger(DeviceDangerVo deviceDangerVo) throws Exception;

	int insertDeviceDanger(DeviceDangerCudVo deviceDangerVo) throws Exception;

	int updateDeviceDanger(DeviceDangerCudVo deviceDangerVo) throws Exception;

	int deleteDeviceDangerByUid(DeviceDangerCudVo deviceDangerVo) throws Exception;

	int updateDevicePasswordDanger(DeviceDangerPasswordVo deviceDangerPasswordVo) throws Exception;

	String selectDevicePasswordDanger() throws Exception;

	Integer selectCountByDangerDeviceNo(String device_no) throws Exception;

	DeviceDangerCudVo selectDeviceDangerByDeviceNo(String device_no) throws Exception;

	int insertDeviceDangerWorker(DeviceDangerCudVo deviceDangerVo) throws Exception;

	int updatePersonDevice(AppWorkerRegEventVo appWorkerRegEventVo) throws Exception;

	// 150901 신규 단말 등록시, 해당 번호의 작업 종료가 남아 있으면 강제 종료
	int updateNewWorkerAssignedDeviceRelease(String device_no) throws Exception;

	// 150901 신규 단말 등록시, 해당 번호의 작업 종료가 남아 있으면 강제 종료
	int updateNewWorkerNWorkUnReg(String device_no) throws Exception;

}

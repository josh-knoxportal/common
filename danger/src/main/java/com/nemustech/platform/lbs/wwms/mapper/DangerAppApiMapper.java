package com.nemustech.platform.lbs.wwms.mapper;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.wwms.vo.AppAliveReportEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppServerAccessEventVo;
import com.nemustech.platform.lbs.wwms.vo.AppWorkerRegEventVo;

@Repository(value = "DangerAppApiMapper")
public interface DangerAppApiMapper {

	// 160803 [개인폰] [신규] alvie report event
	public int insertAppAliveReportInfo(AppAliveReportEventVo appAliveReportEventVo) throws Exception;

	// 160803 [개인폰] [신규] alvie report event
	public int updateAppAliveReportInfo(AppAliveReportEventVo appAliveReportEventVo) throws Exception;

	// 160804 [개인폰] [신규] 작업자 등록
	public int insertAppWorker(AppWorkerRegEventVo appWorkerRegEventVo) throws Exception;

	// 160804 [개인폰] [신규] 단말 서버 연결 체크
	public int updateAppServerAccessTime(AppServerAccessEventVo appServerAccessEventVo) throws Exception;

	// 160803 [개인폰] [신규] alvie report event
	public Integer selectCountWorkerAssignedByDeviceNo(String device_no) throws Exception;

	// 160903 alvie report event 수정
	public int insertWorkerAssinged(AppWorkerRegEventVo appWorkRegEventVo) throws Exception;

	// 160903 alvie report event 수정
	public int updateAppYesterDayAliveReportEnd(String device_no) throws Exception;

	// 160903 alvie report event 수정
	public Integer selectCountYesterDayAliveReport(String device_no) throws Exception;

	// 160903 alvie report event 수정
	public int updateExitPlantAliveReportEnd(String device_no) throws Exception;

	public Integer selectCountWorkerAssignedByDeviceNoWorkerUid(AppAliveReportEventVo appAliveReportEventVo)
			throws Exception;

	public String selectDeviceInfoByDeviceNo(String device_no) throws Exception;

}

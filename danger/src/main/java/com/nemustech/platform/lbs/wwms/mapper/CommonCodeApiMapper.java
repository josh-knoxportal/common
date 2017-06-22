package com.nemustech.platform.lbs.wwms.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.common.vo.DateVo;
import com.nemustech.platform.lbs.common.vo.DeviceBeaconVo;
import com.nemustech.platform.lbs.common.vo.DeviceEcgiVo;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleDestinationTypeVo;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleDestinationVo;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleTypeVo;
import com.nemustech.platform.lbs.wwms.vo.CodeFactoryZoneVo;
import com.nemustech.platform.lbs.wwms.vo.CodeOrganizationVo;
import com.nemustech.platform.lbs.wwms.vo.CodeWorkNoVo;
import com.nemustech.platform.lbs.wwms.vo.CodeWorkTypeVo;
import com.nemustech.platform.lbs.wwms.vo.ParterNameVo;
import com.nemustech.platform.lbs.wwms.vo.ServerConfigVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerContactRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerContactVo;

@Repository(value = "CommonCodeApiMapper")
public interface CommonCodeApiMapper {
	public List<CodeWorkTypeVo> selectWorkTypeList() throws Exception;

	public List<CodeWorkNoVo> selectWorkNoList() throws Exception;

	public List<ParterNameVo> selectParterNameList() throws Exception;

	public List<CodeFactoryZoneVo> selectFactoryList() throws Exception;

	public List<CodeFactoryZoneVo> selectZoneList() throws Exception;

	/* [2016/05/26] */
	public List<WorkerContactVo> selectWorkerContactListOnFactoryZone(WorkerContactRequestVo requestVo)
			throws Exception;

	// 160804 [개인폰] [신규] 부서/협력업체 정보
	public List<CodeOrganizationVo> selectOrganizationList() throws Exception;

	// 160804 [개인폰] [신규] 차량유형 정보
	public List<CodeVehicleTypeVo> selectVehicleTypeList() throws Exception;

	// 160804 [개인폰] [신규] [안전운행15] 목적지 정보 조회
	public List<CodeVehicleDestinationVo> selectDestinationList() throws Exception;

	// 160804 [개인폰] [신규] [안전운행15] 목적지 정보 조회
	public List<CodeVehicleDestinationTypeVo> selectDestinationTypeList() throws Exception;

	// 1608 [신규] [공통] 단말 ECGI 정보 조회
	public List<DeviceEcgiVo> selectDeviceEcgiList() throws Exception;

	// 1608 [신규] [공통] 단말 ECGI 정보 조회
	public List<DeviceBeaconVo> selectDeviceBeaconList() throws Exception;

	// 1608 [신규] [공통] 목적지 정보의 최신 갱신 여부
	public DateVo getIsLastModifiedDestination() throws Exception;

	// 1608 [신규] [공통] ECGI 정보의 최신 갱신 여부
	public DateVo getIsLastModifiedEcgi() throws Exception;

	// 1608 [신규] [공통] 차량유형 정보의 최신 갱신 여부
	public DateVo getIsLastModifiedVehicleType() throws Exception;

	// 1609 [신규] [공통] 출입감지 작업유형 정보의 최신 갱신 여부
	public DateVo getIsLastModifiedCodeWorkTypeList() throws Exception;

	public ServerConfigVo getServerConfig() throws Exception;

	public DateVo getIsLastModifiedCodeWorkNoList() throws Exception;

	public DateVo getIsLastModifiedAppRuleProperty() throws Exception;

	public DateVo getIsLastModifiedCodeOrganizationList() throws Exception;

	public List<CodeWorkNoVo> selectBeforeStartWorkNoList() throws Exception;

	public List<CodeWorkNoVo> selectWorkNoListByDeviceNo(String device_no) throws Exception;

}

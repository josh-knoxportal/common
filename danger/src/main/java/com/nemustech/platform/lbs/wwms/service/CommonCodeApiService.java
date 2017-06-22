package com.nemustech.platform.lbs.wwms.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nemustech.platform.lbs.common.vo.DateVo;
import com.nemustech.platform.lbs.common.vo.DeviceBeaconVo;
import com.nemustech.platform.lbs.common.vo.DeviceEcgiVo;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleDestinationTypeVo;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleDestinationVo;
import com.nemustech.platform.lbs.ngms.vo.CodeVehicleTypeVo;
import com.nemustech.platform.lbs.wwms.mapper.CommonCodeApiMapper;
import com.nemustech.platform.lbs.wwms.vo.CodeFactoryZoneVo;
import com.nemustech.platform.lbs.wwms.vo.CodeOrganizationVo;
import com.nemustech.platform.lbs.wwms.vo.CodeWorkNoVo;
import com.nemustech.platform.lbs.wwms.vo.CodeWorkTypeVo;
import com.nemustech.platform.lbs.wwms.vo.ParterNameVo;
import com.nemustech.platform.lbs.wwms.vo.ServerConfigVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerContactRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerContactVo;

@Service(value = "CommonCodeApiService")
public class CommonCodeApiService {

	private static final Logger logger = LoggerFactory.getLogger(CommonCodeApiService.class);

	private static String FZTAG_ALL = "all";
	private static String FZTAG_FACTORY = "factory";
	private static String FZTAG_ZONE = "zone";

	@Autowired
	private CommonCodeApiMapper commonCodeApiMapper;

	public List<CodeWorkTypeVo> workTypeList() throws Exception {
		logger.debug("workTypeList ... ");
		return commonCodeApiMapper.selectWorkTypeList();
	}

	public List<CodeWorkNoVo> workNoList() throws Exception {
		logger.debug("workNoList ... ");
		return commonCodeApiMapper.selectWorkNoList();
	}

	public List<ParterNameVo> parterNameList() throws Exception {
		logger.debug("parterNameList ... ");
		return commonCodeApiMapper.selectParterNameList();
	}

	/* */
	public List<CodeFactoryZoneVo> factoryZoneList(String tag) throws Exception {
		List<CodeFactoryZoneVo> list = new ArrayList<CodeFactoryZoneVo>();

		if (FZTAG_ALL.equals(tag) || FZTAG_FACTORY.equals(tag)) {
			list.addAll(commonCodeApiMapper.selectFactoryList());
		}

		if (FZTAG_ALL.equals(tag) || FZTAG_ZONE.equals(tag)) {
			list.addAll(commonCodeApiMapper.selectZoneList());
		}

		return list;
	}

	/* [2016/05/26] */
	public List<WorkerContactVo> workerContactListOnFactoryZone(WorkerContactRequestVo requestVo) throws Exception {
		return commonCodeApiMapper.selectWorkerContactListOnFactoryZone(requestVo);
	}

	// 160804 [개인폰] [신규] 부서/협력업체 정보
	public List<CodeOrganizationVo> getOrganizationList() throws Exception {
		return commonCodeApiMapper.selectOrganizationList();
	}

	// 160804 [개인폰] [신규] 차량유형 정보
	public List<CodeVehicleTypeVo> selectVehicleTypeList() throws Exception {
		return commonCodeApiMapper.selectVehicleTypeList();
	}

	// 160804 [개인폰] [신규] [안전운행15] 목적지 정보 조회
	public List<CodeVehicleDestinationVo> selectDestinationList() throws Exception {
		return commonCodeApiMapper.selectDestinationList();
	}

	// 160804 [개인폰] [신규] [안전운행15] 목적지 정보 조회
	public List<CodeVehicleDestinationTypeVo> selectDestinationTypeList() throws Exception {
		return commonCodeApiMapper.selectDestinationTypeList();
	}

	// 1608 [신규] [공통] 단말 ECGI 정보 조회
	public List<DeviceEcgiVo> selectDeviceEcgiList() throws Exception {
		return commonCodeApiMapper.selectDeviceEcgiList();
	}

	// 1608 [신규] [공통] 단말 ECGI 정보 조회
	public List<DeviceBeaconVo> selectDeviceBeaconList() throws Exception {
		return commonCodeApiMapper.selectDeviceBeaconList();
	}

	// 1608 [신규] [공통] 목적지 정보의 최신 갱신 여부
	public boolean getIsLastModifiedDestination(String if_modified_since) throws Exception {
		DateVo dateVo = commonCodeApiMapper.getIsLastModifiedDestination();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);
		logger.info("date : " + param);
		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	// 1608 [신규] [공통] ECGI 정보의 최신 갱신 여부
	public boolean getIsLastModifiedEcgi(String if_modified_since) throws Exception {
		DateVo dateVo = commonCodeApiMapper.getIsLastModifiedEcgi();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);
		logger.info("date : " + param);
		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	// 1608 [신규] [공통] 차량유형 정보의 최신 갱신 여부
	public boolean getIsLastModifiedVehicleType(String if_modified_since) throws Exception {
		DateVo dateVo = commonCodeApiMapper.getIsLastModifiedVehicleType();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);
		logger.info("date : " + param);
		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	// 1609 [신규] [공통] 출입감지 작업유형 정보의 최신 갱신 여부
	public boolean getIsLastModifiedCodeWorkTypeList(String if_modified_since) throws Exception {
		DateVo dateVo = commonCodeApiMapper.getIsLastModifiedCodeWorkTypeList();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);
		logger.info("date : " + param);
		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	public ServerConfigVo getServerConfig() throws Exception {
		return commonCodeApiMapper.getServerConfig();
	}

	public boolean getIsLastModifiedCodeWorkNoList(String if_modified_since) throws Exception {
		DateVo dateVo = commonCodeApiMapper.getIsLastModifiedCodeWorkNoList();
		if (StringUtils.isEmpty(dateVo)) {
			return false;
		} else {
			DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
			Date param = sdFormat.parse(if_modified_since);
			logger.info("date : " + param);
			if (dateVo.getUpd_date().after(param) == true) {
				return true;
			}
		}
		return false;
	}

	public boolean getIsLastModifiedAppRuleProperty(String if_modified_since) throws Exception {
		DateVo dateVo = commonCodeApiMapper.getIsLastModifiedAppRuleProperty();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);
		logger.info("date : " + param);
		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	public boolean getIsLastModifiedCodeOrganizationList(String if_modified_since) throws Exception {
		DateVo dateVo = commonCodeApiMapper.getIsLastModifiedCodeOrganizationList();
		DateFormat sdFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", new Locale("en", "US"));
		Date param = sdFormat.parse(if_modified_since);
		logger.info("date : " + param);
		if (dateVo.getUpd_date().after(param) == true) {
			return true;
		}
		return false;
	}

	public List<CodeWorkNoVo> selectBeforeStartWorkNoList() throws Exception {
		return commonCodeApiMapper.selectBeforeStartWorkNoList();
	}

	public List<CodeWorkNoVo> workNoListByDeviceNo(String device_no) throws Exception {
		logger.debug("workNoListByDeviceNo ... ");
		return commonCodeApiMapper.selectWorkNoListByDeviceNo(device_no);
	}

}

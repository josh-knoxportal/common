package com.nemustech.platform.lbs.wwms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nemustech.platform.lbs.common.vo.SearchVo;
import com.nemustech.platform.lbs.wwms.mapper.DeviceDangerApiMapper;
import com.nemustech.platform.lbs.wwms.model.DeviceDangerCud;
import com.nemustech.platform.lbs.wwms.model.DeviceDangerList;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerCudVo;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerPasswordVo;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerVo;

@Service
public class DeviceDangerApiService {

	private static final Logger logger = LoggerFactory.getLogger(DeviceDangerApiService.class);

	@Autowired
	private DeviceDangerApiMapper deviceDangerApiMapper;

	public DeviceDangerList selectDeviceDangerList(SearchVo searchVo) throws Exception {
		logger.info("selectDeviceDangerList");
		DeviceDangerList list = new DeviceDangerList();
		list.setDeviceDangerList(deviceDangerApiMapper.selectDeviceDangerList(searchVo));
		list.setTotalCnt(deviceDangerApiMapper.selectDeviceDangerListTotalCount(searchVo));
		return list;
	}

	public DeviceDangerCud selectDeviceDanger(DeviceDangerVo deviceDangerVo) throws Exception {
		logger.info("selectDeviceDangerList");
		DeviceDangerCud info = new DeviceDangerCud();
		info.setDeviceDangerCudVo(deviceDangerApiMapper.selectDeviceDanger(deviceDangerVo));
		return info;
	}

	public int deviceDangerCud(DeviceDangerCudVo deviceDangerCudVo, String opcode) throws Exception {

		logger.info("deviceDangerCud opcode[{}]", opcode);

		int nCudResult = 0;

		if ("create".equals(opcode)) {
			nCudResult = createDeviceDanger(deviceDangerCudVo);
		} else if ("update".equals(opcode)) {
			nCudResult = modifyDeviceDanger(deviceDangerCudVo);
		} else if ("deleteByUid".equals(opcode)) {
			nCudResult = removeDeviceDangerByUid(deviceDangerCudVo);
		} else {
			logger.info("deviceDangerCud opcode not matching, return");
		}

		logger.info("deviceDangerCud opcode[{}] nCudResult[{}]", opcode, nCudResult);

		return nCudResult;
	}

	@Transactional
	private int createDeviceDanger(DeviceDangerCudVo deviceDangerVo) throws Exception {
		return deviceDangerApiMapper.insertDeviceDanger(deviceDangerVo);
	}

	@Transactional
	private int modifyDeviceDanger(DeviceDangerCudVo deviceDangerVo) throws Exception {
		return deviceDangerApiMapper.updateDeviceDanger(deviceDangerVo);
	}

	@Transactional
	private int removeDeviceDangerByUid(DeviceDangerCudVo deviceDangerVo) throws Exception {
		return deviceDangerApiMapper.deleteDeviceDangerByUid(deviceDangerVo);
	}

	@Transactional
	public int modifyDevicePasswordDanger(DeviceDangerPasswordVo deviceDangerPasswordVo) throws Exception {
		return deviceDangerApiMapper.updateDevicePasswordDanger(deviceDangerPasswordVo);
	}

	public String selectDevicePasswordDanger() throws Exception {
		logger.info("selectDevicePasswordDanger");
		return deviceDangerApiMapper.selectDevicePasswordDanger();
	}

	public boolean isExistDeviceNo(String device_no) throws Exception {
		Integer count = deviceDangerApiMapper.selectCountByDangerDeviceNo(device_no);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

}

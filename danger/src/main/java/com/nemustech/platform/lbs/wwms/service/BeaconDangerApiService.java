package com.nemustech.platform.lbs.wwms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nemustech.platform.lbs.common.vo.SearchVo;
import com.nemustech.platform.lbs.wwms.mapper.BeaconDangerApiMapper;
import com.nemustech.platform.lbs.wwms.model.BeaconDangerCud;
import com.nemustech.platform.lbs.wwms.model.BeaconDangerList;
import com.nemustech.platform.lbs.wwms.vo.BeaconDangerVo;

@Service
public class BeaconDangerApiService {
	private static final Logger logger = LoggerFactory.getLogger(BeaconDangerApiService.class);

	@Autowired
	private BeaconDangerApiMapper beaconDangerApiMapper;

	public BeaconDangerList selectBeaconDangerList(SearchVo searchVo) throws Exception {
		logger.info("BeaconDangerApiService selectBeaconDangerList");
		BeaconDangerList list = new BeaconDangerList();
		list.setBeaconDangerList(beaconDangerApiMapper.selectBeaconDangerList(searchVo));
		logger.info("BeaconDangerApiService selectBeaconDangerList1");
		list.setTotalCnt(beaconDangerApiMapper.selectBeaconDangerListTotalCount(searchVo));
		logger.info("BeaconDangerApiService selectBeaconDangerList2");
		list.setBeaconActivate(beaconDangerApiMapper.selectBeaconDangerActivateCount());
		logger.info("BeaconDangerApiService selectBeaconDangerList3");
		return list;
	}

	public BeaconDangerCud selectBeaconDanger(BeaconDangerVo beaconDangerVo) throws Exception {
		logger.info("selectBeaconDangerList");
		BeaconDangerCud info = new BeaconDangerCud();
		info.setBeaconDangerVo(beaconDangerApiMapper.selectBeaconDanger(beaconDangerVo));
		return info;
	}

	public int beaconDangerCud(BeaconDangerVo beaconDangerVo, String opcode) throws Exception {

		logger.info("beaconDangerCud opcode[{}]", opcode);

		int nCudResult = 0;

		if ("update".equals(opcode)) {
			nCudResult = modifyBeaconDanger(beaconDangerVo);
		} else if ("deleteByUid".equals(opcode)) {
			nCudResult = removeBDangerByUid(beaconDangerVo);
		} else {
			logger.info("beaconDangerCud opcode not matching, return");
		}

		logger.info("beaconDangerCud opcode[{}] nCudResult[{}]", opcode, nCudResult);

		return nCudResult;
	}

	@Transactional
	private int modifyBeaconDanger(BeaconDangerVo beaconDangerVo) throws Exception {
		return beaconDangerApiMapper.updateBeaconDanger(beaconDangerVo);
	}

	@Transactional
	private int removeBDangerByUid(BeaconDangerVo beaconDangerVo) throws Exception {
		return beaconDangerApiMapper.deleteBeaconDangerByUid(beaconDangerVo);
	}
}

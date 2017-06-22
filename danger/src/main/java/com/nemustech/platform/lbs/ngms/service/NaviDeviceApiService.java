package com.nemustech.platform.lbs.ngms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nemustech.platform.lbs.ngms.mapper.NaviDeviceApiMapper;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceAssignedVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceSearchVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceVo;

@Service(value = "naviDeviceApiService")
public class NaviDeviceApiService {

	private static final Logger logger = LoggerFactory.getLogger(NaviDeviceApiService.class);

	@Autowired
	private NaviDeviceApiMapper naviDeviceApiMapper;

	public NaviDeviceVo selectNaviDevice(NaviDeviceVo naviDeviceVo) throws Exception {
		logger.info("안전운행 단말기 검색");
		return naviDeviceApiMapper.selectNaviDevice(naviDeviceVo);
	}

	public int updateNaviDevice(NaviDeviceVo naviDeviceVo) throws Exception {
		logger.info("안전운행 단말기 업데이트");
		return naviDeviceApiMapper.updateNaviDevice(naviDeviceVo);
	}

	public List<NaviDeviceVo> selectNaviDeviceList(NaviDeviceSearchVo searchVo) throws Exception {
		logger.info("안전운행 단말기 리스트 검색");
		return naviDeviceApiMapper.selectNaviDeviceList(searchVo);
	}

	public int selectNaviDeviceListTotalCnt(NaviDeviceSearchVo searchVo) throws Exception {
		logger.info("안전운행 단말기 리스트 검색");
		return naviDeviceApiMapper.selectNaviDeviceListTotalCnt(searchVo);
	}

	public int createNaviDevice(NaviDeviceVo naviDeviceVo) throws Exception {
		logger.info("안전운행 단말기 추가");
		return naviDeviceApiMapper.insertNaviDevice(naviDeviceVo);
	}

	public int removeNaviDevie(NaviDeviceVo naviDeviceVo) throws Exception {
		logger.info("안전운행 단말기 삭제");
		return naviDeviceApiMapper.deleteNaviDevice(naviDeviceVo);
	}

	public NaviDeviceAssignedVo selectNaviDeviceAssignedCount() throws Exception {
		logger.info("안전운행 단말기 할당 개수");
		return naviDeviceApiMapper.selectNaviDeviceAssignedCount();
	}

	public int releaseNaviDevice(NaviDeviceVo naviDeviceVo) throws Exception {
		logger.info("안전운행 단말기 Release");
		int result = 0;

		result = naviDeviceApiMapper.releaseNaviDevice(naviDeviceVo);

		// 160804 [웹 단말관리 해제] 해당 단말의 등록삭제시 차량 테이블 is_out = 1 적용
		result = naviDeviceApiMapper.updateVehicleByReleaseDevices(naviDeviceVo);

		return result;
	}

	public int blockNaviDevice(NaviDeviceVo naviDeviceVo) throws Exception {
		logger.info("안전운행 단말기 Block");
		return naviDeviceApiMapper.blockNaviDevice(naviDeviceVo);
	}

	public List<NaviDeviceVo> selectBlockNaviDeviceList(NaviDeviceSearchVo searchVo) throws Exception {
		logger.info("안전운행 단말기 차단리스트 검색");
		return naviDeviceApiMapper.selectBlockNaviDeviceList(searchVo);
	}

	public int selectBlockNaviDeviceListTotalCnt(NaviDeviceSearchVo searchVo) throws Exception {
		logger.info("안전운행 단말기 차단리스트 검색");
		return naviDeviceApiMapper.selectBlockNaviDeviceListTotalCnt(searchVo);
	}

	public boolean isExistDeviceNo(String device_no) throws Exception {
		Integer count = naviDeviceApiMapper.selectCountByVehicleDeviceNo(device_no);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
}

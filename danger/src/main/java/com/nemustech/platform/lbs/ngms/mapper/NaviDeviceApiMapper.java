package com.nemustech.platform.lbs.ngms.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.ngms.vo.NaviDeviceAssignedVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceSearchVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceVo;

@Repository(value = "NaviDeviceApiMapper")
public interface NaviDeviceApiMapper {

	public NaviDeviceVo selectNaviDevice(NaviDeviceVo naviDeviceVo) throws Exception;

	public int updateNaviDevice(NaviDeviceVo naviDeviceVo) throws Exception;

	public List<NaviDeviceVo> selectNaviDeviceList(NaviDeviceSearchVo searchVo) throws Exception;

	public int selectNaviDeviceListTotalCnt(NaviDeviceSearchVo searchVo) throws Exception;

	public int insertNaviDevice(NaviDeviceVo naviDeviceVo) throws Exception;

	public int deleteNaviDevice(NaviDeviceVo naviDeviceVo) throws Exception;

	public NaviDeviceAssignedVo selectNaviDeviceAssignedCount() throws Exception;

	public int releaseNaviDevice(NaviDeviceVo naviDeviceVo) throws Exception;

	public Integer selectCountByVehicleDeviceNo(String device_no) throws Exception;

	public int blockNaviDevice(NaviDeviceVo naviDeviceVo) throws Exception;

	public List<NaviDeviceVo> selectBlockNaviDeviceList(NaviDeviceSearchVo searchVo) throws Exception;

	public int selectBlockNaviDeviceListTotalCnt(NaviDeviceSearchVo searchVo) throws Exception;

	// 160804 [개인폰] [신규] [안전운행 11] 개인 단말 차량 등록
	public int insertAppPersonDeviceRegister(NaviDeviceVo naviDeviceVo) throws Exception;

	// 160804 [개인폰] [신규] [안전운행 11] 개인 단말 차량 등록
	public int updateAppPersonDeviceRegister(NaviDeviceVo naviDeviceVo) throws Exception;

	// 160804 [웹 단말관리 해제] 해당 단말의 등록삭제시 차량 테이블 is_out = 1 적용
	public int updateVehicleByReleaseDevices(NaviDeviceVo naviDeviceVo) throws Exception;
}

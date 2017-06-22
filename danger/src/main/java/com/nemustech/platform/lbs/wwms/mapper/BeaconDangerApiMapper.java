package com.nemustech.platform.lbs.wwms.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.common.vo.SearchVo;
import com.nemustech.platform.lbs.wwms.vo.BeaconDangerActivateVo;
import com.nemustech.platform.lbs.wwms.vo.BeaconDangerVo;

@Repository
public interface BeaconDangerApiMapper {

	List<BeaconDangerVo> selectBeaconDangerList(SearchVo searchVo) throws Exception;

	int selectBeaconDangerListTotalCount(SearchVo searchVo) throws Exception;

	BeaconDangerVo selectBeaconDanger(BeaconDangerVo beaconDangerVo) throws Exception;

	BeaconDangerActivateVo selectBeaconDangerActivateCount() throws Exception;

	int updateBeaconDanger(BeaconDangerVo beaconDangerVo) throws Exception;

	int deleteBeaconDangerByUid(BeaconDangerVo beaconDangerVo) throws Exception;

}

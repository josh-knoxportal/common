package com.nemustech.platform.lbs.common.mapper;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.common.vo.PushHistoryVo;
import com.nemustech.platform.lbs.common.vo.PushTargetVo;


@Repository(value = "PushApiMapper")
public interface PushApiMapper {
	public String selectUid() throws Exception;
	public int insertPushHistory(PushHistoryVo pushHistoryVo) throws Exception;
	public int insertPushTarget(PushHistoryVo pushHistoryVo) throws Exception;
	public int updatePushTarget(PushTargetVo pushTargetVo) throws Exception;
	
	public List<String> selectDangerPushIdList(PushHistoryVo pushHistoryVo) throws Exception;
	public List<String> selectVehiclePushIdList(PushHistoryVo pushHistoryVo) throws Exception;
	
}

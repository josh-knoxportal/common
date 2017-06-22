package com.nemustech.platform.lbs.lpms.mapper;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.lpms.vo.LpmsWorkTypeVo;
import com.nemustech.platform.lbs.lpms.vo.LpmsWorkVo;

@Repository
public interface LpmsApiMapper {

	int insertLpmsWorkData(LpmsWorkVo lpmsWorkVo) throws Exception;

	int insertLpmsWorkTypeData(LpmsWorkTypeVo lpmsWorkVo) throws Exception;

	String selectWorkUid() throws Exception;

	int selectWorkCount(String strToday) throws Exception;

	int selectLpmsDailDataCount(String strToday) throws Exception;

	int selectExistWorkNo(String request_no) throws Exception;

	int insertLpmsDailyData(LpmsWorkVo lpmsWorkVo) throws Exception;
}

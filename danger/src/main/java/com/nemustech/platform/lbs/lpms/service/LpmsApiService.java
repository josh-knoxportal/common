package com.nemustech.platform.lbs.lpms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nemustech.platform.lbs.lpms.mapper.LpmsApiMapper;
import com.nemustech.platform.lbs.lpms.vo.LpmsWorkTypeVo;
import com.nemustech.platform.lbs.lpms.vo.LpmsWorkVo;
import com.nemustech.platform.lbs.lpms.vo.LpmsWorksVo;
import com.nemustech.platform.lbs.lpms.vo.WorkType;

@Service
public class LpmsApiService {
	private static final Logger logger = LoggerFactory.getLogger(LpmsApiService.class);
	public static final String WORKTYPE_Y = "Y";

	@Autowired
	LpmsApiMapper lpmsMapper;

	public int insertLpmsData(LpmsWorksVo lpmsWorksVo) throws Exception {
		int result = 0;
		LpmsWorkVo lpmsWorkVo = null;

		for (int i = 0; i < lpmsWorksVo.getLpmsWorksVo().size(); i++) {

			logger.debug("lpms data index [{}]", i);

			lpmsWorkVo = lpmsWorksVo.getLpmsWorksVo().get(i);
			if (!StringUtils.isEmpty(lpmsWorkVo)) {

				logger.debug("lpms data input index [{}]", i);
				try {
					result = lpmsMapper.insertLpmsDailyData(lpmsWorkVo);
				} catch (DataIntegrityViolationException e) {
					logger.debug("duplicate key");
				} catch (Exception e) {
					logger.debug("Exception [{}]", e.getMessage());
				}
			}
		}

		for (int i = 0; i < lpmsWorksVo.getLpmsWorksVo().size(); i++) {

			logger.debug("lpms data index [{}]", i);

			lpmsWorkVo = lpmsWorksVo.getLpmsWorksVo().get(i);
			if (!StringUtils.isEmpty(lpmsWorkVo)) {

				logger.debug("lpms data input index [{}]", i);

				if (!StringUtils.isEmpty(lpmsWorkVo.getRequest_no())) {
					if (!isExistWorNo(lpmsWorkVo.getRequest_no())) {
						insertLpmsDailyDataToWork(lpmsWorkVo);
					}
				}
			}
		}

		return result;
	}

	private boolean isExistWorNo(String request_no) {
		int nCountExistWorkNo = 0;
		try {
			nCountExistWorkNo = lpmsMapper.selectExistWorkNo(request_no);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		logger.info("nCountExistWorkNo [{}]", nCountExistWorkNo);
		if (nCountExistWorkNo > 0)
			return true;
		else
			return false;
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	private void insertLpmsDailyDataToWork(LpmsWorkVo lpmsWorkVo) {
		try {

			// 1. make_uid
			String work_uid = lpmsMapper.selectWorkUid();
			logger.debug("lpms work_uid [{}]", work_uid);

			lpmsWorkVo.setWork_uid(work_uid);

			// 2. insert work
			logger.debug("lpms insertLpmsWorkData");
			lpmsMapper.insertLpmsWorkData(lpmsWorkVo);

			// 3. insert work_type
			insertWorkType(lpmsWorkVo);

		} catch (DataIntegrityViolationException e) {
			logger.debug("duplicate key");
		} catch (Exception e) {
			logger.debug("Exception");
		}
	}

	/*
	 * WORK_TYPE1 작업유형-일반 fire WORK_TYPE2 작업유형-밀폐공간 airtight WORK_TYPE3
	 * 작업유형-일반위험A dangerA WORK_TYPE4 작업유형-전기 electricity WORK_TYPE5 작업유형-방사선
	 * radiation WORK_TYPE6 작업유형-중장비 heavy WORK_TYPE7 작업유형-굴착 excavation
	 * WORK_TYPE8 작업유형-일반위험B dangerB WORK_TYPE9 작업유형-고소 height
	 */
	protected void insertWorkType(LpmsWorkVo lpmsWorkVo) throws Exception {

		if (!StringUtils.isEmpty(lpmsWorkVo.getWork_type1()) && WORKTYPE_Y.equals(lpmsWorkVo.getWork_type1())) {
			insetLpmsWorktypeData(lpmsWorkVo.getWork_uid(), WorkType.FIRE);
		}

		if (!StringUtils.isEmpty(lpmsWorkVo.getWork_type2()) && WORKTYPE_Y.equals(lpmsWorkVo.getWork_type2())) {
			insetLpmsWorktypeData(lpmsWorkVo.getWork_uid(), WorkType.AIRTIGHT);
		}

		if (!StringUtils.isEmpty(lpmsWorkVo.getWork_type3()) && WORKTYPE_Y.equals(lpmsWorkVo.getWork_type3())) {
			insetLpmsWorktypeData(lpmsWorkVo.getWork_uid(), WorkType.DANGERA);
		}

		if (!StringUtils.isEmpty(lpmsWorkVo.getWork_type4()) && WORKTYPE_Y.equals(lpmsWorkVo.getWork_type4())) {
			insetLpmsWorktypeData(lpmsWorkVo.getWork_uid(), WorkType.ELECTRICITY);
		}

		if (!StringUtils.isEmpty(lpmsWorkVo.getWork_type5()) && WORKTYPE_Y.equals(lpmsWorkVo.getWork_type5())) {
			insetLpmsWorktypeData(lpmsWorkVo.getWork_uid(), WorkType.RADIATION);
		}

		if (!StringUtils.isEmpty(lpmsWorkVo.getWork_type6()) && WORKTYPE_Y.equals(lpmsWorkVo.getWork_type6())) {
			insetLpmsWorktypeData(lpmsWorkVo.getWork_uid(), WorkType.HEAVY);
		}

		if (!StringUtils.isEmpty(lpmsWorkVo.getWork_type7()) && WORKTYPE_Y.equals(lpmsWorkVo.getWork_type7())) {
			insetLpmsWorktypeData(lpmsWorkVo.getWork_uid(), WorkType.EXCAVATION);
		}

		if (!StringUtils.isEmpty(lpmsWorkVo.getWork_type8()) && WORKTYPE_Y.equals(lpmsWorkVo.getWork_type8())) {
			insetLpmsWorktypeData(lpmsWorkVo.getWork_uid(), WorkType.DANGERB);
		}

		if (!StringUtils.isEmpty(lpmsWorkVo.getWork_type9()) && WORKTYPE_Y.equals(lpmsWorkVo.getWork_type9())) {
			insetLpmsWorktypeData(lpmsWorkVo.getWork_uid(), WorkType.HEIGHT);
		}
	}

	private void insetLpmsWorktypeData(String work_uid, WorkType work_type) throws Exception {
		logger.debug("lpms data input work_uid [{}] work_type [{}]", work_uid, work_type);
		LpmsWorkTypeVo workTypeVo = new LpmsWorkTypeVo();
		workTypeVo.setWork_uid(work_uid);
		workTypeVo.setWork_type(work_type.getValue());
		lpmsMapper.insertLpmsWorkTypeData(workTypeVo);
	}

	public int selectWorkCount(String strToday) throws Exception {
		return lpmsMapper.selectWorkCount(strToday);
	}

}
package com.nemustech.platform.lbs.web.vehicle.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nemustech.platform.lbs.ngms.service.NaviDeviceApiService;
import com.nemustech.platform.lbs.web.vehicle.mapper.AdminAccountApiMapper;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountCudVo;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountSearchVo;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountVo;

@Service(value = "adminAccountApiService")
public class AdminAccountApiService {

	private static final Logger logger = LoggerFactory.getLogger(NaviDeviceApiService.class);

	@Autowired
	private AdminAccountApiMapper adminAccountApiMapper;

	public List<AdminAccountVo> selectAdminAccountList(AdminAccountSearchVo searchVo) throws Exception {
		return adminAccountApiMapper.selectAdminAccountList(searchVo);
	}

	public int selectAdminAccountListTotalCnt(AdminAccountSearchVo searchVo) throws Exception {
		return adminAccountApiMapper.selectAdminAccountListTotalCount(searchVo);
	}

	public AdminAccountVo selectAdminAccount(AdminAccountVo adminAccountVo) throws Exception {
		return adminAccountApiMapper.selectAdminAccount(adminAccountVo);
	}

	@Transactional
	public int createAdminAccount(AdminAccountCudVo adminAccountVo) throws Exception {
		return adminAccountApiMapper.insertAdminAccount(adminAccountVo);
	}

	@Transactional
	public int modifyAdminAccount(AdminAccountCudVo adminAccountVo) throws Exception {
		return adminAccountApiMapper.updateAdminAccount(adminAccountVo);
	}

	@Transactional
	public int removeAdminAccount(AdminAccountCudVo adminAccountVo) throws Exception {
		return adminAccountApiMapper.deleteAdminAccount(adminAccountVo);
	}

	@Transactional
	public int removeAdminAccountByUid(AdminAccountCudVo adminAccountCudVo) throws Exception {
		return adminAccountApiMapper.deleteAdminAccountByUid(adminAccountCudVo);
	}

	@Deprecated
	public boolean isExistUserId(String user_id) throws Exception {
		Integer count = adminAccountApiMapper.selectCountByUserId(user_id);
		logger.info("userid exist count [{}]", count);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	// [추가] 로그인 테이블 분
	public boolean isExistUserIdBySystemType(AdminAccountCudVo adminAccountCudVo) throws Exception {
		Integer count = adminAccountApiMapper.selectCountByUserIdBySystemType(adminAccountCudVo);
		logger.info("userid exist count [{}]", count);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isExistEmail(AdminAccountCudVo adminAccountCudVo) throws Exception {
		Integer count = adminAccountApiMapper.selectCountByEmail(adminAccountCudVo);
		logger.info("email exist count [{}]", count);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

}

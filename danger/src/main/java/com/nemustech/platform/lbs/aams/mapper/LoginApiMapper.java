package com.nemustech.platform.lbs.aams.mapper;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.common.vo.LoginHistoryVo;
import com.nemustech.platform.lbs.common.vo.LoginVo;
import com.nemustech.platform.lbs.common.vo.PortalLoginVo;
import com.nemustech.platform.lbs.common.vo.ResetLoginVo;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;

@Repository
public interface LoginApiMapper {

	public ResponseLoginVo selectLoginInfo(LoginVo loginVo) throws Exception;

	public int updateLoginDate(ResponseLoginVo responseLoginVo) throws Exception;

	public int updateResetPassword(ResetLoginVo loginVo) throws Exception;

	public ResponseLoginVo selectSearchId(ResetLoginVo resetLoginVo) throws Exception;

	public int updatePasswordFailCount(LoginVo loginVo) throws Exception;

	public int updatePasswordFailCountInit(LoginVo loginVo) throws Exception;

	public int insertVehicleLoginHistory(LoginHistoryVo loginHistoryVo) throws Exception;

	public int insertDangerLoginHistory(LoginHistoryVo loginHistoryVo) throws Exception;

	public ResponseLoginVo selectIsExistPortalId(PortalLoginVo portalLoginVo) throws Exception;
}

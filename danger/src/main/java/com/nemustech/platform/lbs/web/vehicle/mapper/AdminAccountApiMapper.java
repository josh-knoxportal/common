package com.nemustech.platform.lbs.web.vehicle.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountCudVo;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountSearchVo;
import com.nemustech.platform.lbs.web.vehicle.vo.AdminAccountVo;

@Repository(value = "adminAccountApiMapper")
public interface AdminAccountApiMapper {

	public List<AdminAccountVo> selectAdminAccountList(AdminAccountSearchVo searchVo) throws Exception;

	public int selectAdminAccountListTotalCount(AdminAccountSearchVo searchVo) throws Exception;

	public AdminAccountVo selectAdminAccount(AdminAccountVo adminAccountVo) throws Exception;

	public int insertAdminAccount(AdminAccountCudVo adminAccountVo) throws Exception;

	public int updateAdminAccount(AdminAccountCudVo adminAccountVo) throws Exception;

	public int deleteAdminAccount(AdminAccountCudVo adminAccountVo) throws Exception;

	public int deleteAdminAccountByUid(AdminAccountCudVo adminAccountCudVo) throws Exception;

	public Integer selectCountByUserId(String user_id) throws Exception;

	public Integer selectCountByEmail(AdminAccountCudVo adminAccountCudVo) throws Exception;

	public Integer selectCountByUserIdBySystemType(AdminAccountCudVo adminAccountCudVo) throws Exception;

}

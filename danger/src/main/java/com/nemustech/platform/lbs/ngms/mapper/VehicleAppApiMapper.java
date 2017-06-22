package com.nemustech.platform.lbs.ngms.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.common.vo.DateVo;
import com.nemustech.platform.lbs.ngms.vo.AppResourceFileInfoVo;
import com.nemustech.platform.lbs.ngms.vo.MetaZoneVo;
import com.nemustech.platform.lbs.wwms.vo.AppPwdEventVo;

@Repository
public interface VehicleAppApiMapper {

	// 160804 [개인폰] [신규] [안전운행13] FCM_token 갱신
	int updateVehicleDevicePushId(AppPwdEventVo appPwdEventVo) throws Exception;

	// 160804 [개인폰] [신규] [안전운행 16] Meta Zone 정보 조회
	List<MetaZoneVo> selectMetaZoneList() throws Exception;

	// 160804 [개인폰] [신규] [안전운행10] 도로 지도 리소스 정보 조회
	List<AppResourceFileInfoVo> selectAppResourceList() throws Exception;

	// 1608 [신규] [공통] 리소스 정보의 최신 갱신 여부
	DateVo getIsLastModifiedResource() throws Exception;

	// 1608 [신규] [공통] MetaZone 정보의 최신 갱신 여부
	DateVo getIsLastModifiedMetaZone() throws Exception;

}

package com.nemustech.platform.lbs.wwms.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nemustech.platform.lbs.wwms.vo.FactoryCoordVo;
import com.nemustech.platform.lbs.wwms.vo.FactoryVo;
import com.nemustech.platform.lbs.wwms.vo.MapFactoryRestrictUpdateRequestVo;
import com.nemustech.platform.lbs.wwms.vo.MapWorkStatusOnFactoriesVo;
import com.nemustech.platform.lbs.wwms.vo.MapWorkStatusOnWorkTypeVo;
import com.nemustech.platform.lbs.wwms.vo.MapWorkStatusOnZonesVo;
import com.nemustech.platform.lbs.wwms.vo.MapWorkStatusRequestVo;
import com.nemustech.platform.lbs.wwms.vo.MapZoneRestrictUpdateRequestVo;
import com.nemustech.platform.lbs.wwms.vo.ZoneCoordVo;
import com.nemustech.platform.lbs.wwms.vo.ZoneVo;


@Repository(value = "MapDangerApiMapper")
public interface MapDangerApiMapper {
	public List<FactoryVo> selectFactoryList() throws Exception;	
	public List<FactoryCoordVo> selectFactoryCoordList() throws Exception;
	public List<ZoneVo> selectZoneList() throws Exception;	
	public List<ZoneCoordVo> selectZoneCoordList() throws Exception;
	
	/* */
	public void updateAllowedFactoryAll() throws Exception;
	public void updateFactoryRestrict(MapFactoryRestrictUpdateRequestVo requestVo) throws Exception;
	public void updateAllowedZoneAll() throws Exception;
	public void updateZoneRestrict(MapZoneRestrictUpdateRequestVo requestVo) throws Exception;
	
	/* [2016/08/19] 작업 기준  */
	public List<MapWorkStatusOnFactoriesVo> selectWorkStatusOnFactories(MapWorkStatusRequestVo requestVo) throws Exception;
	public List<MapWorkStatusOnZonesVo> 	selectWorkStatusOnZones(MapWorkStatusRequestVo requestVo) throws Exception;
	public List<MapWorkStatusOnWorkTypeVo> 	selectWorkStatusOnWorkType(MapWorkStatusRequestVo requestVo) throws Exception;
	
	/* [2016/08/19] 작업자 기준 */
	public List<MapWorkStatusOnFactoriesVo> selectWorkerStatusOnFactories(MapWorkStatusRequestVo requestVo) throws Exception;
	public List<MapWorkStatusOnZonesVo> 	selectWorkerStatusOnZones(MapWorkStatusRequestVo requestVo) throws Exception;
	public List<MapWorkStatusOnWorkTypeVo> 	selectWorkerStatusOnWorkType(MapWorkStatusRequestVo requestVo) throws Exception;
}

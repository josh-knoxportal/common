package com.nemustech.platform.lbs.wwms.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nemustech.platform.lbs.wwms.mapper.MapDangerApiMapper;
import com.nemustech.platform.lbs.wwms.model.FactoryCoordList;
import com.nemustech.platform.lbs.wwms.model.MapWorkStatusList;
import com.nemustech.platform.lbs.wwms.model.ZoneCoordList;
import com.nemustech.platform.lbs.wwms.vo.MapFactoryRestrictUpdateRequestVo;
import com.nemustech.platform.lbs.wwms.vo.MapWorkStatusOnFactoriesVo;
import com.nemustech.platform.lbs.wwms.vo.MapWorkStatusRequestVo;
import com.nemustech.platform.lbs.wwms.vo.MapZoneRestrictUpdateRequestVo;

@Service(value = "MapDangerApiService")
public class MapDangerApiService {

	@Autowired
	private MapDangerApiMapper mapDangerApiMapper;

	/* */
	public FactoryCoordList factoryCoordList() throws Exception {
		FactoryCoordList coord = new FactoryCoordList();
		coord.setFactoryList(mapDangerApiMapper.selectFactoryList());
		coord.setFactoryCoordList(mapDangerApiMapper.selectFactoryCoordList());

		return coord;
	}

	/* */
	public ZoneCoordList zoneCoordList() throws Exception {
		ZoneCoordList coord = new ZoneCoordList();
		coord.setZoneList(mapDangerApiMapper.selectZoneList());
		coord.setZoneCoordList(mapDangerApiMapper.selectZoneCoordList());

		return coord;
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void updateFactoryRestrict(MapFactoryRestrictUpdateRequestVo requestVo) throws Exception {
		mapDangerApiMapper.updateAllowedFactoryAll();
		if (!requestVo.isEmpty()) {
			mapDangerApiMapper.updateFactoryRestrict(requestVo);
		}
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void updateZoneRestrict(MapZoneRestrictUpdateRequestVo requestVo) throws Exception {
		mapDangerApiMapper.updateAllowedZoneAll();
		if (!requestVo.isEmpty()) {
			mapDangerApiMapper.updateZoneRestrict(requestVo);
		}
	}

	/*
	 * [2016/08/19] map_work_status.do와 map_worker_status.do로 구분
	 * 
	 * 1 : map_work_status.do는 작업기준(1일기준) 위험지역 현황 2 : map_worker_status.do는
	 * 현재작업중인(is_completed기주) 위험지역 현황
	 * 
	 * 단, MapWorkStatusList, MapWorkStatusRequestVo는 동일하게 이용하고 확장한다.
	 */
	/*
	 * [2016/08/19] modified [주의] work <--> worker
	 */
	public MapWorkStatusList workStatusList(MapWorkStatusRequestVo requestVo) throws Exception {
		MapWorkStatusList statusList = new MapWorkStatusList();

		/* WORK */
		if (requestVo.isOnFactory()) {
			statusList.setFactoryStatusList(mapDangerApiMapper.selectWorkStatusOnFactories(requestVo));
		} else {
			statusList.setFactoryStatusList(new ArrayList<MapWorkStatusOnFactoriesVo>());
		}

		/* */
		statusList.setZoneStatusList(mapDangerApiMapper.selectWorkStatusOnZones(requestVo));
		statusList.setWorktypeStatusList(mapDangerApiMapper.selectWorkStatusOnWorkType(requestVo));

		/* */
		statusList.setShow_type(requestVo.showType());

		return statusList;
	}

	/*
	 * [2016/08/19] added [주의] work <--> worker
	 */
	public MapWorkStatusList workerStatusList(MapWorkStatusRequestVo requestVo) throws Exception {
		MapWorkStatusList statusList = new MapWorkStatusList();

		/* WORKER */
		if (requestVo.isOnFactory()) {
			statusList.setFactoryStatusList(mapDangerApiMapper.selectWorkerStatusOnFactories(requestVo));
		} else {
			statusList.setFactoryStatusList(new ArrayList<MapWorkStatusOnFactoriesVo>());
		}

		/* */
		statusList.setZoneStatusList(mapDangerApiMapper.selectWorkerStatusOnZones(requestVo));
		statusList.setWorktypeStatusList(mapDangerApiMapper.selectWorkerStatusOnWorkType(requestVo));

		/* */
		statusList.setShow_type(requestVo.showType());

		return statusList;
	}

}

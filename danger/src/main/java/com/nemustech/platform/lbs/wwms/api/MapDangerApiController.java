package com.nemustech.platform.lbs.wwms.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.wwms.model.FactoryCoordList;
import com.nemustech.platform.lbs.wwms.model.FactoryCud;
import com.nemustech.platform.lbs.wwms.model.MapWorkStatusList;
import com.nemustech.platform.lbs.wwms.model.ZoneCoordList;
import com.nemustech.platform.lbs.wwms.model.ZoneCud;
import com.nemustech.platform.lbs.wwms.service.MapDangerApiService;
import com.nemustech.platform.lbs.wwms.vo.MapFactoryRestrictUpdateRequestVo;
import com.nemustech.platform.lbs.wwms.vo.MapWorkStatusRequestVo;
import com.nemustech.platform.lbs.wwms.vo.MapZoneRestrictUpdateRequestVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("MapDangerApiController")
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_V1_ROOT, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class MapDangerApiController extends DefaultController {

	@Autowired
	private MapDangerApiService mapDangerApiService;

	@ApiOperation(value = "", notes = "단위공장 속성 및 좌표계를 가져온다.", response = FactoryCoordList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/factory_coord.do", method = RequestMethod.GET)
	public ResponseEntity<FactoryCoordList> v1WwmsFactoryCoordListGet() throws NotFoundException {
		FactoryCoordList coordList = null;
		try {
			coordList = mapDangerApiService.factoryCoordList();
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<FactoryCoordList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<FactoryCoordList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (coordList == null) {
			return new ResponseEntity<FactoryCoordList>(HttpStatus.NO_CONTENT);
		} else {
			coordList.setMsg("SUCCESS");
			return new ResponseEntity<FactoryCoordList>(coordList, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "비콘 존 속성 및 좌표계를 가져온다.", response = ZoneCoordList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/zone_coord.do", method = RequestMethod.GET)
	public ResponseEntity<ZoneCoordList> v1WwmsZoneCoordListGet() throws NotFoundException {
		ZoneCoordList coordList = null;
		try {
			coordList = mapDangerApiService.zoneCoordList();
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ZoneCoordList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ZoneCoordList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (coordList == null) {
			return new ResponseEntity<ZoneCoordList>(HttpStatus.NO_CONTENT);
		} else {
			coordList.setMsg("SUCCESS");
			return new ResponseEntity<ZoneCoordList>(coordList, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "단위공장 위험지역을 설정한다.", response = FactoryCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/factory_restrict.do", method = RequestMethod.POST)
	public ResponseEntity<FactoryCud> v1WwmsFactoryRestrictUpdatePost(
			@ApiParam(value = "위험지역 단위공장 목록", required = true) @RequestBody MapFactoryRestrictUpdateRequestVo requestVo)
			throws NotFoundException {
		try {
			mapDangerApiService.updateFactoryRestrict(requestVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<FactoryCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<FactoryCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		FactoryCud vo = new FactoryCud();
		vo.setMsg("SUCCESS");
		return new ResponseEntity<FactoryCud>(vo, HttpStatus.OK);
	}

	@ApiOperation(value = "", notes = "비콘 존 위험지역을 설정한다.", response = ZoneCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/zone_restrict.do", method = RequestMethod.POST)
	public ResponseEntity<ZoneCud> v1WwmsZoneRestrictUpdatePost(
			@ApiParam(value = "위험지역 비콘 존 목록", required = true) @RequestBody MapZoneRestrictUpdateRequestVo requestVo)
			throws NotFoundException {
		try {
			mapDangerApiService.updateZoneRestrict(requestVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<ZoneCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<ZoneCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		ZoneCud vo = new ZoneCud();
		vo.setMsg("SUCCESS");
		return new ResponseEntity<ZoneCud>(vo, HttpStatus.OK);
	}

	/*
	 * [2016/08/19] map_work_status.do와 map_worker_status.do로 구분
	 * 
	 * 1 : map_work_status.do는 작업기준(1일기준) 위험지역 현황 2 : map_worker_status.do는
	 * 현재작업중인(is_completed기주) 위험지역 현황
	 * 
	 * 단, MapWorkStatusList, MapWorkStatusRequestVo는 동일하게 이용하고 확장한다.
	 */
	@ApiOperation(value = "", notes = "작업기준(1일기준) 위험지역 현황", response = MapWorkStatusList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/map_work_status.do", method = RequestMethod.GET)
	public ResponseEntity<MapWorkStatusList> v1WwmsMapWorkStatusGet(
			@ApiParam(value = "all:모두, factory:단위공장, zone:존", required = false) @RequestParam(value = "type", required = false, defaultValue = "all") String type,
			@ApiParam(value = "단위공장 또는 비콘존식별자", required = false) @RequestParam(value = "uid", required = false, defaultValue = "") String uid,
			@ApiParam(value = "작업유형", required = false) @RequestParam(value = "work_type", required = false, defaultValue = "") String work_type)
			throws NotFoundException {
		MapWorkStatusList vo = null;
		try {
			MapWorkStatusRequestVo requestVo = new MapWorkStatusRequestVo();
			requestVo.setType(type);
			requestVo.setUid(uid);
			requestVo.setWork_type(work_type);

			if (!requestVo.isValid()) {
				return new ResponseEntity<MapWorkStatusList>(HttpStatus.BAD_REQUEST);
			}

			/* [2016/08/18] 차이가 발생하는 위치 */
			vo = mapDangerApiService.workStatusList(requestVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<MapWorkStatusList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<MapWorkStatusList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (vo == null) {
			return new ResponseEntity<MapWorkStatusList>(HttpStatus.NO_CONTENT);
		} else {
			vo.setMsg("SUCCESS");
			return new ResponseEntity<MapWorkStatusList>(vo, HttpStatus.OK);
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
	@ApiOperation(value = "", notes = "작업자기준(is_completed) 위험지역 현황", response = MapWorkStatusList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/map_worker_status.do", method = RequestMethod.GET)
	public ResponseEntity<MapWorkStatusList> v1WwmsMapWorkerStatusGet(
			@ApiParam(value = "all:모두,factory:단위공장, zone:존", required = false) @RequestParam(value = "type", required = false, defaultValue = "all") String type,
			@ApiParam(value = "단위공장 또는 비콘존식별자", required = false) @RequestParam(value = "uid", required = false, defaultValue = "") String uid,
			@ApiParam(value = "작업유형", required = false) @RequestParam(value = "work_type", required = false, defaultValue = "") String work_type)
			throws NotFoundException {
		MapWorkStatusList vo = null;
		try {
			MapWorkStatusRequestVo requestVo = new MapWorkStatusRequestVo();
			requestVo.setType(type);
			requestVo.setUid(uid);
			requestVo.setWork_type(work_type);

			if (!requestVo.isValid()) {
				return new ResponseEntity<MapWorkStatusList>(HttpStatus.BAD_REQUEST);
			}

			/* [2016/08/18] 차이가 발생하는 위치 */
			vo = mapDangerApiService.workerStatusList(requestVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<MapWorkStatusList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<MapWorkStatusList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (vo == null) {
			return new ResponseEntity<MapWorkStatusList>(HttpStatus.NO_CONTENT);
		} else {
			vo.setMsg("SUCCESS");
			return new ResponseEntity<MapWorkStatusList>(vo, HttpStatus.OK);
		}
	}
}

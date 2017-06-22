package com.nemustech.platform.lbs.wwms.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.wwms.model.CodeFactoryZoneList;
import com.nemustech.platform.lbs.wwms.model.CodeWorkNoList;
import com.nemustech.platform.lbs.wwms.model.CodeWorkTypeList;
import com.nemustech.platform.lbs.wwms.model.ParterNameList;
import com.nemustech.platform.lbs.wwms.model.WorkerContactList;
import com.nemustech.platform.lbs.wwms.service.CommonCodeApiService;
import com.nemustech.platform.lbs.wwms.vo.CodeFactoryZoneVo;
import com.nemustech.platform.lbs.wwms.vo.CodeWorkNoVo;
import com.nemustech.platform.lbs.wwms.vo.CodeWorkTypeVo;
import com.nemustech.platform.lbs.wwms.vo.ParterNameVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerContactRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerContactVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("CommonCodeApiController")
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_V1_ROOT, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class CommonCodeApiController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(CommonCodeApiController.class);

	@Autowired
	private CommonCodeApiService commonCodeApiService;

	@ApiOperation(value = "", notes = "작업코드 리스트를 가져온다.", response = CodeWorkTypeList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/code_worktype.do", method = RequestMethod.GET)
	public ResponseEntity<CodeWorkTypeList> v1WwmsCodeWorkTypeListGet(
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since)
			throws NotFoundException {
		List<CodeWorkTypeVo> dataList = null;
		CodeWorkTypeList responseCodeWorkTypeList = new CodeWorkTypeList();

		try {
			// check modified time
			if (request_type.equals("1") == true && if_modified_since.length() > 0) {
				if (commonCodeApiService.getIsLastModifiedCodeWorkTypeList(if_modified_since) == false) {
					responseCodeWorkTypeList.setMsg("Not Modified");
					return new ResponseEntity<CodeWorkTypeList>(responseCodeWorkTypeList, HttpStatus.NOT_MODIFIED);
				}
			}
			dataList = commonCodeApiService.workTypeList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CodeWorkTypeList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<CodeWorkTypeList>(HttpStatus.NO_CONTENT);
		} else {
			responseCodeWorkTypeList.setMsg("SUCCESS");
			responseCodeWorkTypeList.setCodeWorkTypeList(dataList);
			responseCodeWorkTypeList.setTotalCnt(dataList.size());
			return new ResponseEntity<CodeWorkTypeList>(responseCodeWorkTypeList, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "작업번호 리스트를 가져온다.", response = CodeWorkTypeList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/code_workno.do", method = RequestMethod.GET)
	public ResponseEntity<CodeWorkNoList> v1WwmsCodeWorkNoListGet(
			@ApiParam(value = "device_no", required = true) @RequestHeader(value = "device_no", required = true) String device_no,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "If-Modified-Since", required = false) @RequestHeader(value = "If-Modified-Since", required = false, defaultValue = "") String if_modified_since)
			throws NotFoundException {
		List<CodeWorkNoVo> dataList = null;
		// CodeWorkNoList responseCodeWorkNoList = new CodeWorkNoList();

		try {

			// [If-Modified-Since] 적용시 주석 풀고 테스트
			// check modified time
			// if (request_type.equals("1") == true &&
			// if_modified_since.length() > 0) {
			// if
			// (commonCodeApiService.getIsLastModifiedCodeWorkNoList(if_modified_since)
			// == false) {
			// responseCodeWorkNoList.setMsg("Not Modified");
			// return new ResponseEntity<CodeWorkNoList>(responseCodeWorkNoList,
			// HttpStatus.NOT_MODIFIED);
			// }
			// }

			// [161019] LPMS 매핑 필터링 관련 정리되면 주석
			// dataList = commonCodeApiService.workNoList();

			// [161019] LPMS 매핑 필터링 관련 정리되면 주석 풀고 테스트
			if (StringUtils.isEmpty(device_no)) {
				dataList = null;
			} else {
				dataList = commonCodeApiService.workNoListByDeviceNo(device_no);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CodeWorkNoList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<CodeWorkNoList>(HttpStatus.NO_CONTENT);
		} else {
			CodeWorkNoList vo = new CodeWorkNoList();
			vo.setMsg("SUCCESS");
			vo.setCodeWorkNoList(dataList);
			return new ResponseEntity<CodeWorkNoList>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "부서/업체명 리스트를 가져온다.", response = ParterNameList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/get_partername.do", method = RequestMethod.GET)
	public ResponseEntity<ParterNameList> v1WwmsParterNameListGet() throws NotFoundException {
		List<ParterNameVo> dataList = null;
		try {
			dataList = commonCodeApiService.parterNameList();
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<ParterNameList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null || dataList.size() == 0) {
			return new ResponseEntity<ParterNameList>(HttpStatus.NO_CONTENT);
		} else {
			ParterNameList vo = new ParterNameList();
			vo.setMsg("SUCCESS");
			logger.info(dataList.get(0).getParter_name());
			vo.setParternameList(dataList);
			return new ResponseEntity<ParterNameList>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "단위공장 및 존의 목록을 가져온다.", response = CodeFactoryZoneList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/code_factoryzone/{tag}.do", method = RequestMethod.GET)
	public ResponseEntity<CodeFactoryZoneList> v1NgmsCodeFactoryZoneListGet(
			@ApiParam(value = "조회조건(all:모두,factory:단위공장만,zone:존만", required = true) @PathVariable(value = "tag") String tag)
			throws NotFoundException {
		List<CodeFactoryZoneVo> dataList = null;
		try {
			dataList = commonCodeApiService.factoryZoneList(tag);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<CodeFactoryZoneList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<CodeFactoryZoneList>(HttpStatus.NO_CONTENT);
		} else {
			CodeFactoryZoneList vo = new CodeFactoryZoneList();
			vo.setMsg("SUCCESS");
			vo.setCodeFactoryZoneList(dataList);
			vo.setTotalCnt(dataList.size());
			return new ResponseEntity<CodeFactoryZoneList>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "단위공장 또는 비콘존에 있는 작업자의 연락처 목록을 구한다.", response = WorkerContactList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/contacts_on/{type}/{uid}.do", method = RequestMethod.GET)
	public ResponseEntity<WorkerContactList> v1NgmsWorkerContactListGet(
			@ApiParam(value = "factory:단위공장,zone:존", required = true) @PathVariable(value = "type") String type,
			@ApiParam(value = "식별자", required = true) @PathVariable(value = "uid") String uid)
			throws NotFoundException {
		List<WorkerContactVo> dataList = null;
		try {
			WorkerContactRequestVo requestVo = new WorkerContactRequestVo();
			requestVo.setType(type);
			requestVo.setUid(uid);

			if (!requestVo.isValid()) {
				return new ResponseEntity<WorkerContactList>(HttpStatus.BAD_REQUEST);
			}

			dataList = commonCodeApiService.workerContactListOnFactoryZone(requestVo);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<WorkerContactList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<WorkerContactList>(HttpStatus.NO_CONTENT);
		} else {
			WorkerContactList vo = new WorkerContactList();
			vo.setMsg("SUCCESS");
			vo.setWorkerContactList(dataList);
			return new ResponseEntity<WorkerContactList>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "작업번호 리스트를 가져온다.", response = CodeWorkTypeList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/code_workno_before.do", method = RequestMethod.GET)
	public ResponseEntity<CodeWorkNoList> v1WwmsCodeWorkNoBeforeListGet() throws NotFoundException {
		List<CodeWorkNoVo> dataList = null;
		try {
			dataList = commonCodeApiService.selectBeforeStartWorkNoList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CodeWorkNoList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<CodeWorkNoList>(HttpStatus.NO_CONTENT);
		} else {
			CodeWorkNoList vo = new CodeWorkNoList();
			vo.setMsg("SUCCESS");
			vo.setCodeWorkNoList(dataList);
			return new ResponseEntity<CodeWorkNoList>(vo, HttpStatus.OK);
		}
	}
}

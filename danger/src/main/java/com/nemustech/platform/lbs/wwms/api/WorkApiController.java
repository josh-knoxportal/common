package com.nemustech.platform.lbs.wwms.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.wwms.model.WorkCud;
import com.nemustech.platform.lbs.wwms.model.WorkDetail;
import com.nemustech.platform.lbs.wwms.model.WorkIssueList;
import com.nemustech.platform.lbs.wwms.model.WorkMntList;
import com.nemustech.platform.lbs.wwms.model.WorkStatusList;
import com.nemustech.platform.lbs.wwms.model.WorkerMntList;
import com.nemustech.platform.lbs.wwms.service.WorkApiService;
import com.nemustech.platform.lbs.wwms.vo.WorkIssueVo;
import com.nemustech.platform.lbs.wwms.vo.WorkMntRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkMntVo;
import com.nemustech.platform.lbs.wwms.vo.WorkStatusRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkStatusVo;
import com.nemustech.platform.lbs.wwms.vo.WorkVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerMntRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerMntVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_V1_ROOT, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class WorkApiController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(WorkApiController.class);

	@Autowired
	private WorkApiService workApiService;

	// 2-1 작업자별 현황 - 작업자별 현황
	@ApiOperation(value = "", notes = "작업 정보 리스트를 가져온다.", response = WorkStatusList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업 정보 리스트를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/workStatus.do", method = RequestMethod.GET)
	public ResponseEntity<WorkStatusList> v1wwmsWorkGet(
			@ApiParam(value = "검색시작일", required = false) @RequestParam(value = "from", required = false, defaultValue = "0") String from,
			@ApiParam(value = "검색종료일", required = false) @RequestParam(value = "to", required = false, defaultValue = "0") String to,
			@ApiParam(value = "검색 작업자 명", required = false) @RequestParam(value = "worker_name", required = false, defaultValue = "") String worker_name,
			@ApiParam(value = "검색 폰번호", required = false) @RequestParam(value = "device_no", required = false, defaultValue = "") String device_no,
			@ApiParam(value = "limit_offset (default_value is 0)", allowableValues = "{}") @RequestParam(value = "limit_offset", required = false, defaultValue = "0") int limit_offset,
			@ApiParam(value = "limit_count (default_value is 10)", allowableValues = "{}") @RequestParam(value = "limit_count", required = false, defaultValue = "-1") int limit_count,
			@ApiParam(value = "order_type") @RequestParam(value = "order_type", required = false, defaultValue = "name") String order_type,
			@ApiParam(value = "order_desc_asc") @RequestParam(value = "order_desc_asc", required = false, defaultValue = "asc") String order_desc_asc)
			throws NotFoundException {

		List<WorkStatusVo> dataList = null;
		int totalcnt = 0;
		try {
			WorkStatusRequestVo requestVo = new WorkStatusRequestVo();
			if (from.equals("0") == false && to.equals("0") == false) {
				requestVo.setFrom(from);
				requestVo.setTo(to);
			}

			requestVo.setLimit_count(limit_count);
			requestVo.setLimit_offset(limit_offset);
			requestVo.setOrder_type(order_type);
			requestVo.setOrder_desc_asc(order_desc_asc);
			requestVo.setDevice_no(device_no);
			requestVo.setWorker_name(worker_name);

			dataList = workApiService.workStatusList(requestVo);
			totalcnt = workApiService.selectWorkStatusTotalCount(requestVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkStatusList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkStatusList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<WorkStatusList>(HttpStatus.NO_CONTENT);
		} else {

			for (int i = 0; i < dataList.size(); i++) {
				if (dataList.get(i).getWork_type_name() != null) {
					String[] array = dataList.get(i).getWork_type_name().split(",");
					String num2 = "";
					for (int j = 0; j < array.length; j++) {
						num2 = getWorkTypeImagePath(array, num2, j);
						dataList.get(i).setWork_type_img(num2);
					}
				}
			}

			WorkStatusList vo = new WorkStatusList();
			vo.setMsg("SUCCESS");
			vo.setWorkStatusList(dataList);
			vo.setTotalcnt(totalcnt);
			return new ResponseEntity<WorkStatusList>(vo, HttpStatus.OK);
		}

	}

	@ApiOperation(value = "", notes = "할당된 작업이 종료안된 작업자 목록 리스트를 가져온다.", response = WorkStatusList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업자 목록 리스트를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/assignWork.do", method = RequestMethod.GET)
	public ResponseEntity<WorkStatusList> v1wwmsAssignWorkerGet(
			@ApiParam(value = "검색 작업자 명", required = false) @RequestParam(value = "worker_name", required = false, defaultValue = "") String worker_name,
			@ApiParam(value = "검색 폰번호", required = false) @RequestParam(value = "device_no", required = false, defaultValue = "") String device_no,
			@ApiParam(value = "limit_offset (default_value is 0)", allowableValues = "{}") @RequestParam(value = "limit_offset", required = false, defaultValue = "0") int limit_offset,
			@ApiParam(value = "limit_count (default_value is 10)", allowableValues = "{}") @RequestParam(value = "limit_count", required = false, defaultValue = "-1") int limit_count,
			@ApiParam(value = "order_type") @RequestParam(value = "order_type", required = false, defaultValue = "name") String order_type,
			@ApiParam(value = "order_desc_asc") @RequestParam(value = "order_desc_asc", required = false, defaultValue = "asc") String order_desc_asc)
			throws NotFoundException {

		WorkStatusList response = null;

		try {
			WorkStatusRequestVo requestVo = new WorkStatusRequestVo();
			requestVo.setLimit_count(limit_count);
			requestVo.setLimit_offset(limit_offset);
			requestVo.setOrder_type(order_type);
			requestVo.setOrder_desc_asc(order_desc_asc);
			requestVo.setDevice_no(device_no);
			requestVo.setWorker_name(worker_name);

			response = workApiService.getWorkerList(requestVo);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkStatusList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkStatusList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (response == null) {
			return new ResponseEntity<WorkStatusList>(HttpStatus.NO_CONTENT);
		} else {
			response.setMsg("SUCCESS");
			return new ResponseEntity<WorkStatusList>(response, HttpStatus.OK);
		}

	}

	// 2-2 작업자별 현황 - 출입감지 현황
	@ApiOperation(value = "", notes = "출입감지 현황 리스트를 가져온다.", response = WorkIssueList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "이상 여부 리스트를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/workIssue.do", method = RequestMethod.GET)
	public ResponseEntity<WorkIssueList> v1wwmsWorkIssueGet(
			@ApiParam(value = "검색시작일", required = false) @RequestParam(value = "from", required = false, defaultValue = "0") String from,
			@ApiParam(value = "검색종료일", required = false) @RequestParam(value = "to", required = false, defaultValue = "0") String to,
			@ApiParam(value = "limit_offset (default_value is 0)", allowableValues = "{}") @RequestParam(value = "limit_offset", required = false, defaultValue = "0") int limit_offset,
			@ApiParam(value = "limit_count (default_value is 10)", allowableValues = "{}") @RequestParam(value = "limit_count", required = false, defaultValue = "-1") int limit_count,
			@ApiParam(value = "order_type") @RequestParam(value = "order_type", required = false, defaultValue = "name") String order_type,
			@ApiParam(value = "order_desc_asc") @RequestParam(value = "order_desc_asc", required = false, defaultValue = "asc") String order_desc_asc)
			throws NotFoundException {

		List<WorkIssueVo> dataList = null;
		int totalcnt = 0;
		try {
			WorkStatusRequestVo requestVo = new WorkStatusRequestVo();
			requestVo.setFrom(from);
			requestVo.setTo(to);
			requestVo.setLimit_count(limit_count);
			requestVo.setLimit_offset(limit_offset);
			requestVo.setOrder_type(order_type);
			requestVo.setOrder_desc_asc(order_desc_asc);

			dataList = workApiService.workIssueList(requestVo);
			totalcnt = workApiService.selectworkIssueListTotalCount(requestVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkIssueList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkIssueList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<WorkIssueList>(HttpStatus.NO_CONTENT);
		} else {

			for (int i = 0; i < dataList.size(); i++) {
				if (dataList.get(i).getWork_type_name() != null) {
					String[] array = dataList.get(i).getWork_type_name().split(",");
					String num2 = "";
					for (int j = 0; j < array.length; j++) {
						num2 = getWorkTypeImagePath(array, num2, j);
						dataList.get(i).setWork_type_img(num2);
					}
				}
			}

			WorkIssueList vo = new WorkIssueList();
			vo.setMsg("SUCCESS");
			vo.setWorkIssueList(dataList);
			vo.setTotalcnt(totalcnt);
			return new ResponseEntity<WorkIssueList>(vo, HttpStatus.OK);
		}

	}

	@ApiOperation(value = "", notes = "작업자 정보 리스트를 가져온다.", response = WorkerMntList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업자 정보 리스트를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/worker.do", method = RequestMethod.GET)
	public ResponseEntity<WorkerMntList> v1wwmsWorkerGet(
			@ApiParam(value = "작업자 이름 검색", required = false) @RequestParam(value = "worker_name", required = false, defaultValue = "") String worker_name,
			@ApiParam(value = "폰번호 검색", required = false) @RequestParam(value = "device_no", required = false, defaultValue = "") String device_no,
			@ApiParam(value = "검색 공장명", required = false) @RequestParam(value = "parter_name", required = false, defaultValue = "") String parter_name,
			@ApiParam(value = "검색 작업명", required = false) @RequestParam(value = "name", required = false, defaultValue = "") String name,
			@ApiParam(value = "검색 작업번호", required = false) @RequestParam(value = "work_no", required = false, defaultValue = "") String work_no,
			@ApiParam(value = "work_uid", required = false) @RequestParam(value = "work_uid", required = false, defaultValue = "") String work_uid,
			@ApiParam(value = "limit_offset (default_value is 0)", allowableValues = "{}") @RequestParam(value = "limit_offset", required = false, defaultValue = "0") int limit_offset,
			@ApiParam(value = "limit_count (default_value is 10)", allowableValues = "{}") @RequestParam(value = "limit_count", required = false, defaultValue = "-1") int limit_count,
			@ApiParam(value = "order_type") @RequestParam(value = "order_type", required = false, defaultValue = "name") String order_type,
			@ApiParam(value = "order_desc_asc") @RequestParam(value = "order_desc_asc", required = false, defaultValue = "asc") String order_desc_asc)
			throws NotFoundException {

		List<WorkerMntVo> dataList = null;
		int totalcnt = 0;
		try {
			WorkerMntRequestVo requestVo = new WorkerMntRequestVo();
			requestVo.setWorker_name(worker_name);
			requestVo.setDevice_no(device_no);
			requestVo.setParter_name(parter_name);
			requestVo.setName(name);
			requestVo.setWork_no(work_no);
			requestVo.setLimit_count(limit_count);
			requestVo.setLimit_offset(limit_offset);
			requestVo.setOrder_type(order_type);
			requestVo.setWork_uid(work_uid);
			requestVo.setOrder_desc_asc(order_desc_asc);

			dataList = workApiService.workerMntList(requestVo);
			totalcnt = workApiService.selectWorkerMntTotalCount(requestVo);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkerMntList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkerMntList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<WorkerMntList>(HttpStatus.NO_CONTENT);
		} else {

			for (int i = 0; i < dataList.size(); i++) {
				if (dataList.get(i).getWork_type_name() != null) {
					String[] array = dataList.get(i).getWork_type_name().split(",");
					String num2 = "";
					for (int j = 0; j < array.length; j++) {
						num2 = getWorkTypeImagePath(array, num2, j);
						dataList.get(i).setWork_type_img(num2);
					}
				}
			}

			WorkerMntList vo = new WorkerMntList();
			vo.setMsg("SUCCESS");
			vo.setWorkerMntList(dataList);
			vo.setTotalcnt(totalcnt);
			return new ResponseEntity<WorkerMntList>(vo, HttpStatus.OK);
		}

	}

	@ApiOperation(value = "", notes = "작업 정보 리스트를 가져온다.", response = WorkMntList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업 정보 리스트를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/work.do", method = RequestMethod.GET)
	public ResponseEntity<WorkMntList> v1wwmsWorkGet(
			@ApiParam(value = "검색시작일", required = false) @RequestParam(value = "from", required = false, defaultValue = "0") String from,
			@ApiParam(value = "검색종료일", required = false) @RequestParam(value = "to", required = false, defaultValue = "0") String to,
			@ApiParam(value = "검색 공장명", required = false) @RequestParam(value = "parter_name", required = false, defaultValue = "") String parter_name,
			@ApiParam(value = "검색 작업명", required = false) @RequestParam(value = "name", required = false, defaultValue = "") String name,
			@ApiParam(value = "work_uid", required = false) @RequestParam(value = "work_uid", required = false, defaultValue = "") String work_uid,
			@ApiParam(value = "limit_offset (default_value is 0)", allowableValues = "{}") @RequestParam(value = "limit_offset", required = false, defaultValue = "0") int limit_offset,
			@ApiParam(value = "limit_count (default_value is 10)", allowableValues = "{}") @RequestParam(value = "limit_count", required = false, defaultValue = "-1") int limit_count,
			@ApiParam(value = "order_type") @RequestParam(value = "order_type", required = false, defaultValue = "name") String order_type,
			@ApiParam(value = "order_desc_asc") @RequestParam(value = "order_desc_asc", required = false, defaultValue = "asc") String order_desc_asc)
			throws NotFoundException {

		List<WorkMntVo> dataList = null;
		int totalcnt = 0;
		try {
			WorkMntRequestVo requestVo = new WorkMntRequestVo();
			if (from.equals("0") == false && to.equals("0") == false) {
				requestVo.setFrom(from);
				requestVo.setTo(to);
			}
			requestVo.setParter_name(parter_name);
			requestVo.setName(name);
			requestVo.setLimit_count(limit_count);
			requestVo.setLimit_offset(limit_offset);
			requestVo.setOrder_type(order_type);
			requestVo.setWork_uid(work_uid);
			requestVo.setOrder_desc_asc(order_desc_asc);

			dataList = workApiService.workMntList(requestVo);
			totalcnt = workApiService.selectWorkMntTotalCount(requestVo);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkMntList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkMntList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<WorkMntList>(HttpStatus.NO_CONTENT);
		} else {

			for (int i = 0; i < dataList.size(); i++) {
				if (dataList.get(i).getWork_type_name() != null) {
					String[] array = dataList.get(i).getWork_type_name().split(",");
					String num2 = "";
					for (int j = 0; j < array.length; j++) {
						num2 = getWorkTypeImagePath(array, num2, j);
						dataList.get(i).setWork_type_img(num2);
					}
				}
			}

			WorkMntList vo = new WorkMntList();
			vo.setMsg("SUCCESS");
			vo.setWorkMntList(dataList);
			vo.setTotalcnt(totalcnt);
			return new ResponseEntity<WorkMntList>(vo, HttpStatus.OK);
		}

	}

	private String getWorkTypeImagePath(String[] array, String num2, int j) {
		String num = null;
		switch (array[j]) {
		case "화기":
			num = "1";
			break;
		case "밀폐공간":
			num = "2";
			break;
		case "일반 위험A":
			num = "3";
			break;
		case "전기":
			num = "4";
			break;
		case "방사선":
			num = "5";
			break;
		case "중장비":
			num = "6";
			break;
		case "굴착":
			num = "7";
			break;
		case "일반 위험B":
			num = "8";
			break;
		case "고소":
			num = "9";
			break;
		default:
			num = "0";
			break;
		}
		num2 += "<img src='../resources/img/icon_work_" + num + ".gif' alt='" + array[j] + "' title='"
				+ array[j] + "'>";
		return num2;
	}

	@SuppressWarnings("unused")
	@ApiOperation(value = "", notes = "작업 정보를 등록한다.", response = WorkCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업 정보 등록을수행한다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/workInsert.do", method = RequestMethod.POST)
	public ResponseEntity<WorkCud> v1wwmsWorkInsertGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "work info", required = true) @RequestBody WorkVo workVo) throws NotFoundException {
		int result = 0;
		String msg = Return_Message.SUCCESS.getMessage();
		HttpStatus statusCode = HttpStatus.OK;
		try {
			logger.info("WorkVo:" + workVo);

			result = workApiService.workInsert(workVo);

			if (result < 0)
				statusCode = HttpStatus.I_AM_A_TEAPOT;

			if (result == -801)
				msg = "FAIL|이미 존재하는 작업 번호 입니다.";

			WorkCud vo = new WorkCud();
			vo.setMsg("SUCCESS");
			vo.setWorkCud(workVo);

			return new ResponseEntity<WorkCud>(vo, statusCode);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value = "", notes = "작업 정보를 수정한다.", response = WorkCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업 정보를수정을수행한다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/workUpdate.do", method = RequestMethod.POST)
	public ResponseEntity<WorkCud> v1wwmsWorkUpdateGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "work info", required = true) @RequestBody WorkVo workVo) throws NotFoundException {
		try {
			logger.info("WorkVo:" + workVo);

			workApiService.workUpdate(workVo);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (workVo == null) {
			return new ResponseEntity<WorkCud>(HttpStatus.NO_CONTENT);
		} else {
			WorkCud vo = new WorkCud();
			vo.setMsg("SUCCESS");
			vo.setWorkCud(workVo);
			return new ResponseEntity<WorkCud>(vo, HttpStatus.OK);
		}

	}

	@ApiOperation(value = "", notes = "작업 정보를 삭제한다.", response = WorkCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업 정보 삭제을수행한다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/workDelete.do", method = RequestMethod.POST)
	public ResponseEntity<WorkCud> v1wwmsWorkDeleteGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "work info", required = true) @RequestBody WorkVo workVo) throws NotFoundException {
		try {
			logger.info("WorkVo:" + workVo);

			workApiService.workDelete(workVo);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (workVo == null) {
			return new ResponseEntity<WorkCud>(HttpStatus.NO_CONTENT);
		} else {
			WorkCud vo = new WorkCud();
			vo.setMsg("SUCCESS");
			vo.setWorkCud(workVo);
			return new ResponseEntity<WorkCud>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "완료된 작업 정보를 삭제한다. (beacon Event, Gps Event 모두 삭제", response = WorkCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "완료된 작업 정보 삭제을 수행한다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/complatedWorkDelete.do", method = RequestMethod.POST)
	public ResponseEntity<WorkCud> v1wwmsComplatedWorkDelete(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "work info", required = true) @RequestBody WorkVo workVo) throws NotFoundException {
		try {
			logger.info("WorkVo:" + workVo);
			workApiService.complatedWorkDelete(workVo);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (workVo == null) {
			return new ResponseEntity<WorkCud>(HttpStatus.NO_CONTENT);
		} else {
			WorkCud vo = new WorkCud();
			vo.setMsg("SUCCESS");
			vo.setWorkCud(workVo);
			return new ResponseEntity<WorkCud>(vo, HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "unused" })
	@ApiOperation(value = "", notes = "새로운 작업 번호를 가져온다.", response = WorkCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "새로운 작업 번호를 가져온다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/newWorkNo.do", method = RequestMethod.GET)
	public ResponseEntity<WorkCud> v1wwmsNewWorkNoGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "name") @RequestParam(value = "name", required = false, defaultValue = "") String name)
			throws NotFoundException {

		WorkVo data = new WorkVo();

		try {
			String work_no = workApiService.makeWorkNo(name);
			data.setWork_no(work_no);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (data == null) {
			return new ResponseEntity<WorkCud>(HttpStatus.NO_CONTENT);
		} else {
			WorkCud vo = new WorkCud();
			vo.setMsg("SUCCESS");
			vo.setWorkCud(data);
			return new ResponseEntity<WorkCud>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "작업자 정보를 수정한다.", response = WorkCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업자 정보를수정을수행한다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/workerUpdate.do", method = RequestMethod.POST)
	public ResponseEntity<WorkCud> v1wwmsWorkerUpdatePost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "work info", required = true) @RequestBody WorkerMntVo workerMntVo)
			throws NotFoundException {
		try {
			logger.info("workerMntVo:" + workerMntVo);
			int result = workApiService.updateWorkerAssigned(workerMntVo);
			logger.info("update Worker Assigned result [{}]", result);
			WorkCud vo = new WorkCud();
			vo.setMsg("SUCCESS");
			return new ResponseEntity<WorkCud>(vo, HttpStatus.OK);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "", notes = "완료된 작업자 정보를 삭제한다.", response = WorkCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "완료된 작업 정보 삭제을 수행한다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/complatedWorkerDelete.do", method = RequestMethod.POST)
	public ResponseEntity<WorkCud> v1wwmsComplatedWorkDeletePost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "work info", required = true) @RequestBody WorkerMntVo workerMntVo)
			throws NotFoundException {
		try {
			logger.info("workerMntVo:" + workerMntVo);
			int result = workApiService.deleteWorkerAssigned(workerMntVo);
			logger.info("update Worker Assigned result [{}]", result);
			WorkCud vo = new WorkCud();
			vo.setMsg("SUCCESS");
			return new ResponseEntity<WorkCud>(vo, HttpStatus.OK);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "", notes = "작업 상세정보를 가져온다.", response = WorkMntList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "작업 상세정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/workDetail.do", method = RequestMethod.GET)
	public ResponseEntity<WorkDetail> v1wwmsworkDetailGet(
			@ApiParam(value = "work_uid", required = false) @RequestParam(value = "work_uid", required = true) String work_uid)
			throws NotFoundException {
		WorkMntVo workMntVo = null;

		try {
			workMntVo = workApiService.selectWorkDetail(work_uid);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<WorkDetail>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<WorkDetail>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (StringUtils.isEmpty(workMntVo)) {
			return new ResponseEntity<WorkDetail>(HttpStatus.NO_CONTENT);
		} else {
			WorkDetail response = new WorkDetail();
			response.setMsg("SUCCESS");
			response.setWorkMntVo(workMntVo);
			return new ResponseEntity<WorkDetail>(response, HttpStatus.OK);
		}
	}
}
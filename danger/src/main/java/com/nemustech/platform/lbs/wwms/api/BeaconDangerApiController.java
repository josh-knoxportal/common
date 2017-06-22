package com.nemustech.platform.lbs.wwms.api;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.vo.ResponseLoginVo;
import com.nemustech.platform.lbs.common.vo.SearchVo;
import com.nemustech.platform.lbs.wwms.model.BeaconDangerCud;
import com.nemustech.platform.lbs.wwms.model.BeaconDangerList;
import com.nemustech.platform.lbs.wwms.service.BeaconDangerApiService;
import com.nemustech.platform.lbs.wwms.vo.BeaconDangerVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_V1_ROOT, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class BeaconDangerApiController extends DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(BeaconDangerApiController.class);

	@Autowired
	private BeaconDangerApiService BeaconDangerApiService;

	@ApiOperation(value = "", notes = "위험지역 비콘관리 목록 정보를 가져온다.", response = BeaconDangerList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/beacon.do", method = RequestMethod.GET)
	public ResponseEntity<BeaconDangerList> v1WwmsBeaconDangerListGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "current page index (default_value is 0)", allowableValues = "{}") @RequestParam(value = "page_index", required = false, defaultValue = "0") int page_index,
			@ApiParam(value = "count per page (-1일 경우 모든 정보 가져오기) (default_value is 10)", allowableValues = "{}") @RequestParam(value = "record_count_per_page", required = false, defaultValue = "10") int record_count_per_page,
			@ApiParam(value = "search_filter") @RequestParam(value = "search_filter", required = false, defaultValue = "") String search_filter,
			@ApiParam(value = "search_type") @RequestParam(value = "search_type", required = false, defaultValue = "") String search_type,
			@ApiParam(value = "order_type") @RequestParam(value = "order_type", required = false, defaultValue = "") String order_type,
			@ApiParam(value = "order_desc_asc") @RequestParam(value = "order_desc_asc", required = false, defaultValue = "") String order_desc_asc,
			HttpServletRequest request) throws NotFoundException {

		logger.info("enter v1WwmsBeaconDangerListGet");

		BeaconDangerList response = null;

		try {
			SearchVo searchVo = new SearchVo();
			searchVo.setLimit_count(record_count_per_page);
			searchVo.setLimit_offset(page_index);
			searchVo.setSearch_filter(search_filter);
			searchVo.setSort_column(order_type);
			searchVo.setStr_order_by(order_desc_asc);
			response = BeaconDangerApiService.selectBeaconDangerList(searchVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<BeaconDangerList>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<BeaconDangerList>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (response == null) {
			logger.info("[v1WwmsBeaconDangerListGet] BeaconDangerList is null");
			return new ResponseEntity<BeaconDangerList>(HttpStatus.NO_CONTENT);
		} else {
			response.setMsg(Return_Message.SUCCESS.getMessage());
			return new ResponseEntity<BeaconDangerList>(response, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "위험지역 비콘관리 비콘정보를 가져온다.", response = BeaconDangerCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "비콘 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/beacon/{beacon_uid}.do", method = RequestMethod.GET)
	public ResponseEntity<BeaconDangerCud> v1WwmsBeaconDangerGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "UID of Device", required = true, allowableValues = "{}") @PathVariable(value = "beacon_uid") String beacon_uid)
			throws NotFoundException {

		BeaconDangerCud response = null;

		try {
			logger.info("beacon_uid:" + beacon_uid);

			BeaconDangerVo beaconDangerVo = new BeaconDangerVo();
			beaconDangerVo.setBeacon_uid(beacon_uid);
			response = BeaconDangerApiService.selectBeaconDanger(beaconDangerVo);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<BeaconDangerCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<BeaconDangerCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (response == null) {
			return new ResponseEntity<BeaconDangerCud>(HttpStatus.NO_CONTENT);
		} else {
			response.setMsg(Return_Message.SUCCESS.getMessage());
			return new ResponseEntity<BeaconDangerCud>(response, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "위험지역 비콘관리 비콘정보를 설정한다.", response = BeaconDangerCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/beacon.do", method = RequestMethod.POST)
	public ResponseEntity<BeaconDangerCud> v1WwmsBeaconDangerCudPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "operation code(insert,update,delete", required = true) @RequestHeader(value = "opcode", required = true) String opcode,
			@ApiParam(value = "단말 설정정보", required = true) @RequestBody BeaconDangerVo beaconDangerVo,
			HttpServletRequest request) throws NotFoundException {
		logger.info("opcode [{}]", opcode);

		String editor_id = null;
		ResponseLoginVo account = (ResponseLoginVo) WebUtils.getSessionAttribute(request,
				DefaultController.SESSION_ATTR_KEY_ACCOUNT);
		if (account != null) {
			editor_id = account.getUser_id();
			beaconDangerVo.setEditor_id(editor_id);
		}

		try {
			BeaconDangerApiService.beaconDangerCud(beaconDangerVo, opcode);
		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<BeaconDangerCud>(HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			logger.info("Exception", opcode);
			e.printStackTrace();
			return new ResponseEntity<BeaconDangerCud>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		BeaconDangerCud response = new BeaconDangerCud();
		response.setMsg(Return_Message.SUCCESS.getMessage());
		return new ResponseEntity<BeaconDangerCud>(response, HttpStatus.OK);
	}
}

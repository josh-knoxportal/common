package com.nemustech.platform.lbs.ngms.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.ngms.model.NaviDevice;
import com.nemustech.platform.lbs.ngms.model.NaviDeviceCud;
import com.nemustech.platform.lbs.ngms.model.NaviDeviceList;
import com.nemustech.platform.lbs.ngms.service.NaviDeviceApiService;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceAssignedVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceSearchVo;
import com.nemustech.platform.lbs.ngms.vo.NaviDeviceVo;

@Controller
@RequestMapping(value = "/v1/ngms", produces = "application/json;charset=utf8;")
@Api(value = "/v1/ngms", description = "the v1 API")
public class NaviDeviceApiController extends DefaultController {

	private static final Logger logger = LoggerFactory
			.getLogger(NaviDeviceApiController.class);

	@Autowired
	NaviDeviceApiService naviDeviceApiService;

	@ApiOperation(value = "", notes = "단말기 정보를 가져온다.", response = NaviDeviceList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "단말기 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/device.do", method = RequestMethod.GET)
	public ResponseEntity<NaviDeviceList> v1NgmsAdminAccountListGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "current page index (default_value is 0)", allowableValues = "{}") @RequestParam(value = "page_index", required = false, defaultValue = "0") int page_index,
			@ApiParam(value = "count per page (-1일 경우 모든 단말기정보 가져오기) (default_value is 10)", allowableValues = "{}") @RequestParam(value = "record_count_per_page", required = false, defaultValue = "-1") int record_count_per_page,
			@ApiParam(value = "device_no") @RequestParam(value = "device_no", required = false, defaultValue = "") String device_no,
			@ApiParam(value = "assigned_vehicle_no") @RequestParam(value = "assigned_vehicle_no", required = false, defaultValue = "") String assigned_vehicle_no,
			@ApiParam(value = "order_type") @RequestParam(value = "order_type", required = false, defaultValue = "") String order_type,
			@ApiParam(value = "order_desc_asc") @RequestParam(value = "order_desc_asc", required = false, defaultValue = "") String order_desc_asc)
			throws NotFoundException {

		List<NaviDeviceVo> dataList = null;
		int totalCnt = 0;
		NaviDeviceAssignedVo assignedVo = null;

		try {
			NaviDeviceSearchVo searchVo = new NaviDeviceSearchVo();
			searchVo.setLimit_count(record_count_per_page);
			searchVo.setLimit_offset(page_index);
			searchVo.setDevice_no(device_no);
			searchVo.setAssigned_vehicle_no(assigned_vehicle_no);
			searchVo.setOrder_type(order_type);
			searchVo.setOrder_desc_asc(order_desc_asc);

			dataList = naviDeviceApiService.selectNaviDeviceList(searchVo);
			totalCnt = naviDeviceApiService
					.selectNaviDeviceListTotalCnt(searchVo);
			assignedVo = naviDeviceApiService.selectNaviDeviceAssignedCount();

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<NaviDeviceList>(
					HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<NaviDeviceList>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<NaviDeviceList>(HttpStatus.NO_CONTENT);
		} else {
			NaviDeviceList vo = new NaviDeviceList();
			vo.setMsg(Return_Message.SUCCESS.getMessage());
			vo.setNaviDeiviceList(dataList);
			vo.setTotalCnt(totalCnt);
			vo.setNaviDeviceAssigned(assignedVo);
			return new ResponseEntity<NaviDeviceList>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "차단된 단말기 정보를 가져온다.", response = NaviDeviceList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "단말기 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/blockdevice.do", method = RequestMethod.GET)
	public ResponseEntity<NaviDeviceList> v1NgmsBlockAdminAccountListGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "current page index (default_value is 0)", allowableValues = "{}") @RequestParam(value = "page_index", required = false, defaultValue = "0") int page_index,
			@ApiParam(value = "count per page (-1일 경우 모든 단말기정보 가져오기) (default_value is 10)", allowableValues = "{}") @RequestParam(value = "record_count_per_page", required = false, defaultValue = "-1") int record_count_per_page,
			@ApiParam(value = "search_filter_popup") @RequestParam(value = "search_filter_popup", required = false, defaultValue = "") String search_filter_popup,
			@ApiParam(value = "order_type") @RequestParam(value = "order_type", required = false, defaultValue = "") String order_type,
			@ApiParam(value = "order_desc_asc") @RequestParam(value = "order_desc_asc", required = false, defaultValue = "") String order_desc_asc)
			throws NotFoundException {

		List<NaviDeviceVo> dataList = null;
		int totalCnt = 0;
		NaviDeviceAssignedVo assignedVo = null;

		try {
			NaviDeviceSearchVo searchVo = new NaviDeviceSearchVo();
			searchVo.setLimit_count(record_count_per_page);
			searchVo.setLimit_offset(page_index);
			searchVo.setSearch_filter_popup(search_filter_popup);
			searchVo.setOrder_type(order_type);
			searchVo.setOrder_desc_asc(order_desc_asc);

			dataList = naviDeviceApiService.selectBlockNaviDeviceList(searchVo);
			totalCnt = naviDeviceApiService
					.selectBlockNaviDeviceListTotalCnt(searchVo);
			// assignedVo =
			// naviDeviceApiService.selectNaviDeviceAssignedCount();

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<NaviDeviceList>(
					HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<NaviDeviceList>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (dataList == null) {
			return new ResponseEntity<NaviDeviceList>(HttpStatus.NO_CONTENT);
		} else {
			NaviDeviceList vo = new NaviDeviceList();
			vo.setMsg(Return_Message.SUCCESS.getMessage());
			vo.setNaviDeiviceList(dataList);
			vo.setTotalCnt(totalCnt);
			vo.setNaviDeviceAssigned(assignedVo);
			return new ResponseEntity<NaviDeviceList>(vo, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "", notes = "단말기 정보를 가져온다.", response = NaviDevice.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "단말기 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/device/{device_uid}.do", method = RequestMethod.GET)
	public ResponseEntity<NaviDevice> v1NgmsAdminAccountGet(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "UID of Device", required = true, allowableValues = "{}") @PathVariable(value = "device_uid") String device_uid)
			throws NotFoundException {

		NaviDeviceVo naviDeviceVo = null;
		try {
			logger.info("device_uid:" + device_uid);
			naviDeviceVo = new NaviDeviceVo();
			naviDeviceVo.setDevice_uid(device_uid);
			naviDeviceVo = naviDeviceApiService.selectNaviDevice(naviDeviceVo);

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<NaviDevice>(
					HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<NaviDevice>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (naviDeviceVo == null) {
			return new ResponseEntity<NaviDevice>(HttpStatus.NO_CONTENT);
		} else {
			NaviDevice vo = new NaviDevice();
			vo.setMsg(Return_Message.SUCCESS.getMessage());
			vo.setNaviDevice(naviDeviceVo);
			return new ResponseEntity<NaviDevice>(vo, HttpStatus.OK);
		}

	}

	@ApiOperation(value = "", notes = "단말기 정보를 추가/변경/삭제한다.", response = NaviDeviceCud.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "단말기 정보를 보낸다."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/device.do", method = RequestMethod.POST)
	public ResponseEntity<NaviDeviceCud> v1NgmsAdminAccountPost(
			@ApiParam(value = "admin access token.", required = true) @RequestHeader(value = "access_token", required = true) String access_token,
			@ApiParam(value = "request type", required = true) @RequestHeader(value = "request_type", required = true) String request_type,
			@ApiParam(value = "operation code(insert,update,delete", required = true) @RequestHeader(value = "opcode", required = true) String opcode,
			@ApiParam(value = "device info", required = true) @RequestBody NaviDeviceVo naviDeviceVo)
			throws NotFoundException {

		try {
			logger.info("naviDeviceVo:" + naviDeviceVo);

			if ("create".equals(opcode)) {
				if (isExistDeviceNo(naviDeviceVo.getDevice_no())) {
					NaviDeviceVo deviceInfo = null;

					deviceInfo = new NaviDeviceVo();
					deviceInfo.setDevice_no(naviDeviceVo.getDevice_no());
					deviceInfo = naviDeviceApiService
							.selectNaviDevice(deviceInfo);

					if (deviceInfo.getIs_used() == 1) {
						NaviDeviceCud vo = new NaviDeviceCud();
						vo.setMsg("EXISTDEVICENO");
						return new ResponseEntity<NaviDeviceCud>(vo,
								HttpStatus.OK);
					} else {
						naviDeviceVo.setDevice_uid(deviceInfo.getDevice_uid());
						naviDeviceVo.setReg_date(deviceInfo.getReg_date());
						naviDeviceApiService.updateNaviDevice(naviDeviceVo);
					}
				} else {
					naviDeviceApiService.createNaviDevice(naviDeviceVo);
				}
			} else if ("update".equals(opcode)) {
				naviDeviceApiService.updateNaviDevice(naviDeviceVo);
			} else if ("delete".equals(opcode)) {
				naviDeviceApiService.removeNaviDevie(naviDeviceVo);
			} else if ("release".equals(opcode)) {
				naviDeviceApiService.releaseNaviDevice(naviDeviceVo);
			} else if ("block".equals(opcode)) {
				naviDeviceApiService.blockNaviDevice(naviDeviceVo);
			}

		} catch (DataAccessResourceFailureException e) {
			return new ResponseEntity<NaviDeviceCud>(
					HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<NaviDeviceCud>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (naviDeviceVo == null) {
			return new ResponseEntity<NaviDeviceCud>(HttpStatus.NO_CONTENT);
		} else {
			NaviDeviceCud vo = new NaviDeviceCud();
			vo.setMsg(Return_Message.SUCCESS.getMessage());
			vo.setNaviDevice(naviDeviceVo);
			return new ResponseEntity<NaviDeviceCud>(vo, HttpStatus.OK);
		}

	}

	private boolean isExistDeviceNo(String device_no) throws Exception {
		if (naviDeviceApiService.isExistDeviceNo(device_no)) {
			return true;
		}
		return false;
	}
}

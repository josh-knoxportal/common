package com.nemustech.platform.lbs.web.danger.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.wwms.service.WorkApiService;
import com.nemustech.platform.lbs.wwms.vo.WorkIssueVo;
import com.nemustech.platform.lbs.wwms.vo.WorkMntRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkMntVo;
import com.nemustech.platform.lbs.wwms.vo.WorkStatusRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkStatusVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerMntRequestVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerMntVo;

import io.swagger.annotations.ApiParam;

/**
 * Handles requests for the application home page.
 * 
 */
@Controller("WorkExcleController")
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_ROOT + "excel/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8;")
public class WorkExcelController extends DefaultController {

	// private static final Logger logger =
	// LoggerFactory.getLogger(WorkExcelController.class);

	@Autowired
	private WorkApiService workApiService;

	@RequestMapping(value = "workerMnt_excel.do", method = RequestMethod.GET)
	public String workerMntExcelDownload(
			@RequestParam(value = "target", required = false, defaultValue = "") String target,
			@ApiParam(value = "작업자 이름 검색", required = false) @RequestParam(value = "worker_name", required = false, defaultValue = "") String worker_name,
			@ApiParam(value = "폰번호 검색", required = false) @RequestParam(value = "device_no", required = false, defaultValue = "") String device_no,
			@ApiParam(value = "공장명 검색", required = false) @RequestParam(value = "parter_name", required = false, defaultValue = "") String parter_name,
			@ApiParam(value = "작업명 검색", required = false) @RequestParam(value = "name", required = false, defaultValue = "") String name,
			@ApiParam(value = "작업번호 검색", required = false) @RequestParam(value = "work_no", required = false, defaultValue = "") String work_no,
			Model ModelMap) throws NotFoundException {

		List<WorkerMntVo> excelList = null;

		WorkerMntRequestVo requestVo = new WorkerMntRequestVo();
		requestVo.setWorker_name(worker_name);
		requestVo.setDevice_no(device_no);
		requestVo.setParter_name(parter_name);
		requestVo.setName(name);
		requestVo.setWork_no(work_no);

		try {
			excelList = workApiService.workerMntList(requestVo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < excelList.size(); i++) {
			if (!StringUtils.isEmpty(excelList.get(i).getDevice_no())) {
				String phone_num = excelList.get(i).getDevice_no();
				StringBuffer bf = new StringBuffer(phone_num);
				String num = bf.replace(phone_num.length() - 4, phone_num.length(), "****").toString();
				excelList.get(i).setDevice_no(num);
			}
		}

		ModelMap.addAttribute("excelList", excelList);
		ModelMap.addAttribute("target", target);

		return "excelDownloadService";
	}

	@RequestMapping(value = "workMnt_excel.do", method = RequestMethod.GET)
	public String workMntExcelDownload(
			@RequestParam(value = "target", required = false, defaultValue = "") String target,
			@ApiParam(value = "검색시작일", required = false) @RequestParam(value = "from", required = false, defaultValue = "0") String from,
			@ApiParam(value = "검색종료일", required = false) @RequestParam(value = "to", required = false, defaultValue = "0") String to,
			@ApiParam(value = "공장명 검색", required = false) @RequestParam(value = "parter_name", required = false, defaultValue = "") String parter_name,
			@ApiParam(value = "작업명 검색", required = false) @RequestParam(value = "name", required = false, defaultValue = "") String name,
			Model ModelMap) throws NotFoundException {

		List<WorkMntVo> excelList = null;

		WorkMntRequestVo requestVo = new WorkMntRequestVo();
		requestVo.setFrom(from);
		requestVo.setTo(to);
		requestVo.setParter_name(parter_name);
		requestVo.setName(name);

		try {
			excelList = workApiService.workMntList(requestVo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < excelList.size(); i++) {
			if (!StringUtils.isEmpty(excelList.get(i).getDevice_no())) {
				String phone_num = excelList.get(i).getDevice_no();
				StringBuffer bf = new StringBuffer(phone_num);
				String num = bf.replace(phone_num.length() - 4, phone_num.length(), "****").toString();

				excelList.get(i).setDevice_no(num);
			}
		}

		ModelMap.addAttribute("excelList", excelList);
		ModelMap.addAttribute("target", target);

		return "excelDownloadService";
	}

	@RequestMapping(value = "workIssue_excel.do", method = RequestMethod.GET)
	public String workIssueExcelDownload(
			@RequestParam(value = "target", required = false, defaultValue = "") String target,
			@ApiParam(value = "검색시작일", required = false) @RequestParam(value = "from", required = false, defaultValue = "0") String from,
			@ApiParam(value = "검색종료일", required = false) @RequestParam(value = "to", required = false, defaultValue = "0") String to,
			Model ModelMap) throws NotFoundException {

		List<WorkIssueVo> excelList = null;

		WorkStatusRequestVo requestVo = new WorkStatusRequestVo();
		requestVo.setFrom(from);
		requestVo.setTo(to);

		try {
			excelList = workApiService.workIssueList(requestVo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < excelList.size(); i++) {
			if (!StringUtils.isEmpty(excelList.get(i).getDevice_no())) {
				String phone_num = excelList.get(i).getDevice_no();
				StringBuffer bf = new StringBuffer(phone_num);
				String num = bf.replace(phone_num.length() - 4, phone_num.length(), "****").toString();
				excelList.get(i).setDevice_no(num);
			}
		}

		ModelMap.addAttribute("excelList", excelList);
		ModelMap.addAttribute("target", target);

		return "excelDownloadService";
	}

	@RequestMapping(value = "workStatus_excel.do", method = RequestMethod.GET)
	public String workStatusExcelDownload(
			@RequestParam(value = "target", required = false, defaultValue = "") String target,
			@ApiParam(value = "검색시작일", required = false) @RequestParam(value = "from", required = false, defaultValue = "0") String from,
			@ApiParam(value = "검색종료일", required = false) @RequestParam(value = "to", required = false, defaultValue = "0") String to,
			Model ModelMap) throws NotFoundException {

		List<WorkStatusVo> excelList = null;

		WorkStatusRequestVo requestVo = new WorkStatusRequestVo();
		requestVo.setFrom(from);
		requestVo.setTo(to);

		try {
			excelList = workApiService.workStatusList(requestVo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < excelList.size(); i++) {
			if (!StringUtils.isEmpty(excelList.get(i).getDevice_no())) {
				String phone_num = excelList.get(i).getDevice_no();
				StringBuffer bf = new StringBuffer(phone_num);
				String num = bf.replace(phone_num.length() - 4, phone_num.length(), "****").toString();
				excelList.get(i).setDevice_no(num);
			}
		}

		ModelMap.addAttribute("excelList", excelList);
		ModelMap.addAttribute("target", target);

		return "excelDownloadService";
	}
}

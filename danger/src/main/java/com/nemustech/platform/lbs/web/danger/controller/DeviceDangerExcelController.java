package com.nemustech.platform.lbs.web.danger.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nemustech.platform.lbs.common.api.NotFoundException;
import com.nemustech.platform.lbs.common.controller.DefaultController;
import com.nemustech.platform.lbs.common.vo.SearchVo;
import com.nemustech.platform.lbs.wwms.mapper.DeviceDangerApiMapper;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerVo;

@Controller
@RequestMapping(value = DefaultController.REQUESTMAPPING_DANGER_ROOT+"excel/", produces = MediaType.APPLICATION_JSON_VALUE
		+ ";charset=utf-8;")
public class DeviceDangerExcelController extends DefaultController {

	private static final Logger logger = LoggerFactory
			.getLogger(DeviceDangerExcelController.class);

	@Autowired
	private DeviceDangerApiMapper deviceDangerApiMapper;

	@RequestMapping(value = "device_danger_excel.do", method = RequestMethod.GET)
	public String deviceDangerListExcelDownload(
			@RequestParam(value = "target", required = true, defaultValue = "danger_device_list") String target,
			Model ModelMap) throws NotFoundException {

		List<DeviceDangerVo> excelList = null;

		logger.info("param target [{}]", target);

		try {
			SearchVo searchVo = new SearchVo();
			// searchVo.setLimit_count(record_count_per_page);
			// searchVo.setLimit_offset(-1);
			// searchVo.setSearch_filter(search_filter);
			searchVo.setSort_column("");
			searchVo.setStr_order_by("");
			excelList = deviceDangerApiMapper.selectDeviceDangerList(searchVo);
		} catch (Exception e) {
			logger.info("Exception e [{}]", e.getMessage());
			e.printStackTrace();
		}

		for (int i = 0; i < excelList.size(); i++) {
			if (excelList.get(i).getDevice_no() != null) {
				String phone_num = excelList.get(i).getDevice_no();
				StringBuffer bf = new StringBuffer(phone_num);
				String num = bf.replace(phone_num.length() - 4,
						phone_num.length(), "****").toString();

				excelList.get(i).setDevice_no(num);
			}
		}

		ModelMap.addAttribute("excelList", excelList);
		ModelMap.addAttribute("target", target);

		return "excelDownloadService";
	}

}

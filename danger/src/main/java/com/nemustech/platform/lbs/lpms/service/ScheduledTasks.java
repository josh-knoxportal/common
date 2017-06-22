package com.nemustech.platform.lbs.lpms.service;

import java.io.StringReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.nemustech.common.util.Utils;
import com.nemustech.platform.lbs.common.service.PropertyService;
import com.nemustech.platform.lbs.lpms.vo.LpmsWorksVo;

@Service
public class ScheduledTasks {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	public static final String RETURNcODE = "0";

	public static final int INIT_NUM = 0;

	@Resource(name = "properties")
	protected Properties properties;

	@Autowired
	public Jaxb2Marshaller jaxb2Mashaller;

	@Autowired
	protected PropertyService config;

	@Autowired
	LpmsApiService lpmsService;

//	@Scheduled(cron = "*/1 * * * * *") // test
	@Scheduled(cron = "0 0 3 * * *") // s m h d m day
	public void lpmsGetHtmltoSring() {

		if (!Utils.isValidate(properties.getProperty("buildtime.interworking.type")))
			return;
//		logger.info("test"); // test

		String strToday;
		strToday = getStrToday("yyyyMMdd");

		if (!isTodayLpmsDataExist(strToday)) {
			lpmsDataToDanger(strToday);
		}

	}

	protected void lpmsDataToDanger(String strToday) {
		int result = 0;
		int nDataCount = 0;

		LpmsWorksVo lpmsWorksVo = getLpmsRestfulService(strToday);

		if (!StringUtils.isEmpty(lpmsWorksVo)) {

			if (!RETURNcODE.equals(lpmsWorksVo.getReturnCode()) && !StringUtils.isEmpty(lpmsWorksVo.getReturnCode())) {
				logger.info("Lpms ReturnCode [{}]", lpmsWorksVo.getReturnCode());
			} else {

				logger.info("Lpms Return Data Total Count [{}]", lpmsWorksVo.getReturnTotalCount());
				nDataCount = Integer.parseInt(lpmsWorksVo.getReturnTotalCount());

				if (nDataCount > 0) {
					insertLpmsData(lpmsWorksVo);
				}
			}

			logger.info("Lpms input Data Schedule End [{}]", result);
		}
	}

	protected void insertLpmsData(LpmsWorksVo lpmsWorksVo) {
		try {
			lpmsService.insertLpmsData(lpmsWorksVo);
		} catch (Exception e) {
			logger.info("Lpms input Data Exception[{}]", e.getMessage());
		}
	}

	protected boolean isTodayLpmsDataExist(String strToday) {
		int nCount = 0;

		try {
			nCount = lpmsService.selectWorkCount(strToday);
		} catch (Exception e) {
			logger.info("Lpms DataBAse input Data nCount Exception[{}]", e.getMessage());
			return false;
		}

		logger.debug("Lpms DataBAse input Data nCount [{}]", nCount);
		if (nCount > 0)
			return true;
		else
			return false;
	}

	protected String getStrToday(String format) {
		String strToday = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c1 = Calendar.getInstance();
		strToday = sdf.format(c1.getTime());
		logger.info("The time is str_today {}", strToday);
		return strToday;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected LpmsWorksVo getLpmsRestfulService(String strToday) {

		String lpms_target_url = config.getString("smartplant.to.lpms.url");
		logger.info("Lpms target Url [{}]", lpms_target_url);

		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);

		URI targetUrl = UriComponentsBuilder.fromUriString(lpms_target_url).queryParam("WORK_DATE", strToday).build()
				.toUri();

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.GET, new HttpEntity(header),
				String.class);

		logger.debug("Lpms Data getHeaders [{}]", response.getHeaders());
		logger.info("Lpms Data getBody [{}]", response.getBody());

		Source source = new StreamSource(new StringReader(response.getBody()));
		LpmsWorksVo lpmsWorksVo = (LpmsWorksVo) jaxb2Mashaller.unmarshal(source);

		logger.debug("Lpms Data LpmsWorksVo unmarshal-------- [{}]", lpmsWorksVo.toString());
		return lpmsWorksVo;
	}
}

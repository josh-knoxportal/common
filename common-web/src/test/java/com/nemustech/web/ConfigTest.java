package com.nemustech.web;

import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-test.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigTest {
	private static Log log = LogFactory.getLog(ConfigTest.class);

	// 기본형
	@Autowired
	Properties properties;

	// 수동형
	@Value("${key1}")
	String value1;

	// 능동형
	@Value("#{properties['key2']}")
	String value2;

	// 조합형
	@Resource(name = "properties2")
	String properties2;

	// 갱신형
	@Resource(name = "propertiesConfiguration")
	Configuration configuration;

//	@Autowired
//	MessageSource messageSource;

	@Test
	public void t01() throws Exception {
//		while (true) {
		log.info("properties: " + properties);
		log.info("key1: " + value1);
		log.info("key2: " + value2);
		log.info("properties2: " + properties2);
		log.info("key1_reload: " + configuration.getProperty("key1"));

//		String message = messageSource.getMessage("skoh", null, null);
//		String usMessage = messageSource.getMessage("skoh", null, "skoh2", Locale.US);
//		String korMessage = messageSource.getMessage("skoh", null, "skoh2", Locale.KOREA);
////		String localeMessage = messageSource.getMessage("hello.test",null, "no surch", localeResolver.resolveLocale(request)); //현재 설정된 위치 기준의 메시지 요청
//		log.info(message);
//		log.info(usMessage);
//		log.info(korMessage);

//		Assert.assertTrue("model == null", model != null);
//			log.info("---------------------------------");
//			Thread.currentThread().sleep(5000);
//		}
	}
}
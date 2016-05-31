package org.oh.web;

import java.util.Locale;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-test.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConfig {
	private static Log log = LogFactory.getLog(TestConfig.class);

	@Autowired
	Properties prop;

	@Value("#{prop['key1']}")
	String value1;

	@Autowired
	String value2;

	@Autowired
	private MessageSource messageSource;

	@Test
	public void t01() throws Exception {
//		log.info(prop);
//		log.info(value1);
//		log.info(value2);

		String message = messageSource.getMessage("skoh", null, null);
		String usMessage = messageSource.getMessage("skoh", null, "skoh2", Locale.US);
		String korMessage = messageSource.getMessage("skoh", null, "skoh2", Locale.KOREA);
//		String localeMessage = messageSource.getMessage("hello.test",null, "no surch", localeResolver.resolveLocale(request)); //현재 설정된 위치 기준의 메시지 요청
		log.info(message);
		log.info(usMessage);
		log.info(korMessage);

//		Assert.assertTrue("model == null", model != null);
	}
}
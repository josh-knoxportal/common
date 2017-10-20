package com.nemustech.common;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nemustech.common.util.SpringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-test.xml")
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigTest extends CommonTest {
	// 기본형
//	@Autowired
//	Properties properties;

	// 수동형
//	@Value("${key1}")
//	String value1;
//
//	// 능동형
//	@Value("#{properties['key2']}")
//	String value2;

//	@Value("${java.net.preferIPv4Stack}") ${key1}")
//	@Value("#{systemProperties['spring.profiles.active']}") #{properties['key2']}")
//	@Value("#{${key1} #{properties['key2']}")
	@Value("#{@'org.springframework.context.support.PropertySourcesPlaceholderConfigurer#0'.toString()}")
	String value3;

//	@Value("#{messageSource}")
//	MessageSourceSupport value4;
//
//	@Value("#{@messageSource}")
//	MessageSourceSupport value5;
//
//	@Value("#{#this}")
//	Object value6;

	// 조합형
//	@Resource(name = "properties2")
//	String properties2;

	// 갱신형
//	@Resource(name = "properties")
//	Properties properties;

//	@Resource(name = "propertiesConfiguration")
//	Configuration configuration;
//
//	@Resource(name = "xmlConfiguration")
//	FileConfiguration fileConfiguration;

//	@Autowired
//	MessageSource messageSource;

	@Test
	public void t01() throws Exception {
//		while (true) {
//		log.info("properties: " + properties);
//		log.info("key1: " + value1);
//		log.info("key2: " + value2);
		log.info("key3: " + value3);
//		log.info(SpringUtil.getPlaceholderResult("${java.net.preferIPv4Stack}") ${key1}"));
//		log.info(SpringUtil.getSPELResult("#{systemProperties['spring.profiles.active']}") #{properties['key2']}"));
//		log.info(SpringUtil.getPlaceholderSPELResult("${key1} #{properties['key2']}"));
		log.info(SpringUtil.getPlaceholderSPELResult(
				"#{@'org.springframework.context.support.PropertySourcesPlaceholderConfigurer#0'.toString()}"));

//		log.info("key4: " + value4);
//		log.info("key5: " + value5);
//		log.info("key6: " + value6);
//		log.info(SpringUtil.getEvaluationResult("#{messageSource}"));
//		log.info(SpringUtil.getEvaluationResult("#{@messageSource}"));
//		log.info(SpringUtil.getEvaluationResult("#{#root}"));
//		log.info(SpringUtil.getEvaluationResult("#{#this}"));

//		log.info("properties2: " + properties2);
//		log.info("key1_reload: " + configuration.getProperty("key1"));
//		log.info("key2_reload: " + fileConfiguration.getProperty("key2"));

//		System.out.println("properties: " + properties);

		// -Dorg.oh.home=/Users/skoh/smartplant_safety
		// <value>#{systemProperties['org.oh.home']}/config/vehicle/smartplant-safety.xml</value>
//		String xml = IOUtils.toString(fileConfiguration.getURL(), Charset.defaultCharset());
//		System.out.println("xml: " + xml);
//		XMLJsonUtils2 xmlJsonUtils = new XMLJsonUtils2("smartplant-safety", "object", "element");
//		String json = xmlJsonUtils.convertXmlStringToJsonString(xml);
//		System.out.println("json: " + JsonUtil2.toStringPretty(json));

//		String message = messageSource.getMessage("skoh", null, null);
//		String usMessage = messageSource.getMessage("skoh", null, "skoh2", Locale.US);
//		String korMessage = messageSource.getMessage("skoh", null, "skoh2", Locale.KOREA);
////		String localeMessage = messageSource.getMessage("hello.test",null, "no surch", localeResolver.resolveLocale(request)); //현재 설정된 위치 기준의 메시지 요청
//		log.info(message);
//		log.info(usMessage);
//		log.info(korMessage);

//		Assert.assertTrue("model == null", model != null);
//		Thread.currentThread().sleep(10000);
//		log.info("key1_reload: " + configuration.getProperty("key1"));
//		log.info("key2_reload: " + fileConfiguration.getProperty("key2"));

//		ExpressionParser parser = new SpelExpressionParser();
//		StandardEvaluationContext context = new StandardEvaluationContext();
//		context.setBeanResolver(new MyBeanResolver());

		// This will end up calling resolve(context,"foo") on MyBeanResolver during evaluation
//		Object bean = parser.parseExpression("@messageSource").getValue(context);
	}
}
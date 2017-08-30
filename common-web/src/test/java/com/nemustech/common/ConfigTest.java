package com.nemustech.common;

import javax.annotation.Resource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-test.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigTest {
	private static Log log = LogFactory.getLog(ConfigTest.class);

	@Autowired
	ConfigurableBeanFactory beanFactory;

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

//	@Value("${spring.profiles.active:default}")
//	@Value("#{systemProperties['spring.profiles.active']}")
//	@Value("#{DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')}")
//	String value3;

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

	@Resource(name = "propertiesConfiguration")
	Configuration configuration;
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
//		log.info("key3: " + value3);
//		log.info(SpringUtil.getEvaluationResult("#{${key1}}", beanFactory));
//		log.info(SpringUtil.getEvaluationResult("#{properties['key2']}", beanFactory));
//		log.info(SpringUtil.getEvaluationResult("#{${spring.profiles.active}}"));
//		StandardBeanExpressionResolver resolver = new StandardBeanExpressionResolver();
//		BeanExpressionContext rootObject = new BeanExpressionContext(beanFactory, null);
//		log.info(resolver.evaluate("#{${spring.profiles.active}}", rootObject));

//		log.info("key4: " + value4);
//		log.info("key5: " + value5);
//		log.info("key6: " + value6);
//		log.info(SpringUtil.getEvaluationResult("#{messageSource}", beanFactory));
//		log.info(SpringUtil.getEvaluationResult("#{@messageSource}", beanFactory));
//		log.info(SpringUtil.getEvaluationResult("#{#this}", beanFactory));

//		log.info("properties2: " + properties2);
		log.info("key1_reload: " + configuration.getProperty("key1"));
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
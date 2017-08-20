package com.nemustech.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.MessageSourceSupport;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nemustech.common.util.SpringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-test.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigTest {
	private static Log log = LogFactory.getLog(ConfigTest.class);

	@Autowired
	ApplicationContext context;

	// 기본형
//	@Autowired
//	Properties properties;
//
//	// 수동형
//	@Value("${key1}")
//	String value1;
//
//	// 능동형
//	@Value("#{properties['key2']}")
//	String value2;

//	@Value("${org.oh.home}")
//	@Value("#{systemProperties['org.oh.home']}")
//	@Value("#{DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')}")
//	String value3;

	@Value("#{messageSource}")
	MessageSourceSupport value4;
//
//	// 조합형
//	@Resource(name = "properties2")
//	String properties2;
//
//	// 갱신형
//	@Resource(name = "configuration")
//	Configuration configuration;

//	@Resource(name = "properties")
//	Properties properties;
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
		log.info("key4: " + value4);
//		log.info("properties2: " + properties2);
//		log.info("key1_reload: " + configuration.getProperty("key1"));
//		log.info("key2_reload: " + fileConfiguration.getProperty("key2"));

		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setBeanResolver(new BeanFactoryResolver(this.context));

		System.out.println(SpringUtil.getEvaluationResult("#{@messageSource}"));

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
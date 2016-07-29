package org.oh.agent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oh.agent.AgentApplication;
import org.oh.agent.PropertyUtils;
import org.oh.agent.service.SampleService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:conf/spring-config.xml")
public class AgentApplicationTest {
	@Autowired
	protected SampleService sampleService;

	@BeforeClass
	public static void beforeClass() throws Exception {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.reset();

		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(context);
		configurator.doConfigure("conf/logback.xml");
	}

	@Before
	public void init() {
		new AgentApplication();
	}

	@Test
	public void test01() {
		System.out.println(PropertyUtils.getString("message"));
		Assert.assertNotNull("서비스 객체가 null입니다.", sampleService);
	}

	@Test
	public void test02() {
		sampleService.task();
	}
}

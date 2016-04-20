package org.oh.web;

import java.util.Properties;

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
public class TestConfig {
	private static Log log = LogFactory.getLog(TestConfig.class);
	
	@Autowired
	Properties prop;

	@Value("#{prop['key1']}")
	String value1;

	@Autowired
	String value2;

	@Test
	public void t01() throws Exception {
		log.info(prop);
		log.info(value1);
		log.info(value2);

//		Assert.assertTrue("model == null", model != null);
	}
}
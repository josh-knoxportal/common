package org.oh;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebApplication.class)
@WebAppConfiguration

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebApplicationTests {
	protected Log log = LogFactory.getLog(getClass());

	@Autowired
	protected MessageSource messageSource;

	@Test
	public void test02() {
		log.info(messageSource.getMessage("field.required", new String[] { "은" }, Locale.KOREA));
		log.info(messageSource.getMessage("field.required", new String[] { "is" }, Locale.US));
	}
}

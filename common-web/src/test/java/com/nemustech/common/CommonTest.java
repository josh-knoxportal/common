package com.nemustech.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.nemustech.web.util.WebApplicationContextUtil;

public abstract class CommonTest {
	protected Log log = LogFactory.getLog(getClass());

	@Autowired
	protected ApplicationContext applicationContext;

	@Test
	public void t00() throws Exception {
//		WebApplicationContextUtil.printBeans(applicationContext, true);
	}
}
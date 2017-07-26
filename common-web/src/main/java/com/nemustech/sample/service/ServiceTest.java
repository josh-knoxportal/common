package com.nemustech.sample.service;

import org.springframework.scheduling.annotation.Scheduled;

import com.nemustech.common.util.StringUtil;

public class ServiceTest {
//	@Autowired
//	protected GenericApplicationContext context;

	public void t01() throws Exception {
//		ConfigurableListableBeanFactory factory = context.getBeanFactory();
//		factory.registerSingleton("serviceTest", this);
//		Thread.sleep(1000 * 6000);
	}

	@Scheduled(cron = "*/3 * * * * *")
	public void test01() throws Exception {
		System.out.println(StringUtil.getDate());
	}
}
package com.nemustech.web;

import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nemustech.common.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScheduleTest {
	protected Log log = LogFactory.getLog(getClass());

//	@Autowired
//	protected GenericApplicationContext context;

	@Autowired
	protected ThreadPoolTaskScheduler scheduler;

	@Test
	public void t01() throws Exception {
//		ConfigurableListableBeanFactory factory = context.getBeanFactory();
//		factory.registerSingleton("serviceTest", this);

		Runnable runnable = new ScheduledMethodRunnable(this, "run");// factory.getBean("serviceTest"), "run");

		log.info("------------------------------------------------------------");
		CronTrigger trigger = new CronTrigger("*/1 * * * * *");
		ScheduledFuture<?> future = scheduler.schedule(runnable, trigger);

		Thread.sleep(1000 * 5);

		log.info("------------------------------------------------------------");
		future.cancel(false);
		trigger = new CronTrigger("*/2 * * * * *");
		future = scheduler.schedule(runnable, trigger);

		Thread.sleep(1000 * 7);

		log.info("------------------------------------------------------------");
		future.cancel(false);
		trigger = new CronTrigger("*/3 * * * * *");
		future = scheduler.schedule(runnable, trigger);

		Thread.sleep(1000 * 10);

		log.info("------------------------------------------------------------");
		scheduler.shutdown();
	}

//	@Scheduled(cron = "*/3 * * * * *")
	public void run() throws Exception {
		log.info(StringUtil.getDateTime());
	}
}
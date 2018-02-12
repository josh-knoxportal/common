package com.nemustech.web;

import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
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
import org.springframework.test.context.web.WebAppConfiguration;

import com.nemustech.common.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config-spring.xml")
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScheduleTest {
	protected Log log = LogFactory.getLog(getClass());

	@Autowired
	protected GenericApplicationContext context;

	@Autowired
	protected ThreadPoolTaskScheduler scheduler;

//	@Test
	public void t01() throws Exception {
		ConfigurableListableBeanFactory factory = context.getBeanFactory();
		factory.registerSingleton("serviceTest", this);

		Runnable runnable = new ScheduledMethodRunnable(factory.getBean("serviceTest"), "run");

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

	/**
	 * <pre>
	 * <cron 표현식>
	 *
	 * 1. 초 분 시 일 월 요일
	 * : 각 시간 단위 값은 공백으로 구분되며, 다음의 표현식을 통해서 시간 간격, 주기 등을 표현할 수 있다.
	 * - * : 전체 값을 의미 .
	 * - 특정 값 : 해당 시간을 정확하게 지정. 예) "0", "10", "20"
	 * - 값1-값2 : 값1 부터 값2 사이를 표현 예) "0-1 0"
	 * - 값1,값2,값3 : 콤마로 구분하여 특정 값 목록을 지정 . 예) "O,1 5,30"
	 * - 범위/숫자 : 범위에 속한 값 중 숫자 만큼 간격으로 값 목록을 지정
	 *   예를 들어, "0-23/2"는 0부터 23까지 2간격으로 값을 설정한다.
	 *   즉, 0, 2, 4, 6, ~ ,20, 22의 값을 표현한다. * 을 사용해서 * /2와 같이 표현할수도 있다
	 *
	 * 2. 값의 범위
	 * - 초 : 0 ~ 59
	 * - 분 : 0 ~ 59
	 * - 시 : 0 ~ 23
	 * - 일 : 1 ~ 31
	 * - 월 : 1 ~ 12
	 * - 요일 : 0 - 7 (0 또는 7은 일요일)
	 *
	 * 3. 예
	 * - 0 0 * * * * : 매일 매시 정각
	 * - * /10 * * * * * : 0, 10, 20, 30, 40, 50초
	 * - 0 0 8-10 * * * : 매일 8시, 9시, 10 정각
	 * - 0 0/30 8-10 * * * : 매일 8시, 8시 30분, 9시, 9시 30분, 10시
	 * - 0 0 9-18 * * 1-5 : 매주 월요일부터 금요일의 9시부터 오후 6시까지 매시
	 * </pre>
	 *
	 * @throws Exception
	 */
//	@Scheduled(cron = "*/3 * * * * *")
	public void run() throws Exception {
		log.info(StringUtil.getDateTime());
	}
}
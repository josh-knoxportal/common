package org.oh.agent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

public class AgentApplication {
	protected static Log log = LogFactory.getLog(AgentApplication.class);

	protected JoranConfigurator configurator = null;
	protected Mybatis mybatis = null;
	protected AbstractApplicationContext context = null;

	public AgentApplication() {
		try {
			init();
		} catch (AgentException e) {
			log.error("Agent 초기화 중 에러 발생", e);
		}
	}

	protected void init() throws AgentException {
		try {
			LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
			context.reset();

			configurator = new JoranConfigurator();
			configurator.setContext(context);
			configurator.doConfigure("conf/logback.xml");
		} catch (Exception e) {
			throw new AgentException("Agent_001", "Log 설정파일 로드 중 오류 발생", e);
		}

		mybatis = new Mybatis();
	}

	protected void run() throws AgentException {
		try {
			context = new FileSystemXmlApplicationContext("conf/spring-config.xml");
		} catch (AgentException e) {
			log.error("Agent 실행 중 에러 발생", e);
		}

		context.registerShutdownHook();
	}

	public static void main(String[] args) {
		AgentApplication main = new AgentApplication();
		main.run();
	}
}

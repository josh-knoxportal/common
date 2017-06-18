package com.nemustech.web.common;

import javax.servlet.ServletContextEvent;

import ch.qos.logback.ext.spring.web.LogbackConfigListener;

/**
 * tomcat zombie 예방
 * 
 * @author skoh
 * @see ch.qos.logback.ext.spring.web.WebLogbackConfigurer
 */
public class LogbackConfigListener2 extends LogbackConfigListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		WebLogbackConfigurer2.initLogging(event.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		WebLogbackConfigurer2.shutdownLogging(event.getServletContext());
	}
}
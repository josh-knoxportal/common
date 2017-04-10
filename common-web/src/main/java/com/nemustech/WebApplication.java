package com.nemustech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import com.nemustech.web.util.WebApplicationContextUtil;

//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class,
		JmxAutoConfiguration.class })
@ImportResource({ "classpath:config-spring.xml", "classpath:config-spring_jmx.xml" })
public class WebApplication {
	@Autowired
	protected static ApplicationContext applicationContext;

	public WebApplication() {
	}

	public WebApplication(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(WebApplication.class, args);
//		WebApplicationContextUtil.printBeans(applicationContext, true);
	}

	@Bean
	protected ApplicationRunner init() {
		System.out.println("init()");

		return null;
	}

	public static void main(String[] args) {
		new WebApplication(args);
	}
}

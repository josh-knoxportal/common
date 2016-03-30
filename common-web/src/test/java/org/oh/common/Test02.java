package org.oh.common;

import org.oh.common.util.LogUtil;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test02 {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("config-spring.xml");
		String[] beanNames = context.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			try {
				LogUtil.writeLog(beanName + "\t");
				LogUtil.writeLog(context.getBean(beanName).getClass().getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		context.close();
	}

}

package org.oh.web.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

public abstract class WebApplicationContextUtil extends WebApplicationContextUtils {
	public static ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static Locale getLocale() {
		return getRequest().getLocale();
	}

	public static WebApplicationContext getApplicationContext() {
		return RequestContextUtils.getWebApplicationContext(getRequest());
	}

	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public static void printBeans(ListableBeanFactory beanFactory) {
		printBeans(beanFactory, false);
	}

	public static void printBeans(ListableBeanFactory beanFactory, boolean sort) {
		List<String> list = Arrays.asList(beanFactory.getBeanDefinitionNames());
		System.out.println("Total beans size = " + list.size());

		if (sort)
			Collections.sort(list);

		int i = 1;
		for (String name : list) {
			if ("sessionContext".equals(name))
				continue;

			System.out.println(String.format("%-4.4s %-100.100s %s", i++, name,
					((beanFactory.getBean(name) == null) ? "" : beanFactory.getBean(name).getClass().getName())));
		}
	}
}

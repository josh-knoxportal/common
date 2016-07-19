package org.oh.web.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.oh.common.util.LogUtil;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

public abstract class WebApplicationContextUtil extends WebApplicationContextUtils {
	public static ServletContext getServletContext() {
		HttpSession session = getSession();

		return (session == null) ? null : session.getServletContext();
	}

	public static HttpSession getSession() {
		HttpServletRequest request = getRequest();

		return (request == null) ? null : request.getSession();
	}

	public static Locale getLocale() {
		HttpServletRequest request = getRequest();

		return (request == null) ? null : request.getLocale();
	}

	public static WebApplicationContext getApplicationContext() {
		HttpServletRequest request = getRequest();

		return (request == null) ? null : RequestContextUtils.findWebApplicationContext(request);
	}

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();

		return (requestAttributes == null) ? null : requestAttributes.getRequest();
	}

	public static void printBeans(ListableBeanFactory beanFactory) {
		printBeans(beanFactory, false);
	}

	public static void printBeans(ListableBeanFactory beanFactory, boolean sort) {
		List<String> list = Arrays.asList(beanFactory.getBeanDefinitionNames());
		LogUtil.writeLog("--------------------------------------------------------------------------------");
		LogUtil.writeLog("Total beans size = " + list.size());

		if (sort)
			Collections.sort(list);

		int i = 1;
		for (String name : list) {
			if ("sessionContext".equals(name))
				continue;

			LogUtil.writeLog(String.format("%-4.4s %-100.100s %s", i++, name,
					((beanFactory.getBean(name) == null) ? "" : beanFactory.getBean(name).getClass().getName())));
		}
		LogUtil.writeLog("--------------------------------------------------------------------------------");
	}
}

package com.nemustech.web.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.nemustech.common.util.LogUtil;

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

		return (request == null) ? null : RequestContextUtils.getWebApplicationContext(request);
//		return (request == null) ? null : RequestContextUtils.findWebApplicationContext(request);
	}

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();

		return (requestAttributes == null) ? null : requestAttributes.getRequest();
	}

	///////////////////////////////////////////////////////////////////////////

	public static BeanFactory getBeanFactory() {
		WebApplicationContext webApplicationContext = getApplicationContext();
		if (webApplicationContext == null)
			return null;

		if (isConfigurableWebApplicationContext(webApplicationContext)) {
			@SuppressWarnings("resource")
			ConfigurableWebApplicationContext configurableWebApplicationContext = (ConfigurableWebApplicationContext) webApplicationContext;

			return configurableWebApplicationContext.getBeanFactory();
		} else {
			return webApplicationContext.getParentBeanFactory();
		}
	}

	/**
	 * ConfigurableWebApplicationContext 인지를 확인
	 * 
	 * @param webApplicationContext
	 * @return
	 */
	public static boolean isConfigurableWebApplicationContext(WebApplicationContext webApplicationContext) {
		if (webApplicationContext != null && webApplicationContext instanceof ConfigurableWebApplicationContext) {
			return true;
		} else {
			return false;
		}
	}

	///////////////////////////////////////////////////////////////////////////

	public static void printBeans() {
		printBeans(false);
	}

	public static void printBeans(boolean sort) {
		BeanFactory beanFactory = getBeanFactory();
		if (beanFactory != null && beanFactory instanceof ListableBeanFactory) {
			printBeans((ListableBeanFactory) beanFactory, sort);
		}
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

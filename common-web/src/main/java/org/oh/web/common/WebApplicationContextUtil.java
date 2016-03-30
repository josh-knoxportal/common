package org.oh.web.common;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
}

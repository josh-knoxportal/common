package com.nemustech.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 크로스 도메인 문제를 해결하기 위한 필터
 * 
 * @author skoh
 */
public class CORSFilter implements Filter {
	protected Log log = LogFactory.getLog(getClass());
	protected String allowHeaders;

	@Override
	public void init(FilterConfig config) throws ServletException {
		allowHeaders = config.getInitParameter("allowHeaders");
		log.info("allowHeaders: " + allowHeaders);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader("Access-Control-Allow-Origin", "*");
		res.addHeader("Access-Control-Allow-Methods", "*");
		res.addHeader("Access-Control-Allow-Headers", allowHeaders);

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
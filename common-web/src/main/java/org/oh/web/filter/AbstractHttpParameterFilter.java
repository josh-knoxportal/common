package org.oh.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oh.common.util.LogUtil;
import org.oh.common.util.Utils;
import org.springframework.util.StringUtils;

/**
 * HTTP 입력 파라미터 값을 필터링한다.
 * 
 * @author skoh
 */
public abstract class AbstractHttpParameterFilter implements Filter {
	protected FilterConfig config;

	/**
	 * 필터링에서 제외할 URL를 열거합니다.
	 * 
	 * <pre>
	 * 예) /test/test.do
	 * </pre>
	 */
	protected String[] excludeUrls;

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		excludeUrls = StringUtils.tokenizeToStringArray(config.getInitParameter("excludeUrls"), ",; \t\n");
		LogUtil.writeLog("excludeUrls: " + Utils.toString(excludeUrls), getClass());
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (excludeUrl(request)) {
			chain.doFilter(request, response);
		} else {
			chain.doFilter(getHttpServletRequestWrapper((HttpServletRequest) request, (HttpServletResponse) response),
					response);
		}
	}

	protected boolean excludeUrl(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String path = httpServletRequest.getContextPath();
		String uri = httpServletRequest.getRequestURI();

		for (String excludeUrl : excludeUrls) {
			if (uri.startsWith(path + excludeUrl)) {
				return true;
			}
		}

		return false;
	}

	public void destroy() {
	}

	/**
	 * 커스터마이징한 HttpServletRequestWrapper를 적용한다.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public abstract HttpServletRequest getHttpServletRequestWrapper(HttpServletRequest request,
			HttpServletResponse response);
}

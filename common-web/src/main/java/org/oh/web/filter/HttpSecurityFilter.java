package org.oh.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.common.util.Utils;
import org.oh.web.common.HttpSecurityRequestWrapper;

/**
 * HTTP 입력 파라미터 값에 포함된 보안 위배 문자열을 필터링한다.
 * 
 * @author skoh
 */
public class HttpSecurityFilter extends AbstractHttpParameterFilter {
	private final Log log = LogFactory.getLog(HttpSecurityFilter.class);

	@Override
	public HttpServletRequest getHttpServletRequestWrapper(HttpServletRequest request, HttpServletResponse response) {
		log.debug(Utils.toString(request));

		return new HttpSecurityRequestWrapper(request, response);
	}
}

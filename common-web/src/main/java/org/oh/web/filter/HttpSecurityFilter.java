package org.oh.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.common.util.JsonUtil2;
import org.oh.web.common.HttpSecurityRequestWrapper;

/**
 * HTTP 입력 파라미터 값에 포함된 보안 위배 문자열을 필터링한다.
 * 
 * @author skoh
 */
public class HttpSecurityFilter extends AbstractHttpParameterFilter {
	protected Log log = LogFactory.getLog(getClass());

	@Override
	public HttpServletRequest getHttpServletRequestWrapper(HttpServletRequest request, HttpServletResponse response) {
		log.debug(JsonUtil2.toString(request));

		return new HttpSecurityRequestWrapper(request, response);
	}
}

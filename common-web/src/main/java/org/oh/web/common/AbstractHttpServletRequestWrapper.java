package org.oh.web.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * HTTP 입력 파라미터 값을 체크한다.
 * 
 * @author skoh
 */
public abstract class AbstractHttpServletRequestWrapper extends HttpServletRequestWrapper {
	public AbstractHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}

		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				values[i] = checkValue(values[i]);
			}
		}

		return values;
	}

	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}

		return checkValue(value);
	}

	/**
	 * 파라미터 값을 체크한다.
	 * 
	 * @param value
	 * @return
	 */
	public abstract String checkValue(String value);
}

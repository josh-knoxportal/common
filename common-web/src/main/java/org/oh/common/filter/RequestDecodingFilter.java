package org.oh.common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * GET 방식에서 파라미터 디코딩 필터. (한글 깨짐 방지)
 * 
 * @author skoh
 */
public class RequestDecodingFilter extends OncePerRequestFilter {
	protected Log log = LogFactory.getLog(getClass());

	/**
	 * 엔코딩 방식
	 */
	protected String encoding;

	/**
	 * 디코딩 방식
	 */
	protected String decoding;

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setDecoding(String decoding) {
		this.decoding = decoding;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		filterChain.doFilter(new HttpServletRequestDecode(encoding, decoding, request), response);
	}

	class HttpServletRequestDecode extends HttpServletRequestWrapper {
		protected String encoding;
		protected String decoding;

		public HttpServletRequestDecode(String encoding, String decoding, HttpServletRequest request) {
			super(request);

			this.encoding = encoding;
			this.decoding = decoding;
		}

		@Override
		public String getParameter(String name) {
			String parameter = super.getParameter(name);
			if (parameter != null && encoding != null && decoding != null && "get".equalsIgnoreCase(getMethod())) {
				try {
					return new String(parameter.getBytes(encoding), decoding);
				} catch (Exception e) {
					log.error("Error decoding the request parameter.", e);
				}
			}

			return parameter;
		}

		@Override
		public String[] getParameterValues(String name) {
			String[] parameters = super.getParameterValues(name);
			if (parameters != null && encoding != null && decoding != null && "get".equalsIgnoreCase(getMethod())) {
				try {
					List<String> parameterList = new ArrayList<String>();
					for (String parameter : parameters) {
						if (parameter != null)
							parameterList.add(new String(parameter.getBytes(encoding), decoding));
						else
							parameterList.add(parameter);
					}

					return parameterList.toArray(new String[parameterList.size()]);
				} catch (Exception e) {
					log.error("Error decoding the request parameter.", e);
				}
			}

			return parameters;
		}
	}
}
package org.oh.web.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oh.common.util.Utils;
import org.oh.common.util.WebUtil;
import org.oh.web.exception.WebException;

public class HttpSecurityRequestWrapper extends AbstractHttpServletRequestWrapper {
	/**
	 * 파일 경로 필터링 문자열(유지보수 대상)
	 */
	public static final String[] checkValues = { "./", "../", ".\\", "..\\" };

	protected HttpServletResponse response;

	public HttpSecurityRequestWrapper(HttpServletRequest request, HttpServletResponse response) {
		super(request);
		this.response = response;
	}

	@Override
	public String checkValue(String value) {
		if (!Utils.isValidate(value)) {
			return value;
		}

		value = checkValue1(value);
		value = checkValue2(value);

		return value;
	}

	/**
	 * HTML 태그를 필터링한다.
	 * 
	 * <pre>
	 * : HTMLTagFilter의 경우는 파라미터에 대하여 XSS 오류 방지를 위한 변환을 처리합니다.
	 * 우선 HTMLTagFIlter의 경우는 JSP의 <c:out /> 등을 사용하지 못하는 특수한 상황에서 사용하시면 됩니다.(<c:out />의 경우 뷰단에서 데이터 출력시 XSS 방지 처리가 됨)
	 * 부득히 HTMLTagFilter를 사용하셔야 하는 경우는 web.xml 상에 다음과 같이 CertProcessFilter(GPKI 인증 처리 오류 해결) 등록해 주시면 됩니다.
	 * </pre>
	 * 
	 * @param value
	 * @return
	 */
	protected String checkValue1(String value) {
		// JSON 데이타는 예외
		if (value.startsWith("{") && value.endsWith("}")) {
			return value;
		}
		if (value.startsWith("[{") && value.endsWith("}]")) {
			return value;
		}

		// XML 데이타는 예외
		if (value.startsWith("<?xml")) {
			return value;
		}

		StringBuffer strBuff = new StringBuffer();
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			// HTML 태그 필터링 문자열(유지보수 대상)
			switch (c) {
				case '<':
					strBuff.append("&lt;");
					break;
				case '>':
					strBuff.append("&gt;");
					break;
				case '&':
					strBuff.append("&amp;");
					break;
				case '"':
					strBuff.append("&quot;");
					break;
				case '\'':
					strBuff.append("&apos;");
					break;
				default:
					strBuff.append(c);
					break;
			}
		}

		return strBuff.toString();
	}

	/**
	 * 파일 경로 문자열을 필터링한다.
	 * 
	 * @param value
	 * @return
	 */
	protected String checkValue2(String value) {
		for (String checkValue : checkValues) {
			if (value.indexOf(checkValue) >= 0) {
				String message = "보안상 사용할 수 없는 문자가 포함되어 있습니다.";
				WebUtil.printAlert(message, response);

				throw new WebException("ERR001", message);
			}
		}

		return value;
	}
}
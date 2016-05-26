package org.oh.web.util;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oh.common.util.LogUtil;
import org.springframework.web.util.WebUtils;

/**
 * 웹 관련 유틸리티 클래스.<br/>
 * - org.springframework.web.util.WebUtils 클래스를 상속받음.
 *
 * @see <a href=http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/util/WebUtils.html</a>
 */
public abstract class WebUtil extends WebUtils {
	/**
	 * 파라미터를 Map를 반환한다.
	 * 
	 * @param request ServletRequest
	 * 
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getParametersStartingWith(ServletRequest request) {
		return getParametersStartingWith(request, null);
	}

	public static void printAlert(String message, HttpServletResponse response) {
		printAlert(message, true, response);
	}

	/**
	 * alert 메세지를 출력한다.
	 * 
	 * @param message
	 * @param back
	 * @param response
	 */
	public static void printAlert(String message, boolean back, HttpServletResponse response) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script type='text/javascript'>");
		sb.append("alert('" + message + "');");
		if (back)
			sb.append("history.back();");
		sb.append("</script>");

		printHtml(sb.toString(), response);
	}

	public static void printHtml(String html, HttpServletResponse response) {
		printHtml(html, "UTF-8", response);
	}

	/**
	 * 화면에 HTML 메세지를 출력한다.
	 * 
	 * @param message
	 * @param charset
	 * @param response
	 */
	public static void printHtml(String html, String charset, HttpServletResponse response) {
		try {
			response.setCharacterEncoding(charset);
			response.setContentType("text/html;charset=" + charset);

			response.getOutputStream().write(html.getBytes(charset));
			response.flushBuffer();
		} catch (Exception e) {
			LogUtil.writeLog(e.getMessage(), e, WebUtil.class);
		}
	}
}

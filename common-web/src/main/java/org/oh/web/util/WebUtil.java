package org.oh.web.util;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.oh.common.util.JsonUtil2;
import org.oh.common.util.LogUtil;
import org.oh.web.common.Header;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
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

	public static String toJsonPretty(Object... pojos) {
		return toJson(pojos, true);
	}

	public static String toJson(Object... pojos) {
		return toJson(pojos, false);
	}

	public static String toJson(Object[] pojos, boolean prettyPrint) {
		if (pojos.length == 1) {
			return toJson(pojos[0], prettyPrint);
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < pojos.length; i++) {
				if (i == 0) {
					sb.append("[ ");
				} else {
					sb.append(", ");
				}

				sb.append(toJson(pojos[i], prettyPrint));

				if (i == pojos.length - 1) {
					sb.append(" ]");
				}
			}
			return sb.toString();
		}
	}

	public static String toJson(Object pojo, boolean prettyPrint) {
		if (pojo instanceof HttpServletRequest) {
			return toJsonRequest((HttpServletRequest) pojo, prettyPrint);
		} else if (pojo instanceof HttpSession) {
			return toJsonSession((HttpSession) pojo, prettyPrint);
		} else {
			return JsonUtil2.toString(pojo, prettyPrint);
//			return "{" + ((prettyPrint) ? System.lineSeparator() + "  " : "") + "\"warn\":\"Not suported "
//					+ ReflectionToStringBuilder.toString(pojo) + "\"}";
		}
	}

	public static String toJsonRequest(HttpServletRequest request, boolean prettyPrint) {
		// parameters
		String client = request.getRemoteAddr();
		String method = request.getMethod();
		Map<String, String[]> parameter = request.getParameterMap();

		// headers
		Map<String, String> header = new LinkedHashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			header.put(key, value);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("{\"request\": {");
//		sb.append("\"uri\": \"" + request.getRequestURI() + "\"");
//		sb.append(", \"method\": \"" + method + "\"");
		sb.append("\"parameters\": " + JsonUtil2.toString(parameter));
		sb.append(", \"headers\": " + JsonUtil2.toString(header));
		sb.append(", " + toJsonSession(request.getSession(), prettyPrint, true));
		sb.append(", \"client\": \"" + client + "\"");
		sb.append("}}");

		return JsonUtil2.toString(sb.toString(), prettyPrint);
	}

	public static String toJsonSession(HttpSession session, boolean prettyPrint) {
		return toJsonSession(session, prettyPrint, false);
	}

	protected static String toJsonSession(HttpSession session, boolean prettyPrint, boolean embeded) {
		Map<String, Object> attrMap = new LinkedHashMap<String, Object>();
		Enumeration<String> attrs = session.getAttributeNames();
		while (attrs.hasMoreElements()) {
			String name = (String) attrs.nextElement();
			attrMap.put(name, session.getAttribute(name));
		}

		StringBuilder sb = new StringBuilder();
		sb.append((embeded ? "" : "{") + "\"session\": {");
		sb.append("\"attributes\": " + JsonUtil2.readValue(attrMap));
		sb.append("}" + (embeded ? "" : "}"));

		return embeded ? sb.toString() : JsonUtil2.toString(sb.toString(), prettyPrint);
	}

	public static void main(String[] args) {
		Header model = new Header();
		model.setError_message("test");
		System.out.println(toJson(model, model));
		System.out.println(toJsonPretty(model, model));

		MockHttpServletRequest request = new MockHttpServletRequest();
		System.out.println(toJson(request, request));
		System.out.println(toJsonPretty(request, request));

		MockHttpSession session = new MockHttpSession();
		System.out.println(toJson(session, session));
		System.out.println(toJsonPretty(session, session));
	}
}

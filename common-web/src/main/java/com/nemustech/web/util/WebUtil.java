package com.nemustech.web.util;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.util.WebUtils;

import com.nemustech.common.model.Header;
import com.nemustech.common.util.JsonUtil2;
import com.nemustech.common.util.LogUtil;

/**
 * 웹 관련 유틸리티 클래스.<br/>
 * - org.springframework.web.util.WebUtils 클래스를 상속받음.
 *
 * @see <a href=http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/util/WebUtils.html</a>
 */
public abstract class WebUtil extends WebUtils {
	/**
	 * HttpMethod객체를 반환한다.
	 * 
	 * @param method HTTP method
	 * @return
	 */
	public static HttpMethod getHttpMethod(String method) {
		if (method == null)
			method = "GET";
		else
			method = method.toUpperCase();

		return HttpMethod.resolve(method);
	}

	/**
	 * 파라미터를 Map를 반환한다.
	 * 
	 * @param request ServletRequest
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

	public static String toStringJson(Object... objs) {
		return toStringJson(objs, false);
	}

	public static String toStringJsonPretty(Object... objs) {
		return toStringJson(objs, true);
	}

	public static String toStringJson(Object[] objs, boolean prettyPrint) {
		if (objs.length == 1) {
			return toStringJson(objs[0], prettyPrint);
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < objs.length; i++) {
				if (i == 0) {
					sb.append("[ ");
				} else {
					sb.append(", ");
				}

				sb.append(toStringJson(objs[i], prettyPrint));

				if (i == objs.length - 1) {
					sb.append(" ]");
				}
			}
			return sb.toString();
		}
	}

	/**
	 * object 을 JSON 포맷으로 구한다.
	 * 
	 * @param obj
	 * @param prettyPrint
	 * @return
	 */
	public static String toStringJson(Object obj, boolean prettyPrint) {
		if (obj instanceof HttpServletRequest) {
			return toJsonRequest((HttpServletRequest) obj, prettyPrint);
		} else if (obj instanceof HttpSession) {
			return toJsonSession((HttpSession) obj, prettyPrint);
		} else {
			return JsonUtil2.toString(obj, prettyPrint);
//			return "{" + ((prettyPrint) ? Utils.LINE_SEPARATOR + "  " : "") + "\"warn\":\"Not suported "
//					+ StringUtil.toString(obj) + "\"}";
		}
	}

	/**
	 * request 를 JSON 포맷으로 구한다.
	 * 
	 * @param request
	 * @param prettyPrint
	 * @return
	 */
	public static String toJsonRequest(HttpServletRequest request, boolean prettyPrint) {
		// parameters
		String client = request.getRemoteAddr();
		String method = request.getMethod();

		// headers
		Map<String, String> header = new LinkedHashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			header.put(key, value);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("{\"request\":{");
//		sb.append("\"uri\":\"" + request.getRequestURI() + "\"");
//		sb.append(", \"method\":\"" + method + "\"");
//		sb.append(toJsonParameter(request, prettyPrint, true));
		sb.append("\"headers\":" + JsonUtil2.toString(header));
		sb.append(", " + toJsonSession(request.getSession(), prettyPrint, true));
		sb.append(", \"client\":\"" + client + "\"");
		sb.append("}}");

		return JsonUtil2.toString(sb.toString(), prettyPrint);
	}

	public static String toJsonSession() {
		return toJsonSession(null, false);
	}

	public static String toJsonSession(HttpSession session, boolean prettyPrint) {
		HttpServletRequest request = WebApplicationContextUtil.getRequest();
		session = (request != null && session == null) ? request.getSession() : session;
		if (session == null)
			return "";

		return toJsonSession(session, prettyPrint, false);
	}

	/**
	 * session 을 JSON 포맷으로 구한다.
	 * 
	 * @param session
	 * @param prettyPrint
	 * @param embeded
	 * @return
	 */
	protected static String toJsonSession(HttpSession session, boolean prettyPrint, boolean embeded) {
		Map<String, Object> attrMap = new LinkedHashMap<String, Object>();
		Enumeration<String> attrs = session.getAttributeNames();
		while (attrs.hasMoreElements()) {
			String name = (String) attrs.nextElement();
			attrMap.put(name, session.getAttribute(name));
		}

		StringBuilder sb = new StringBuilder();
		sb.append((embeded ? "" : "{") + "\"session\":{");
		sb.append("\"attributes\":" + JsonUtil2.readValue(attrMap));
		sb.append("}" + (embeded ? "" : "}"));

		return embeded ? sb.toString() : JsonUtil2.toString(sb.toString(), prettyPrint);
	}

	public static String toJsonParameter() {
		return toJsonParameter(null, false);
	}

	public static String toJsonParameter(HttpServletRequest request, boolean prettyPrint) {
		return toJsonParameter(request, prettyPrint, false);
	}

	/**
	 * parameters 를 JSON 포맷으로 구한다.
	 * 
	 * @param request
	 * @param prettyPrint
	 * @param embeded false 이면 {...} 로 감싼다.
	 * @return
	 */
	protected static String toJsonParameter(HttpServletRequest request, boolean prettyPrint, boolean embeded) {
		request = (request == null) ? WebApplicationContextUtil.getRequest() : request;
		if (request == null)
			return "";

		StringBuilder sb = new StringBuilder();
		sb.append(embeded ? "" : "{");
		sb.append("\"parameters\":" + JsonUtil2.toString(request.getParameterMap()));
		sb.append("}" + (embeded ? "" : "}"));

		return embeded ? sb.toString() : JsonUtil2.toString(sb.toString(), prettyPrint);
	}

	public static void main(String[] args) {
		Header model = new Header();
		model.setError_message("test");
		System.out.println(toStringJson(model, model));
		System.out.println(toStringJsonPretty(model, model));
		System.out.println();

		MockHttpServletRequest request = new MockHttpServletRequest();
		System.out.println(toStringJson(request, request));
		System.out.println(toStringJsonPretty(request, request));
		System.out.println();

		MockHttpSession session = new MockHttpSession();
		System.out.println(toStringJson(session, session));
		System.out.println(toStringJsonPretty(session, session));
	}
}

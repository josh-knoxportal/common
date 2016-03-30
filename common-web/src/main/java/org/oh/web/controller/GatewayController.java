package org.oh.web.controller;

import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.common.util.PropertyUtils;
import org.oh.web.exception.WebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Controller
public class GatewayController {
	public static final String BEGIN_STRING = "/v";
	public static final String END_STRING = ".do";

	public static final String API_PATH = "api_path";
	public static final String API_KEY = "api_key";
	public static final String API_KEY_POSTFIX = ".url";

//	protected static final Gson GSON = new Gson();

	private static final Log log = LogFactory.getLog(GatewayController.class);

	@Autowired
	protected RestTemplate restTemplate;

	/**
	 * HttpMethod객체를 반환한다.
	 * 
	 * @param method HTTP method
	 * 
	 * @return
	 */
	public static HttpMethod getHttpMethod(String method) {
		if (method == null)
			return HttpMethod.GET;

		method = method.toUpperCase();
		if ("GET".equals(method))
			return HttpMethod.GET;
		else if ("POST".equals(method))
			return HttpMethod.POST;
		else if ("PUT".equals(method))
			return HttpMethod.PUT;
		else if ("DELETE".equals(method))
			return HttpMethod.DELETE;
		else
			return HttpMethod.GET;
	}

	/**
	 * API path을 구한다.
	 * 
	 * @param queryString 예) api_path=/v1/mms/rssi.do&sort_column=type%20desc,%20signal
	 * 
	 * @return 예) /v1/mms/rssi.do?sort_column=type desc, signal
	 */
	public static String getAPIPath(String queryString) throws Exception {
		queryString = queryString.replace(END_STRING + "&", END_STRING + "?");

		int beginIndex = queryString.indexOf(API_PATH);
		beginIndex = (beginIndex < 0) ? 0 : beginIndex + API_PATH.length() + 1;

		queryString = queryString.substring(beginIndex, queryString.length());

		return URLDecoder.decode(queryString, "UTF-8");
	}

	/**
	 * API key을 구한다.
	 * 
	 * @param api_path 예) /v1/mms/rssi.do?sort_column=type desc, signal
	 * 
	 * @return 예) mms.url
	 */
	public static String getAPIKey(String api_path) {
		int beginIndex = getBeginIndex(api_path, 1);

		int endIndex = api_path.indexOf("/", beginIndex);
		endIndex = (endIndex < 0) ? api_path.length() : endIndex;

		return api_path.substring(beginIndex, endIndex) + API_KEY_POSTFIX;
	}

	/**
	 * API URL을 구한다.
	 * 
	 * @param api_url 예) http://localhost:8080/v1/mms/
	 * @param api_path 예) /v1/mms/rssi.do?sort_column=type desc, signal
	 * 
	 * @return 예) http://localhost:8080/v1/mms/rssi.do?sort_column=type desc, signal
	 * 
	 * @throws Exception
	 */
	public static String getAPIUrl(String api_url, String api_path) {
		StringBuilder apiUrl = new StringBuilder(api_url);

		int beginIndex = getBeginIndex(api_path, 2);
		apiUrl.append(api_path.substring(beginIndex));

		return apiUrl.toString();
	}

	public static int getBeginIndex(String api_path, int n) {
		return getBeginIndex(api_path, "/", n);
	}

	/**
	 * 버전 이후에 특정 단어의 n번째 (위치 + 1)를 찾는다.
	 * 
	 * @param api_path 예) /v1/mms/rssi.do?sort_column=type desc, signal
	 * @param str 예) /
	 * @param n 예) 2
	 * 
	 * @return 예) 8
	 */
	public static int getBeginIndex(String api_path, String str, int n) {
		int beginIndex = api_path.indexOf(BEGIN_STRING);
		beginIndex = (beginIndex < 0) ? 0 : beginIndex + 1;

		for (int i = 0; i < n; i++) {
			beginIndex = api_path.indexOf(str, beginIndex);
			beginIndex = (beginIndex < 0) ? 0 : beginIndex + 1;
		}

		return beginIndex;
	}

	/**
	 * 타겟 URL로 http header와 body를 그대로 전송하고 그 결과를 반환한다.
	 * (크로스 도메인 문제를 해결하기 위한 by pass 형태의 gateway)
	 * 
	 * @param header 필수값(api_path : 예) /v1/mms/rssi.do), 선택값(api_key : 예) mms.url)
	 * @param body
	 * @param request
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "gateway.do")
	public ResponseEntity<Object> gateway(@RequestHeader HttpHeaders header,
			@RequestBody(required = false) Map<String, Object> body, HttpServletRequest request,
			HttpServletResponse response) throws WebException {
		StringBuilder msg = new StringBuilder();
		msg.append("\n").append("gateway request.url:" + request.getRequestURI());
		msg.append("\n").append("gateway request.queryString:" + request.getQueryString());
		msg.append("\n").append("gateway request.parameter:" + request.getParameterMap());
		msg.append("\n").append("gateway request.method:" + request.getMethod());
//		msg.append("\n").append("gateway request.header:" + header);
		log.info(msg.toString());

		try {
			String queryString = request.getQueryString();
			Map<String, String[]> paramMap = request.getParameterMap();

//			String api_path = header.get(API_PATH).get(0);
//			String api_path = paramMap.get(API_PATH)[0];
			String api_path = getAPIPath(queryString);

//			List<String> api_keys = header.get(API_KEY);
//			String api_key = (api_keys != null && api_keys.size() > 0) ? api_keys.get(0) : getAPIKey(api_path);
			String[] api_keys = paramMap.get(API_KEY);
			String api_key = (api_keys != null && api_keys.length > 0) ? api_keys[0] : getAPIKey(api_path);

			String api_url = PropertyUtils.getInstance().getString(api_key);
			api_url = getAPIUrl(api_url, api_path);

			ResponseEntity<Object> responseEntity = callHttp(api_url, request.getMethod(), header, body);
//			if (api_url.indexOf("/cbms/") >= 0) {
//				responseEntity = new ResponseEntity<Object>(GSON.toJson(responseEntity.getBody()),
//						responseEntity.getStatusCode());
//			} else {
			responseEntity = new ResponseEntity<Object>(responseEntity.getBody(), responseEntity.getStatusCode());
//			}

//			response.setHeader("Content-Length", "");

			return responseEntity;
		} catch (Exception e) {
			throw new WebException(msg.toString(), e);
		}
	}

	/**
	 * HTTP 서버를 호출한다.
	 * 
	 * @param url
	 * @param method
	 * @param header
	 * @param body
	 * 
	 * @return
	 */
	protected ResponseEntity<Object> callHttp(String url, String method, HttpHeaders header, Map<String, Object> body)
			throws RestClientException {
		StringBuilder msg = new StringBuilder();
		msg.append("\n").append("callHttp request.url:" + url);
		msg.append("\n").append("callHttp request.method:" + method);
		msg.append("\n").append("callHttp request.header:" + header);
		msg.append("\n").append("callHttp request.body:" + body);
		log.info(msg.toString());

		try {
			ResponseEntity<Object> response = restTemplate.exchange(url, getHttpMethod(method),
					new HttpEntity<Map<String, Object>>(body, header), Object.class);
			log.info("response.headers: " + response.getHeaders());
			log.trace("response.body: " + response.getBody());

			return response;
		} catch (Exception e) {
			throw new RestClientException(msg.toString(), e);
		}
	}

	public static void main(String[] args) throws Exception {
		String queryString = "api_path=/v1/mms/rssi.do&sort_column=type%20desc,%20signal";
		System.out.println(queryString);

		String api_path = getAPIPath(queryString);
		System.out.println(api_path);

		System.out.println(getBeginIndex(api_path, 2));

		String api_key = getAPIKey(api_path);
		System.out.println(api_key);

		String api_url = "http://localhost:8080/v1/mms/";
		System.out.println(api_url);

		api_url = getAPIUrl(api_url, api_path);
		System.out.println(api_url);
	}
}

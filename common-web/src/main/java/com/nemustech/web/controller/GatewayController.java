package com.nemustech.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

//@Controller
public class GatewayController {
	public static final String PARAM_NAME = "api_uri";

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	protected RestTemplate restTemplate;

	/**
	 * HttpMethod객체를 반환한다.
	 * 
	 * @param method HTTP method
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
	 * API URI로 http header와 body를 그대로 전송하고 그 결과를 반환한다.
	 * 
	 * @param header
	 * @param body
	 * @param request
	 * @param response
	 * @return ResponseEntity<Object>
	 * @throws Exception
	 */
	@RequestMapping(value = "gateway.do")
	public ResponseEntity<Object> gateway(@RequestHeader(required = false) HttpHeaders header,
			@RequestBody(required = false) Map<String, Object> body, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, String[]> paramMap = request.getParameterMap();
			String api_url = paramMap.get(PARAM_NAME)[0];

			ResponseEntity<Object> responseEntity = callHttp(api_url, request.getMethod(), header, body);

			return new ResponseEntity<Object>(responseEntity.getBody(), responseEntity.getStatusCode());
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * HTTP 서버를 호출한다.
	 * 
	 * @param url
	 * @param method
	 * @param header
	 * @param body
	 * @return ResponseEntity<Object>
	 * @throws RestClientException
	 */
	protected ResponseEntity<Object> callHttp(String url, String method, HttpHeaders header, Map<String, Object> body)
			throws RestClientException {
		return restTemplate.exchange(url, getHttpMethod(method), new HttpEntity<Map<String, Object>>(body, header),
				Object.class);
	}
}

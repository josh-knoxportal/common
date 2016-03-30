package org.oh.adapter;

import java.net.URI;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.oh.adapter.exception.AdapterException;
import org.oh.adapter.http.IHttpMapper;

/**
 * HTTP Server 연동 Adapter
 * 
 * @since 1.0.2
 */
public interface HTTPAdapter {
	/**
	 * HTTP Client를 얻는다.
	 * 
	 * @return Apache http client Instance
	 */
	public HttpClient getHttpClient();

	public String getDomain();

	/**
	 * HTTP Server에 GET방식으로 요청을 한다.
	 * 
	 * @param uri 요청할 서버의 URI 문자열
	 * @return HttpResponse
	 * @throws AdapterException 예외
	 */
	public HttpResponse doGet(String uri) throws AdapterException;

	/**
	 * HTTP Server에 GET방식으로 요청을 한다.
	 * 
	 * @param uri 요청할 서버의 URI
	 * @return HttpResponse
	 * @throws AdapterException 예외
	 */
	public HttpResponse doGet(URI uri) throws AdapterException;

	/**
	 * HTTP Server에 GET방식으로 요청을 한다.
	 * 
	 * @param uri 요청할 서버의 URI 문자열
	 * @param paramObj 요청 데이터
	 * @param mapper 요청, 응답 데이터 매핑 처리
	 * @return 응답 처리 객체
	 * @throws AdapterException 예외
	 */
	public <T> T doGet(String uri, Object paramObj, IHttpMapper<T> mapper) throws AdapterException;

	/**
	 * HTTP Server에 GET방식으로 요청을 한다.
	 * 
	 * @param uri 요청할 서버의 URI
	 * @param paramObj 요청 데이터
	 * @param mapper 요청, 응답 데이터 매핑 처리
	 * @return 응답 처리 객체
	 * @throws AdapterException 예외
	 */
	public <T> T doGet(URI uri, Object paramObj, IHttpMapper<T> mapper) throws AdapterException;

	/**
	 * HTTP Server에 POST방식으로 요청을 한다.
	 * 
	 * @param uri 요청할 서버의 URI 문자열
	 * @param paramObj 요청 데이터
	 * @param mapper 요청, 응답 데이터 매핑 처리
	 * @return 응답 처리 객체
	 * @throws AdapterException 예외
	 */
	public <T> T doPost(String uri, Object paramObj, IHttpMapper<T> mapper) throws AdapterException;

	/**
	 * HTTP 요청 URI를 생성한다.
	 * 
	 * @param url HTTP 서버의 주소 (http://www.domain.com:port/path)
	 * @param paramMap 요청 parameter map
	 * @return 요청 URI instance
	 * @throws AdapterException 예외
	 */
	public URI buildURI(String url, Map<String, String> paramMap) throws AdapterException;

	/**
	 * HTTP 요청 URI를 생성한다.
	 * 
	 * @param url HTTP 서버의 주소 (http://www.domain.com:port/path)
	 * @param paramObj 요청 parameter object
	 * @return 요청 URI instance
	 * @throws AdapterException 예외
	 */
	public <T> URI buildURI(String url, T paramObj) throws AdapterException;
}

package org.oh.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.PropertyConfigurator;
import org.oh.common.download.Attachment;
import org.oh.common.download.URLDownloader;
import org.oh.common.exception.CommonException;

/**
 * 파일/디렉토리 관련 유틸리티 클래스.<br/>
 * - org.apache.commons.io.IOUtils 클래스를 상속받음.
 *
 * @see <a href=http://commons.apache.org/io/api-release/org/apache/commons/io/IOUtils.html>org.apache.commons.io.IOUtils</a>
 */
public abstract class HTTPUtil extends org.apache.commons.io.IOUtils {
//	public static final BasicResponseHandler BASIC_RESPONSE_HANDLER = new BasicResponseHandler();

	protected static HttpClient httpClient = null;

	/**
	 * List<NameValuePair> -> Map<String, String> 변환
	 */
	public static Map<String, String> convertListToMap(List<NameValuePair> list) {
		Map<String, String> paramMap = new HashMap<String, String>();
		if (list != null) {
			for (NameValuePair param : list) {
				paramMap.put(param.getName(), param.getValue());
			}
		}

		return paramMap;
	}

	/**
	 * List<NameValuePair> -> Object 변환
	 */
	public static <T> T convertListToObject(List<NameValuePair> list, Class<T> resultType) throws CommonException {
		Object obj = null;
		try {
			obj = resultType.newInstance();
			for (NameValuePair pair : list) {
				Field field = null;
				try {
					field = resultType.getDeclaredField(pair.getName());
				} catch (NoSuchFieldException e) {
					continue;
				}
				field.setAccessible(true);
				field.set(obj, pair.getValue());
			}
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Convert list to object \"" + list + "\" error", e.getMessage()), e);
		}

		return (T) obj;
	}

	/**
	 * Map<String, String> -> List<NameValuePair> 변환
	 */
	public static List<NameValuePair> convertMapToList(Map<String, String> map) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (map != null) {
			for (Map.Entry<String, String> param : map.entrySet()) {
				if (param == null || param.getValue() == null)
					continue;

				list.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
		}

		return list;
	}

	/**
	 * List<Attachment> -> Map<String, byte[]> 변환
	 */
	public static Map<String, byte[]> convertAttachListToMap(List<Attachment> attachs) {
		Map<String, byte[]> fileMap = new HashMap<String, byte[]>();
		if (attachs != null) {
			for (Attachment attachment2 : attachs) {
				fileMap.put(attachment2.getName(), attachment2.getBytes());
			}
		}

		return fileMap;
	}

	/**
	 * 파일경로 -> List<Attachment> 변환
	 */
	public static List<Attachment> convertFileArrayToList(String... filePaths) {
		List<Attachment> list = new ArrayList<Attachment>();
		if (filePaths != null) {
			for (String filePath : filePaths) {
				list.add(new Attachment("", FilenameUtils.getName(filePath), getFileBytes(filePath)));
			}
		}

		return list;
	}

	/**
	 * Object -> List<NameValuePair> 변환
	 */
	public static List<NameValuePair> convertObjectToParamList(Object obj) throws CommonException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				list.add(new BasicNameValuePair(field.getName(), field.get(obj).toString()));
			}
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Convert object to list \"" + obj + "\" error", e.getMessage()), e);
		}

		return list;
	}

	/**
	 * Object -> List<Attachment> 변환
	 */
	public static List<Attachment> convertObjectToAttachList(Object obj) {
		List<Attachment> list = new ArrayList<Attachment>();

		Attachment attach = ReflectionUtil.getValue(obj, Attachment.class);
		if (Utils.isValidate(attach)) {
			list.add(attach);
		}

		Attachment[] attachs = ReflectionUtil.getValue(obj, Attachment[].class);
		if (Utils.isValidate(attachs)) {
			list.addAll(Utils.convertArrayToList(ReflectionUtil.getValue(obj, Attachment[].class)));
		}

		return list;
	}

	/**
	 * 객체의 필드를 파라미터로 URI를 만든다.
	 */
	public static String convertObjectToURI(String url, List<NameValuePair> formParams) {
		if (!Utils.isValidate(formParams))
			return url;

		return url + "?" + URLEncodedUtils.format(formParams, (String) null);
	}

	/**
	 * 파일 읽기
	 */
	public static byte[] getFileBytes(String filePath) throws CommonException {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(filePath));

			return toByteArray(bis);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Load bytes file \"" + filePath + "\" error", e.getMessage()), e);
		} finally {
			closeQuietly(bis);
		}
	}

	/**
	 * 기본 메소드명 반환
	 */
	public static String getDefaultMethod(List<NameValuePair> params) {
		return HttpGet.METHOD_NAME;
	}

	/**
	 * 기본 캐릿터셋 반환
	 */
	public static Charset getCharset(String charset) {
		return Charset.forName(HTTPUtils.getCharset(charset));
	}

	// //////////////////////////////////////////////////////////////////////////

	/**
	 * 파일 다운로드
	 */
	public static byte[] getBytes(String url, String fileName) throws CommonException {
		URLDownloader urlDownloader = new URLDownloader();

		Attachment attch = null;
		try {
			attch = urlDownloader.download(fileName, url);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR, LogUtil.buildMessage(
					"Download bytes from url \"" + url + "\", file \"" + fileName + "\" error", e.getMessage()), e);
		}

		return attch.getBytes();
	}

	// //////////////////////////////////////////////////////////////////////////

	/**
	 * 커넥션 반환
	 */
	public static synchronized HttpClient getHttpClient() throws CommonException {
		if (httpClient != null)
			return httpClient;

		// ver 4.2
//		X509TrustManager x509TrustManager = new X509TrustManager() {
//			@Override
//			public void checkClientTrusted(X509Certificate[] chain, String authType) {
//			}
//
//			@Override
//			public void checkServerTrusted(X509Certificate[] chain, String authType) {
//			}
//
//			@Override
//			public X509Certificate[] getAcceptedIssuers() {
//				return null;
//			}
//		};

		try {
//			SSLContext sslContext = SSLContext.getInstance(SSLSocketFactory.TLS);
//			sslContext.init(null, new X509TrustManager[] { x509TrustManager }, null);
//			SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext,
//					SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//			Scheme scheme = new Scheme("https", 443, sslSocketFactory);
//
////			httpClient = new DefaultHttpClient();
////			httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
//
//			PoolingClientConnectionManager connMgr = new PoolingClientConnectionManager();
//			connMgr.getSchemeRegistry().register(scheme);

			// ver 4.3
			Registry<ConnectionSocketFactory> schemeRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
					schemeRegistry);

			// 커넥션 풀 설정
			String key = "http.connection.pool.max_per_route";
			String value = PropertyUtils.getInstance().getString(key, "");
			LogUtil.writeLog("properties:[" + key + "=" + value + "]", HTTPUtil.class);
			if (Utils.isValidate(value)) {
//				connMgr.setDefaultMaxPerRoute(Integer.parseInt(value));
				connectionManager.setDefaultMaxPerRoute(Integer.parseInt(value));
			}

			key = "http.connection.pool.max_total";
			value = PropertyUtils.getInstance().getString(key, "");
			LogUtil.writeLog("properties:[" + key + "=" + value + "]", HTTPUtil.class);
			if (Utils.isValidate(value)) {
//				connMgr.setMaxTotal(Integer.parseInt(value));
				connectionManager.setMaxTotal(Integer.parseInt(value));
			}

//			httpClient = new DefaultHttpClient(connMgr);
//
//			// 커넥션 타임아웃 설정
//			HttpConnectionParamBean param = new HttpConnectionParamBean(httpClient.getParams());
			RequestConfig.Builder requestBuilder = RequestConfig.custom();

			key = "http.connection.timeout";
			value = PropertyUtils.getInstance().getString(key, "");
			LogUtil.writeLog("properties:[" + key + "=" + value + "]", HTTPUtil.class);
			if (Utils.isValidate(value)) {
//				param.setConnectionTimeout(Integer.parseInt(value));
				requestBuilder = requestBuilder.setConnectTimeout(Integer.parseInt(value));
			}

			key = "http.socket.timeout";
			value = PropertyUtils.getInstance().getString(key, "");
			LogUtil.writeLog("properties:[" + key + "=" + value + "]", HTTPUtil.class);
			if (Utils.isValidate(value)) {
//				param.setSoTimeout(Integer.parseInt(value));
				requestBuilder = requestBuilder.setSocketTimeout(Integer.parseInt(value));
			}

//			return httpClient;
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			builder.setDefaultRequestConfig(requestBuilder.build());
			httpClient = builder.build();

			return httpClient;
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR,
					LogUtil.buildMessage("Create http client error", e.getMessage()), e);
		}
	}

	public static void download(String url, String filePath) throws CommonException {
		download(url, null, filePath);
	}

	public static void download(String url, List<NameValuePair> params, String filePath) throws CommonException {
		download(url, getDefaultMethod(params), params, filePath);
	}

	public static void download(String url, String method, List<NameValuePair> params, String filePath)
			throws CommonException {
		download(url, method, null, params, filePath);
	}

	public static void download(String url, String method, List<NameValuePair> headers, List<NameValuePair> params,
			String filePath) throws CommonException {
		download(url, method, headers, params, filePath, null);
	}

	/**
	 * 다운로드
	 */
	public static void download(String url, String method, List<NameValuePair> headers, List<NameValuePair> params,
			String filePath, String charset) throws CommonException {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(filePath));
			bos.write((byte[]) callHttp(url, method, headers, params, charset).get("content"));
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR, LogUtil.buildMessage(
					"Download url \"" + url + "\" to file \"" + filePath + "\" error", e.getMessage()), e);
		} finally {
			closeQuietly(bos);
		}
	}

	public static Map<String, Object> callHttp(String url) throws CommonException {
		return callHttp(url, null);
	}

	public static Map<String, Object> callHttp(String url, List<NameValuePair> params) throws CommonException {
		return callHttp(url, getDefaultMethod(params), params);
	}

	public static Map<String, Object> callHttp(String url, String method, List<NameValuePair> params)
			throws CommonException {
		return callHttp(url, method, null, params);
	}

	public static Map<String, Object> callHttp(String url, String method, List<NameValuePair> headers,
			List<NameValuePair> params) throws CommonException {
		return callHttp(url, method, headers, params, null);
	}

	/**
	 * HTTP 호출
	 *
	 * @return Map(header, content)
	 */
	public static Map<String, Object> callHttp(String url, String method, List<NameValuePair> headers,
			List<NameValuePair> params, String charset) throws CommonException {
		Map<String, Object> response = new HashMap<String, Object>();

		String message = "Call HTTP URL \"" + url + "\" error";
		Charset encCharset = getCharset(charset);
		HttpRequestBase httpRequest = null;
		try {
			url = HTTPUtils.encodeURL(url, charset);
			if (HttpPost.METHOD_NAME.equalsIgnoreCase(method)) {
				httpRequest = new HttpPost(url);
			} else if (HttpPut.METHOD_NAME.equalsIgnoreCase(method)) {
				httpRequest = new HttpPut(url);
			} else if (HttpDelete.METHOD_NAME.equalsIgnoreCase(method)) {
				httpRequest = new HttpDelete(url);
			} else {
				httpRequest = new HttpGet(url);
			}

			if (HttpPost.METHOD_NAME.equalsIgnoreCase(method) || HttpPut.METHOD_NAME.equalsIgnoreCase(method)) {
				// 파라미터 설정
				if (Utils.isValidate(params)) {
					HttpEntityEnclosingRequestBase httpBase2 = ((HttpEntityEnclosingRequestBase) httpRequest);
					if (Utils.isValidate(params.get(0).getName()))
						httpBase2.setEntity(new UrlEncodedFormEntity(params, encCharset));
					else
						httpBase2.setEntity(new StringEntity(params.get(0).getValue(), encCharset));
				}

				// 로그 출력
//				HttpEntity httpEntity = ((HttpPost) httpRequest).getEntity();
//				ByteArrayOutputStream out = new ByteArrayOutputStream((int) httpEntity.getContentLength());
//				httpEntity.writeTo(out);
//				LogUtil.writeLog(new String(out.toByteArray()), HTTPUtil.class);
			}

			if (headers != null) {
				for (NameValuePair header : headers) {
					httpRequest.setHeader(header.getName(), header.getValue());
				}
			}

			HttpResponse resHttp = getHttpClient().execute(httpRequest);
			StatusLine statusLine = resHttp.getStatusLine();
			LogUtil.writeLog("statusline: " + statusLine.toString(), HTTPUtil.class);

			HttpEntity entity = resHttp.getEntity();
			if (statusLine.getStatusCode() >= 300) {
				String content = toString(entity.getContent());
				EntityUtils.consume(entity);
				throw new HttpResponseException(statusLine.getStatusCode(), content);
			}

			response.put("header", resHttp.getAllHeaders());
			response.put("content", toByteArray(entity.getContent()));
//			Charset defaultCharset = ContentType.getOrDefault(entity).getCharset();
//			String responseString = BASIC_RESPONSE_HANDLER.handleResponse(resHttp);
//			response.put("content", responseString.getBytes(defaultCharset));
		} catch (CommonException e) {
			throw e;
		} catch (HttpResponseException e) {
			throw new CommonException(e.getStatusCode() + "", LogUtil.buildMessage(message, e.getMessage()), e);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR, LogUtil.buildMessage(message, e.getMessage()), e);
		} finally {
			if (httpRequest != null)
				httpRequest.releaseConnection();
		}

		return response;
	}

	public static Map<String, Object> upload(String url, List<Attachment> attachs) throws CommonException {
		return upload(url, null, attachs);
	}

	public static Map<String, Object> upload(String url, List<NameValuePair> params, List<Attachment> attachs)
			throws CommonException {
		return upload(url, getDefaultMethod(params), params, attachs);
	}

	public static Map<String, Object> upload(String url, String method, List<NameValuePair> params,
			List<Attachment> attachs) throws CommonException {
		return upload(url, method, null, params, attachs);
	}

	public static Map<String, Object> upload(String url, String method, List<NameValuePair> headers,
			List<NameValuePair> params, List<Attachment> attachs) throws CommonException {
		return upload(url, method, headers, params, attachs, null);
	}

	/**
	 * 업로드
	 *
	 * @return Map(header, content)
	 */
	public static Map<String, Object> upload(String url, String method, List<NameValuePair> headers,
			List<NameValuePair> params, List<Attachment> attachs, String charset) throws CommonException {
		Map<String, Object> response = new HashMap<String, Object>();

		String message = "Upload files \"" + attachs + "\" to url \"" + url + "\" error";
		Charset encCharset = getCharset(charset);
		HttpPost httpPost = null;
		try {
			url = HTTPUtils.encodeURL(url, charset);
			httpPost = new HttpPost(url);
			if (attachs != null && attachs.size() > 0) {
				// ver 4.0
//				MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,
//						encCharset);
				// ver 4.3
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				builder.setCharset(encCharset);
				if (params != null) {
					for (NameValuePair param : params) {
//						multipartEntity.addPart(param.getName(), new StringBody(param.getValue(), encCharset));
						builder.addPart(param.getName(), new StringBody(param.getValue(), encCharset));
					}
				}
				for (Attachment attachment : attachs) {
//					multipartEntity.addPart(attachment.getFileName(),
//							new ByteArrayBody(attachment.getBytes(), attachment.getFileName()));
					builder.addPart(attachment.getFileName(),
							new ByteArrayBody(attachment.getBytes(), attachment.getFileName()));
				}
//				httpPost.setEntity(multipartEntity);
				httpPost.setEntity(builder.build());
			} else if (Utils.isValidate(params)) {
				httpPost.setEntity(new UrlEncodedFormEntity(params, encCharset));
			}

			if (headers != null) {
				for (NameValuePair header : headers) {
					httpPost.setHeader(header.getName(), header.getValue());
				}
			}

			// 로그 출력
//			HttpEntity httpEntity = httpPost.getEntity();
//			ByteArrayOutputStream out = new ByteArrayOutputStream((int) httpEntity.getContentLength());
//			httpEntity.writeTo(out);
//			LogUtil.writeLog(new String(out.toByteArray()), HTTPUtil.class);

			HttpResponse resHttp = getHttpClient().execute(httpPost);
			StatusLine statusLine = resHttp.getStatusLine();
			LogUtil.writeLog("statusline: " + statusLine.toString(), HTTPUtil.class);

			HttpEntity entity = resHttp.getEntity();
			if (statusLine.getStatusCode() >= 300) {
				String content = toString(entity.getContent());
				EntityUtils.consume(entity);
				throw new HttpResponseException(statusLine.getStatusCode(), content);
			}

			response.put("header", resHttp.getAllHeaders());
			response.put("content", toByteArray(entity.getContent()));
		} catch (CommonException e) {
			throw e;
		} catch (HttpResponseException e) {
			throw new CommonException(e.getStatusCode() + "", LogUtil.buildMessage(message, e.getMessage()), e);
		} catch (Exception e) {
			throw new CommonException(CommonException.ERROR, LogUtil.buildMessage(message, e.getMessage()), e);
		} finally {
			if (httpPost != null)
				httpPost.releaseConnection();
		}

		return response;
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("HOME", "C:/dev/workspace/workspace_common/HOME");
		PropertyConfigurator.configure(System.getProperty("HOME") + "/conf/server/log4j.properties");

//		String url = "https://www.google.co.kr/images/srpr/logo11w.png";
//		byte[] bytes = getBytes(url, "logo11w.png");
//		try {
//			write(bytes, new FileOutputStream("C:/down/logo11w.png"));
//		} catch (Exception e) {
//			LogUtil.writeLog(e);
//		}

//		String url = "http://127.0.0.1:5050/common/upload"; // 로컬
//		String url = "https://mot.keb.co.kr:8443/keb-mgw/upload"; // 개발
//		String url = "https://211.210.107.143:8443/keb-mgw/upload"; // 운영
//		String url = "https://appstoredev.dongwha-mh.com/NSG/upload"; // 개발
//		String url = "https://appstore.dongwha-mh.com/NSG/upload"; // 운영
//		Map<String, Object> result = upload(url, convertFileArrayToList("test/test.zip", "test/test1.zip"));

//		String url = "http://localhost:8080/common/upload";
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("skoh", "테스트"));
//		url = convertObjectToURI(url, params); // GET
//		Map<String, Object> result = upload(url, params, convertFileArrayToList("test/테스트.json"));

//		String url = "https://m.dongwha-mh.com/Storage/GW/FileStorage/63/15909_안전표어 공모 양식.xlsx";
//		download(url, "test/15909_안전표어 공모 양식.xlsx");

//		String url = "http://gw.nongshim.kr/ekp/upload/attach/brd/2014/06/26/1403762649085.1";
//		download(url, "등대.jpg");

//		Test01 t = new Test01();
//		Object o = ReflectionUtils2.getValue(t, "s");
//		LogUtil.writeLog(t);
//		ReflectionUtils2.setValue(t, "s", "b");
//		LogUtil.writeLog(t);
//		Attachment[] field = ReflectionUtils2.getValue(t, Attachment[].class);
//		LogUtil.writeLog(Utils.toString("field:", field));

//		List<Attachment> list = convertObjectToList(t);
//		LogUtil.writeLog(list);

//		String query = "mode=1&file_name=&I_BUKRS=1000&I_BNUMB=0000000724&I_GJAHR=2014&I_OBJKEY=FOL38000000000004EXT39000000000165";
//		NSG326Request_Body body = convertListToObject(URLEncodedUtils.parse(query, Charset.forName("UTF-8")),
//				NSG326Request_Body.class);
//		LogUtil.writeLog(body);

		String url = "http://localhost/openapi/login.crd?userid=bW9iaWdlbg==&userdomain=bm9uZ3NoaW0=&en_userpass_md5=e2ac6dbc3f59b7ac9b3943e50e51e21f&en_userpass_sha2=df34cbc326c92ce89d529d2df34e3db74aaa09696c710c78174ad143c3670de6";
		Map<String, Object> result = callHttp(url);

		LogUtil.writeLog(result);
		LogUtil.writeLog(new String((byte[]) result.get("content")));
	}

	public static class Test01 {
//		private String mode = "mobileAppsUpload";
		private String moduleName = "brd";

//		private Attachment attach1 = new Attachment("", "attach", null);
//		private Attachment[] attachs = new Attachment[] { new Attachment("", "attachs1", null),
//				new Attachment("", "attachs2", null) };
//		public byte[] bytes = new byte[] { 65 };

		public String toString() {
			return ReflectionToStringBuilder.toString(this);
		}
	}
}
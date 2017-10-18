package com.nemustech.common.util;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import com.nemustech.common.exception.SmartException;

/**
 * 네트웍/통신 관련 유틸리티 클래스
 * 
 * @since 1.0.0
 * @version 2.5
 */
public abstract class NetUtil {
	/** HTTP 요청메소드 : GET */
	public static final String HTTP_METHOD_GET = "GET";
	/** HTTP 요청메소드 : POST */
	public static final String HTTP_METHOD_POST = "POST";

	/** HTTP 응답결과 ID : Content-Type */
	public static final String HTTP_RESPONSE_CONTENT_TYPE = "Content-Type";
	/** HTTP 응답결과 ID : Content-Length */
	public static final String HTTP_RESPONSE_CONTENT_LEGNTH = "Content-Length";
	/** HTTP 응답결과 ID : Content-Encoding */
	public static final String HTTP_RESPONSE_CONTENT_ENCODING = "Content-Encoding";
	/** HTTP 응답결과 ID : 응답결과 HTML 본문(Content) */
	public static final String HTTP_RESPONSE_CONTENT = "CONTENT";
	/** HTTP 응답결과 ID : 응답 상태코드 */
	public static final String HTTP_RESPONSE_STATUS_CODE = "STATUS_CODE";
	/**
	 * PROXY 서버를 통할 경우 PROXY 서버에서 client의 IP를 넘겨주는 header 이름. <BR />
	 * {@value}
	 */
	public static final String[] PROXY_HEADER_NAMES = { "Proxy-Client-IP", "proxy-client-ip", "PROXY-CLIENT-IP",
			"X-Forwarded-For", "x-forwarded-for", "X-FORWARDED-FOR", "HTTP_X_Forwarded-For", "http_x_forwarded-for",
			"HTTP_X_FORWARDED_FOR", "WL-Proxy-Client-IP", "wl-proxy-client-ip", "WL-PROXY-CLIENT-IP" };

	/** Mime 맵. */
	@SuppressWarnings("serial")
	protected static Map<String, String> mimeMap = new HashMap<String, String>() {
		{
			// text 타입
			put("css", "text/css");
			put("htm", "text/html");
			put("html", "text/html");
			put("js", "text/javascript");

			// image 타입
			put("bmp", "image/bmp");
			put("gif", "image/gif");
			put("jpe", "image/jpeg");
			put("jpeg", "image/jpeg");
			put("jpg", "image/jpeg");
			put("png", "image/png");

			// iPhone/Android
			put("ipa", "application/octet-stream");
			put("plist", "text/xml");
			put("apk", "application/vnd.android.package-archive");

			put("xls", "application/x-msdownload");
		}
	};

	/**
	 * 주어진 파일확장자의 Mime Type을 얻는 메소드.
	 *
	 * @param strExtension 파일확장자.
	 * @return Mime Type. (알수없는 확장자의 경우 application/octet-stream 을 리턴)
	 */
	public static String getMimeType(String strExtension) {
		try {
			return StringUtil.defaultIfBlank(mimeMap.get(strExtension.toLowerCase()), "application/octet-stream");
		} catch (Exception e) {
			return "application/octet-stream";
		}
	}

	/**
	 * 로컬IP를 가져온다.
	 *
	 * @return 로컬IP
	 */
	public static String getLocalIp() {
		String localIp = null;
		try {
			localIp = getIp(InetAddress.getLocalHost());
			if (localIp != null && localIp.startsWith("127.") == false) {
				return localIp;
			}

			Enumeration<NetworkInterface> networkInterfaceEnum = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaceEnum.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaceEnum.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();

				while (enumInetAddress.hasMoreElements()) {
					InetAddress localAddr = enumInetAddress.nextElement();
					localIp = localAddr.getHostAddress();

					if ((localIp != null && localIp.startsWith("127.") == false)
							&& (localIp.indexOf('.') > -1 && localAddr.isLoopbackAddress() == false)) {
						return localIp;
					}
				}
			}

		} catch (Exception e) {
			LogUtil.writeLog(e, NetUtil.class);
		}

		return (localIp == null) ? "127.0.0.1" : localIp;
	}

	/**
	 * 톰캣Port를 가져온다.
	 *
	 * @return 톰캣Port
	 */
	public static int getTomcatPort() {
		int port = -1;
		try {
			MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
			Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
					Query.match(Query.attr("protocol"), Query.value("HTTP/1.1"))); // embed : org.apache.coyote.http11.Http11NioProtocol
			port = Integer.parseInt(objectNames.iterator().next().getKeyProperty("port"));
		} catch (Exception e) {
			LogUtil.writeLog(e.toString(), NetUtil.class);
		}

		return port;
	}

	protected static String getIp(InetAddress localAddr) {
		StringBuffer localIp = new StringBuffer();
		try {
			byte[] localAddrArray = localAddr.getAddress();
			for (int i = 0; i < localAddrArray.length; i++) {
				if (i > 0) {
					localIp.append(".");
				}
				localIp.append(localAddrArray[i] < 0 ? localAddrArray[i] + 256 : localAddrArray[i]);
			}
		} catch (Exception e) {
			LogUtil.writeLog(e, NetUtil.class);
		}

		return localIp.length() > 0 ? localIp.toString() : "127.0.0.1";
	}

	/**
	 * HTTP 응답결과를 <응답결과ID, 응답결과값>맵으로 변환한다.
	 *
	 * @param response HTTP 응답결과
	 * @return <응답결과ID, 응답결과값>맵<br/>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT_TYPE : Content-Type</br>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT_LEGNTH : Content-Length</br>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT_ENCODING : Content-Encoding</br>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT : 응답결과 HTML 본문(Content)</br>
	 *         - NetUtil.HTTP_RESPONSE_STATUS_CODE : 응답 상태코드</br>
	 * @throws SmartException
	 */
	protected static Map<String, Object> toResponseMap(HttpResponse response) throws SmartException {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				Header header = entity.getContentType();
				if (header != null) {
					responseMap.put(HTTP_RESPONSE_CONTENT_TYPE, header.getValue());
				}

				header = entity.getContentEncoding();
				if (header != null) {
					responseMap.put(HTTP_RESPONSE_CONTENT_ENCODING, header.getValue());
				}

				responseMap.put(HTTP_RESPONSE_CONTENT_LEGNTH, entity.getContentLength());
				responseMap.put(HTTP_RESPONSE_CONTENT, EntityUtils.toString(entity));
				responseMap.put(HTTP_RESPONSE_STATUS_CODE, response.getStatusLine().getStatusCode());
			}

		} catch (Exception e) {
			throw new SmartException(e);
		}

		return responseMap;
	}

	protected static HttpClient getHttpClient(String uri, int connectionTimeout, int soTimeout) throws SmartException {
		try {
			BasicHttpParams httpParams = new BasicHttpParams();

			System.setProperty("http.auth.preference", "basic");
			System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");

			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setUseExpectContinue(httpParams, false);
			HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout);
			HttpConnectionParams.setSoTimeout(httpParams, soTimeout);

			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[] { new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new SecureRandom());

			HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			SSLSocketFactory socketFactory = new SSLSocketFactory(context, (X509HostnameVerifier) hostnameVerifier);
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

			ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager();
			connManager.setMaxTotal(100);

			URI url = new URI(uri);
			String protocol = url.getScheme();
			if (StringUtil.equals(protocol, "http")) {
				connManager.getSchemeRegistry()
						.register(new Scheme(protocol, url.getPort(), PlainSocketFactory.getSocketFactory()));
			} else if (StringUtil.equals(protocol, "https")) {
				connManager.getSchemeRegistry().register(new Scheme(protocol, url.getPort(), socketFactory));
			}

			return new DefaultHttpClient(connManager, httpParams);
		} catch (Exception e) {
			LogUtil.writeLog(e, NetUtil.class);
			throw new SmartException(e);
		}
	}

	/**
	 * HTTP 요청을 실행한다.
	 *
	 * @param uri 요청 URL
	 * @param method 요청메소드<br/>
	 *            - NetUtil.HTTP_METHOD_GET : GET<br/>
	 *            - NetUtil.HTTP_METHOD_POST : POST<br/>
	 * @param connectionTimeout 요청연결 타임아웃 (단위 : millisec)
	 * @param soTimeout 소켓연결 타임아웃 (단위 : millisec)
	 * @return <응답결과ID, 응답결과값>맵<br/>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT_TYPE : Content-Type</br>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT_LEGNTH : Content-Length</br>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT_ENCODING : Content-Encoding</br>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT : 응답결과 HTML 본문(Content)</br>
	 *         - NetUtil.HTTP_RESPONSE_STATUS_CODE : 응답 상태코드</br>
	 * @throws SmartException
	 */
	public static Map<String, Object> executeHttp(String uri, String method, int connectionTimeout, int soTimeout)
			throws SmartException {
		try {
			HttpClient httpClient = getHttpClient(uri, connectionTimeout, soTimeout);
			HttpUriRequest httpRequest = (method != null && method.trim().toUpperCase().equals(HTTP_METHOD_POST))
					? new HttpPost(uri) : new HttpGet(uri);
			HttpResponse response = httpClient.execute(httpRequest);
			return toResponseMap(response);
		} catch (SmartException e) {
			throw e;
		} catch (Exception e) {
			LogUtil.writeLog(e, NetUtil.class);
			throw new SmartException(e);
		}
	}

	public static Map<String, Object> executeHttpGet(String uri, int connectionTimeout, int soTimeout)
			throws SmartException {
		return executeHttp(uri, HTTP_METHOD_GET, connectionTimeout, soTimeout);
	}

	public static Map<String, Object> executeHttpPost(String uri, int connectionTimeout, int soTimeout)
			throws SmartException {
		return executeHttp(uri, HTTP_METHOD_POST, connectionTimeout, soTimeout);
	}

	/**
	 * Multipart HTTP / HTTPS POST 요청을 실행한다.
	 *
	 * @param uri 요청 URL
	 * @param connectionTimeout 요청연결 타임아웃 (단위 : millisec)
	 * @param soTimeout 소켓연결 타임아웃 (단위 : millisec)
	 * @param paramMap <요청파라미터ID, 요청파라미터값>맵
	 * @return <응답결과ID, 응답결과값>맵<br/>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT_TYPE : Content-Type</br>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT_LEGNTH : Content-Length</br>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT_ENCODING : Content-Encoding</br>
	 *         - NetUtil.HTTP_RESPONSE_CONTENT : 응답결과 HTML 본문(Content)</br>
	 *         - NetUtil.HTTP_RESPONSE_STATUS_CODE : 응답 상태코드</br>
	 * @throws SmartException
	 */
	public static Map<String, Object> executeMultipartHttp(String uri, int connectionTimeout, int soTimeout,
			Map<String, Object> paramMap) throws SmartException {
		try {
			HttpClient httpClient = getHttpClient(uri, connectionTimeout, soTimeout);

			HttpPost httpRequest = new HttpPost(uri);

			if (paramMap != null) {
				MultipartEntity entity = new MultipartEntity();
				Iterator<String> it = paramMap.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					Object value = paramMap.get(key);
					if (value instanceof File) {
						entity.addPart(key, new FileBody((File) value));
					} else if (value instanceof String) {
						entity.addPart(key, new StringBody((String) value));
					} else {
						entity.addPart(key, new StringBody(value.toString()));
					}
				}
				httpRequest.setEntity(entity);
			}

			HttpResponse response = httpClient.execute(httpRequest);
			return toResponseMap(response);
		} catch (SmartException e) {
			throw e;
		} catch (Exception e) {
			LogUtil.writeLog(e, NetUtil.class);
			throw new SmartException(e);
		}
	}

	public static Map<String, Object> executeJsonHttp(String uri, int connectionTimeout, int soTimeout, String jsonText)
			throws SmartException {
		try {
			HttpClient httpClient = getHttpClient(uri, connectionTimeout, soTimeout);
			HttpPost httpRequest = new HttpPost(uri);

			httpRequest.setEntity(new StringEntity(jsonText));
			httpRequest.setHeader("Content-type", "application/json");

			HttpResponse response = httpClient.execute(httpRequest);
			return toResponseMap(response);
		} catch (SmartException e) {
			throw e;
		} catch (Exception e) {
			LogUtil.writeLog(e, NetUtil.class);
			throw new SmartException(e);
		}
	}

	/**
	 * 주어진 URI 로부터 호스트IP를 추출한다.
	 *
	 * @param uri URI
	 * @return 호스트IP
	 */
	public static String extractHostIp(String uri) {
		if (StringUtil.isBlank(uri)) {
			return uri;
		}

		String extractIp = null;
		int index = uri.indexOf("//");
		extractIp = (index == -1) ? uri : uri.substring(index + 2);
		index = extractIp.indexOf(":");
		if (index == -1) {
			index = extractIp.indexOf("/");
		}

		if (index != -1) {
			extractIp = extractIp.substring(0, index);
		}

		return extractIp;
	}

	public static String extractBrowser(String userAgent) {
		if (userAgent.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (userAgent.indexOf("Safari") > -1) {
			return "Safari";
		} else if (userAgent.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (userAgent.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}

	public static String extractOsVersion(String userAgent, String regex) {
		try {
			String osVersionText = null;
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(userAgent);
			while (matcher.find()) {
				osVersionText = matcher.group();
			}

			if (StringUtil.isNotBlank(osVersionText)) {
				String[] temp = osVersionText.split("\\s");
				return temp[temp.length - 1].replaceAll("_", ".");
			}
		} catch (Exception e) {
			LogUtil.writeLog(e, NetUtil.class);
		}

		return null;
	}

	/**
	 * HTTP 요청한 client의 IP를 얻는다. <br />
	 * 만약, Proxy 서버를 통해서 요청이 온 경우는 HTTP의 header 정보 중에서 <BR />
	 * {@link #PROXY_HEADER_NAMES}에 등록된 HTTP header 값 중에서 IP를 얻는다.
	 * 
	 * @param request HttpServletRequest
	 * @return client IP, IP를 얻지 못 하면 null
	 */
	public static String getClientIP(HttpServletRequest request) {
		String IP = null;

		for (String name : PROXY_HEADER_NAMES) {
			IP = request.getHeader(name);

			if (IP != null && IP.length() > 0) {
				break;
			}
		}

		if (IP == null || IP.length() == 0) {
			IP = request.getRemoteAddr();
		}

		return IP;
	}

	/**
	 * Local 시스템이 사용하는 모든 IP 정보를 돌려 준다.
	 * 
	 * @return IP의 List. Not null
	 */
	public static List<String> getLocalIPs() {
		ArrayList<String> list = new ArrayList<String>();

		try {
			InetAddress[] local = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());

			for (int i = 0; i < local.length; i++) {
				if (local[i].getHostAddress().matches("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b")) {
					list.add(local[i].getHostAddress());
				}
			}
		} catch (UnknownHostException e) {
			list.add("127.0.0.1");
		}

		return list;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(InetAddress.getLocalHost().getHostAddress());
		System.out.println(getLocalIp());
		System.out.println(getTomcatPort());
	}
}

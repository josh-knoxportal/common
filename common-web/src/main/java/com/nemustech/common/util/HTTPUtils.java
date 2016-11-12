package com.nemustech.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * HTTP 유틸(Pure JDK)
 */
public abstract class HTTPUtils {
	static {
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

		X509TrustManager x509TrustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new X509TrustManager[] { x509TrustManager }, null);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		} catch (Exception e) {
			LogUtil.writeLog(e, HTTPUtils.class);
		}
	}

	public static final byte[] BUFFER = new byte[1024 * 8]; // 8KB
	public static final int CONNECT_TIMEOUT_MS = 1000 * 2; // 2초
	public static final int READ_TIMEOUT_MS = 1000 * 0; // 무제한

	// 유틸 /////////////////////////////////////////////////////////////////////////

	/**
	 * xml 파일경로 -> Map<String, String> 읽기
	 */
	public static Map<String, String> readXMLFile(String filePath) throws RuntimeException {
		Map<String, String> xml = new HashMap<String, String>();
		if (!Utils.isValidate(filePath))
			return xml;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);
		Document doc = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(new File(filePath));
		} catch (Exception e) {
			throw new RuntimeException("Parse xml file \"" + filePath + "\" error", e);
		}

		Element element = doc.getDocumentElement();
		NodeList nodeList = element.getChildNodes();
		Node node = null;
		NamedNodeMap nodeMap = null;
		Node nID = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			node = nodeList.item(i);
			if ("property".equals(node.getNodeName())) {
				nodeMap = node.getAttributes();
				nID = nodeMap.getNamedItem("id");
				if (nID == null)
					continue;
				xml.put(nID.getNodeValue(), node.getFirstChild().getNodeValue().replaceAll("\\s", ""));
			}
		}

		return xml;
	}

	public static String getCharset() {
		return getCharset(null);
	}

	public static String getCharset(String charset) {
		return (Utils.isValidate(charset)) ? charset : "UTF-8";
	}

	public static String encodeURL(String url) throws RuntimeException {
		return encodeURL(url, null);
	}

	public static String encodeURL(String url, String charset) throws RuntimeException {
		if (!Utils.isValidate(url))
			return url;

		try {
			url = URLEncoder.encode(url, getCharset(charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Encode url \"" + url + "\", encode \"" + charset + "\" error", e);
		}

		url = url.replaceAll("%3A", ":");
		url = url.replaceAll("%2F", "/");
		url = url.replaceAll("%3F", "?");
		url = url.replaceAll("%26", "&");
		url = url.replaceAll("%3D", "=");
		url = url.replaceAll("\\+", "%20");

		return url;
	}

	public static String decodeURL(String url) throws RuntimeException {
		return decodeURL(url, null);
	}

	public static String decodeURL(String url, String charset) throws RuntimeException {
		if (!Utils.isValidate(url))
			return url;

		try {
			url = URLDecoder.decode(url, getCharset(charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Decode url \"" + url + "\", encode \"" + charset + "\" error", e);
		}

		return url;
	}

	public static URL getEncodeURL(String url) throws RuntimeException {
		LogUtil.writeLog("url: " + url, HTTPUtils.class);

		if (!Utils.isValidate(url))
			return null;

		String[] httpTokens = url.split("://", 2);
		String[] hostTokens = httpTokens[1].split("/", 2);
		String[] portTokens = hostTokens[0].split(":", 2);
		String host = hostTokens[0];

		int port = 80;
		if (portTokens.length == 2) {
			host = portTokens[0];
			port = Integer.parseInt(portTokens[1]);
		}

		URL url1 = null;
		try {
			url1 = new URL(httpTokens[0], host, port, "/" + encodeURL(hostTokens[1]));
		} catch (MalformedURLException e) {
			throw new RuntimeException("Formed url \"" + url + "\" error", e);
		}
		LogUtil.writeLog("url: " + url1, HTTPUtils.class);

		return url1;
	}

	public static Map<String, String> convertListToMap(String params) {
		return convertListToMap(params, "&");
	}

	public static Map<String, String> convertCookieMap(String cookies) {
		return convertListToMap(cookies, ";");
	}

	/**
	 * 파라미터 String -> Map<String, String> 변환
	 */
	public static Map<String, String> convertListToMap(String params, String delimiter) {
		Map<String, String> map = new HashMap<String, String>();
		if (!Utils.isValidate(params))
			return map;

		String[] arrParam = StringUtil.split(params, delimiter);
		for (String param : arrParam) {
			String[] pair = StringUtil.split(param, "=");
			map.put(pair[0], (pair.length >= 2) ? pair[1] : "");
		}

		return map;
	}

	/**
	 * Map을 파라미터로 URI를 만든다.
	 */
	public static String convertMapToURI(String url, Map<String, String> params) {
		return url + "?" + convertParamString(params);
	}

	public static String convertParamString(Map<String, String> params) {
		return convertParamString(params, "&");
	}

	public static String convertCookieString(Map<String, String> cookies) {
		return convertParamString(cookies, ";");
	}

	/**
	 * 파라미터 Map<String, String> -> String 변환
	 */
	public static String convertParamString(Map<String, String> params, String delimiter) {
		if (params == null)
			return "";

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> param : params.entrySet()) {
			if (param == null || param.getValue() == null)
				continue;

			if (sb.length() > 0)
				sb.append(delimiter);
			sb.append(param.getKey());
			if (param.getValue() != null) {
				sb.append("=");
				sb.append(param.getValue());
			}
		}

		return sb.toString();
	}

	/**
	 * 파일경로 String -> Map<String, byte[]> 변환
	 */
	public static Map<String, byte[]> getFiles(String... filePaths) throws RuntimeException {
		Map<String, byte[]> files = new HashMap<String, byte[]>();
		if (!Utils.isValidate(filePaths))
			return files;

		for (String filePath : filePaths) {
			files.put(Utils.getFileName2(filePath), getFileBytes(filePath));
		}

		return files;
	}

	/**
	 * result Map -> header Map
	 */
	public static Map<String, List<String>> getHeader(Map<String, Object> result) {
		if (!Utils.isValidate(result) || !result.containsKey("header"))
			return null;

		return (Map) result.get("header");
	}

	public static String getBodyString(Map<String, Object> result) {
		return getBodyString(result, getCharset());
	}

	/**
	 * result Map -> body String
	 */
	public static String getBodyString(Map<String, Object> result, String charset) {
		byte[] content = getBody(result);
		if (!Utils.isValidate(content))
			return "";

		try {
			return new String(content, charset);
		} catch (Exception e) {
			LogUtil.writeLog(e.getMessage(), HTTPUtils.class);
			return "";
		}
	}

	/**
	 * result Map -> body byte[]
	 */
	public static byte[] getBody(Map<String, Object> result) {
		if (!Utils.isValidate(result) || !result.containsKey("body"))
			return null;

		return (byte[]) result.get("body");
	}

	// 공통 /////////////////////////////////////////////////////////////////////////

	/**
	 * URL 연결
	 */
	public static URLConnection getURLConnection(String url, String method, Map<String, String> headers,
			String boundary, String charset) throws RuntimeException {
		LogUtil.writeLog(" url: " + url + ", method: " + method + ", headers: " + headers + ", boundary: " + boundary
				+ ", charset: " + charset, HTTPUtils.class);

		HttpURLConnection conn = null;
		try {
			URL url1 = new URL(encodeURL(url, charset));

			conn = (HttpURLConnection) url1.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
			conn.setReadTimeout(READ_TIMEOUT_MS);

			if (Utils.isValidate(method))
				conn.setRequestMethod(method);

			for (Map.Entry<String, String> header : headers.entrySet()) {
				if (header == null || header.getValue() == null)
					continue;

				conn.setRequestProperty(header.getKey(), header.getValue());
			}

			if (Utils.isValidate(boundary))
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			else
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			conn.connect();
		} catch (Exception e) {
			throw new RuntimeException("Connection \"" + conn + "\" error", e);
		}

		return conn;
	}

	public static Map<String, Object> callHttp(String url) throws RuntimeException {
		return callHttp(url, null);
	}

	public static Map<String, Object> callHttp(String url, Map<String, String> params) throws RuntimeException {
		return callHttp(url, null, params);
	}

	public static Map<String, Object> callHttp(String url, String method, Map<String, String> params)
			throws RuntimeException {
		return callHttp(url, method, null, params);
	}

	public static Map<String, Object> callHttp(String url, String method, Map<String, String> headers,
			Map<String, String> params) throws RuntimeException {
		return callHttp(url, method, headers, params, null);
	}

	/**
	 * HTTP 호출
	 *
	 * @return Map(header, content)
	 */
	public static Map<String, Object> callHttp(String url, String method, Map<String, String> headers,
			Map<String, String> params, String charset) throws RuntimeException {
		LogUtil.writeLog("*** Http calling...", HTTPUtils.class);

		Map<String, Object> response = new HashMap<String, Object>();
		String boundary = UUID.randomUUID().toString();
		HttpURLConnection conn = null;
		OutputStream os = null;
		try {
			conn = (HttpURLConnection) getURLConnection(url, method, headers, null, charset);
			os = writeParams(conn.getOutputStream(), boundary, params);
			LogUtil.writeLog("response: " + conn.getResponseCode() + ", " + conn.getResponseMessage(), HTTPUtils.class);

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				response.put("header", conn.getHeaderFields());
				response.put("body", getFileBytes(conn.getInputStream()));
			}
			LogUtil.writeLog("result: " + response, HTTPUtils.class);
		} catch (IOException e) {
			throw new RuntimeException("Connection \"" + conn + "\" error", e);
		} finally {
			if (conn != null)
				conn.disconnect();
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				throw new RuntimeException("Close output stream error", e);
			}
		}

		return response;
	}

	/**
	 * 파일 -> 바이트스트림
	 */
	public static byte[] getFileBytes(String filePath) throws RuntimeException {
		LogUtil.writeLog("filePath: " + filePath, HTTPUtils.class);

		if (!Utils.isValidate(filePath))
			return null;

		byte[] bytes = null;
		try {
			bytes = getFileBytes(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Write bytes from file \"" + filePath + "\" error", e);
		}

		return bytes;
	}

	/**
	 * 입력스트림 -> 바이트스트림
	 */
	public static byte[] getFileBytes(InputStream input) throws RuntimeException {
		if (input == null)
			return null;

		byte[] bytes = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = writeInputStream(new ByteArrayOutputStream(), input);
			bytes = baos.toByteArray();
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				throw new RuntimeException("Close input stream error", e);
			}
		}

		return bytes;
	}

	/**
	 * 입력스트림 -> 출력스트림
	 */
	public static <T extends OutputStream> T writeInputStream(T output, InputStream input) throws RuntimeException {
		if (input == null)
			return output;

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new BufferedInputStream(input);
			os = new BufferedOutputStream(output);
			int date = 0;
			while ((date = is.read(BUFFER)) != -1) {
				os.write(BUFFER, 0, date);
			}
			os.flush();
		} catch (IOException e) {
			throw new RuntimeException("Write input stream error", e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				throw new RuntimeException("Close input stream error", e);
			}
		}
//		LogUtil.writeLog("input: " + input + ", output: " + output, HTTPUtils.class);

		return output;
	}

	/**
	 * 바이트스트림 -> 출력스트림
	 */
	public static <T extends OutputStream> T writeBytes(T output, byte[] bytes) throws RuntimeException {
		if (!Utils.isValidate(bytes))
			return output;

		OutputStream os = null;
		try {
			os = new BufferedOutputStream(output);
			os.write(bytes);
			os.flush();
		} catch (IOException e) {
			throw new RuntimeException("Write bytes to output stream error", e);
		}
//		LogUtil.writeLog("bytes: " + bytes + ", output: " + output, HTTPUtils.class);

		return output;
	}

	public static <T extends OutputStream> T writeParams(T output, String boundary, Map<String, String> params)
			throws RuntimeException {
		return writeParams(output, boundary, params, true);
	}

	/**
	 * 파라미터 -> 출력스트림
	 */
	public static <T extends OutputStream> T writeParams(T output, String boundary, Map<String, String> params,
			boolean isLast) throws RuntimeException {
		if (!Utils.isValidate(params))
			return output;

		OutputStream os = null;
		try {
			os = new BufferedOutputStream(output);
			for (Map.Entry<String, String> param : params.entrySet()) {
				if (param == null || param.getValue() == null)
					continue;

				if (Utils.isValidate(param.getKey())) {
					os.write(("--" + boundary + Utils.LINE_SEPARATOR).getBytes());
					os.write(("Content-Disposition: form-data; name=\"" + param.getKey() + "\"").getBytes());
					os.write((Utils.LINE_SEPARATOR + Utils.LINE_SEPARATOR).getBytes());
				}

				os.write(param.getValue().getBytes());

				if (Utils.isValidate(param.getKey()))
					os.write(Utils.LINE_SEPARATOR.getBytes());
			}
			if (Utils.isValidate(params.keySet().toArray()[0]) && isLast)
				os.write(("--" + boundary + "--" + Utils.LINE_SEPARATOR).getBytes());

			os.flush();
		} catch (IOException e) {
			throw new RuntimeException("Write files \"" + params + "\" to output stream error", e);
		}

//		if (isLast)
		LogUtil.writeLog("boundary: " + boundary + ", output: " + output, HTTPUtils.class);

		return output;
	}

	public static <T extends OutputStream> T writeFiles(T output, String boundary, Map<String, byte[]> files)
			throws RuntimeException {
		return writeFiles(output, boundary, files, true);
	}

	/**
	 * 바이트스트림 -> 출력스트림
	 */
	public static <T extends OutputStream> T writeFiles(T output, String boundary, Map<String, byte[]> files,
			boolean isLast) throws RuntimeException {
		if (!Utils.isValidate(files))
			return output;

		OutputStream os = null;
		try {
			os = new BufferedOutputStream(output);
			for (Map.Entry<String, byte[]> file : files.entrySet()) {
				if (file == null || file.getValue() == null)
					continue;

				os.write(("--" + boundary + Utils.LINE_SEPARATOR).getBytes());
				os.write(("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\"" + file.getKey()
						+ "\"").getBytes());
				os.write((Utils.LINE_SEPARATOR + Utils.LINE_SEPARATOR).getBytes());
				os.write(file.getValue());
				os.write(Utils.LINE_SEPARATOR.getBytes());
			}
			if (isLast)
				os.write(("--" + boundary + "--" + Utils.LINE_SEPARATOR).getBytes());

			os.flush();
		} catch (IOException e) {
			throw new RuntimeException("Write files \"" + files + "\" to output stream error", e);
		}

//		if (isLast)
//			LogUtil.writeLog("boundary: " + boundary + ", output: " + output, HTTPUtils.class);

		return output;
	}

	/**
	 * 입력스트림 -> 파일
	 */
	public static void generateFile(String filePath, InputStream input) throws RuntimeException {
		LogUtil.writeLog("filePath: " + filePath, HTTPUtils.class);

		if (!Utils.isValidate(filePath) || input == null)
			return;

		OutputStream os = null;
		try {
			os = writeInputStream(new FileOutputStream(filePath), input);
		} catch (IOException e) {
			throw new RuntimeException("Write file \"" + filePath + "\" from input stream error", e);
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				throw new RuntimeException("Close output stream error", e);
			}
		}

		setFilePerm(filePath);
	}

	/**
	 * 바이트스트림 -> 파일
	 */
	public static void generateFile(String filePath, byte[] bytes) throws RuntimeException {
		LogUtil.writeLog("filePath: " + filePath, HTTPUtils.class);

		if (!Utils.isValidate(bytes))
			return;

		OutputStream os = null;
		try {
			os = writeBytes(new FileOutputStream(filePath), bytes);
		} catch (IOException e) {
			throw new RuntimeException("Write bytes to file \"" + filePath + "\" error", e);
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				throw new RuntimeException("Close output stream error", e);
			}
		}

		setFilePerm(filePath);
	}

	public static void setFilePerm(String filePath) {
		setFilePerm(filePath, true, true);
	}

	/**
	 * 파일권한 추가
	 */
	public static void setFilePerm(String filePath, boolean read, boolean write) {
		File file = new File(filePath);
		file.setReadable(read, false);
		file.setWritable(write, false);
	}

	// 기본 /////////////////////////////////////////////////////////////////////////

	public static Map<String, Object> upload(String url, Map<String, byte[]> files) throws RuntimeException {
		return upload(url, null, files);
	}

	public static Map<String, Object> upload(String url, Map<String, String> params, Map<String, byte[]> files)
			throws RuntimeException {
		return upload(url, null, params, files);
	}

	public static Map<String, Object> upload(String url, String method, Map<String, String> params,
			Map<String, byte[]> files) throws RuntimeException {
		return upload(url, method, null, params, files);
	}

	public static Map<String, Object> upload(String url, String method, Map<String, String> headers,
			Map<String, String> params, Map<String, byte[]> files) throws RuntimeException {
		return upload(url, method, headers, params, files, null);
	}

	/**
	 * 업로드
	 *
	 * @return Map(header, content)
	 */
	public static Map<String, Object> upload(String url, String method, Map<String, String> headers,
			Map<String, String> params, Map<String, byte[]> files, String charset) throws RuntimeException {
		LogUtil.writeLog("*** Uploading...", HTTPUtils.class);

		Map<String, Object> response = new HashMap<String, Object>();
		String boundary = UUID.randomUUID().toString();
		HttpURLConnection conn = null;
		OutputStream os = null;
		try {
			conn = (HttpURLConnection) getURLConnection(url, method, headers, boundary, charset);
			os = writeParams(conn.getOutputStream(), boundary, params, false);
			os = writeFiles(conn.getOutputStream(), boundary, files);
			LogUtil.writeLog("response: " + conn.getResponseCode() + ", " + conn.getResponseMessage(), HTTPUtils.class);

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				response.put("header", conn.getHeaderFields());
				response.put("body", getFileBytes(conn.getInputStream()));
			}
			LogUtil.writeLog("result: " + response, HTTPUtils.class);
		} catch (IOException e) {
			throw new RuntimeException("Upload files \"" + files + "\" to connection \"" + conn + "\" error", e);
		} finally {
			if (conn != null)
				conn.disconnect();
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				throw new RuntimeException("Close output stream error", e);
			}
		}

		return response;
	}

	public static void download(String url, String... filePath) throws RuntimeException {
		download(url, new HashMap<String, String>(), filePath);
	}

	public static void download(String url, Map<String, String> params, String... filePath) throws RuntimeException {
		download(url, null, params, filePath);
	}

	public static void download(String url, String method, Map<String, String> params, String... filePath)
			throws RuntimeException {
		download(url, method, null, params, filePath);
	}

	public static void download(String url, String method, Map<String, String> headers, Map<String, String> params,
			String... filePath) throws RuntimeException {
		download(url, method, headers, params, null, filePath);
	}

	/**
	 * 다운로드
	 */
	public static void download(String url, String method, Map<String, String> headers, Map<String, String> params,
			String charset, String... filePaths) throws RuntimeException {
		LogUtil.writeLog("*** Downloading...", HTTPUtils.class);

		String boundary = UUID.randomUUID().toString();
		HttpURLConnection conn = null;
		OutputStream os = null;
		for (int i = 0; i < filePaths.length; i = i + 2) {
			try {
				conn = (HttpURLConnection) getURLConnection(url, method, headers, null, charset);
				params.put("file_name", (Utils.isValidate(filePaths[i + 1])) ? filePaths[i + 1] : "");
				os = writeParams(conn.getOutputStream(), boundary, params);
				LogUtil.writeLog("response: " + conn.getResponseCode() + ", " + conn.getResponseMessage(),
						HTTPUtils.class);

				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
					generateFile(filePaths[i], conn.getInputStream());
			} catch (IOException e) {
				throw new RuntimeException(
						"Download connection \"" + conn + "\" to files \"" + Utils.toString(filePaths) + "\" error", e);
			} finally {
				if (conn != null)
					conn.disconnect();
				try {
					if (os != null)
						os.close();
				} catch (IOException e) {
					throw new RuntimeException("Close output stream error", e);
				}
			}
		}
	}

	public static void main(String[] args) {
//		System.out.println("test");

//		System.out.println(convertListToMap("mode=1&file_name="));

//		String url = "https://appstoredev.dongwha-mh.com/NSG/upload"; // 개발
//		String url = "https://appstore.dongwha-mh.com/NSG/upload"; // 운영
//		Map<String, Object> result = upload(url, getFiles("test/test.zip", "test/test1.zip"));

//		String url = "http://roset02.keb.co.kr:80/keb/pi/sign100.nsf/0/492575a60038191449257c820014898c/$FILE/외환은행 준법지원 SMS기반 App실행 방안_v1.0_20140110.pptx";
//		String url = "https://m.dongwha-mh.com/Storage/GW/FileStorage/63/15909_안전표어 공모 양식.xlsx";
//		download(url, "test/15909_안전표어 공모 양식.xlsx", "15909_안전표어 공모 양식.xlsx");

//		String url = "http://localhost:8080/common/upload";
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("skoh", "테스트");
////		url = convertMapToURI(url, params); // GET
//		Map<String, Object> result = upload(url, "POST", params, getFiles("test/테스트.json"));

//		String url = "http://gw.nongshim.kr/ekp/upload/attach/brd/2014/06/26/1403762649085.1";
//		download(url, new String[] { "등대.jpg", null, "등대1.jpg", null });

//		String url = "http://localhost/openapi/login.crd?userid=bW9iaWdlbg==&userdomain=bm9uZ3NoaW0=&en_userpass_md5=e2ac6dbc3f59b7ac9b3943e50e51e21f&en_userpass_sha2=df34cbc326c92ce89d529d2df34e3db74aaa09696c710c78174ad143c3670de6";
//		Map<String, Object> result = callHttp(url);

//		System.out.println(result);
//		System.out.println(new String((byte[]) result.get("body")));

		System.out.println(encodeURL("가"));
		System.out.println(decodeURL("%EA%B0%80"));

		System.exit(0);

		if (args.length < 3) {
			System.out.println(
					"  <Usage>  : java HTTPUtils xmlfilename -option(u/lu/d/ld) localfilepath [remotefilepath] [localfilepath [remotefilepath]] ...");
			System.out.println("- Upload   : java HTTPUtils common -u ./test.zip ./test1.zip");
			System.out.println(
					"- Download : java HTTPUtils common -ld ./test.zip ./test.zip.mob ./test1.zip ./test1.zip.mob");
			return;
		}

		Map<String, String> xml = readXMLFile(args[0] + ".xml");

		String cookies = null;
		args[1] = args[1].toLowerCase();
		if (args[1].indexOf("l") >= 0) {
			Map<String, List<String>> header = (Map<String, List<String>>) getHeader(
					callHttp(xml.get("login_url"), convertListToMap(xml.get("login_params"))));
			cookies = ((List<String>) header.get("Set-Cookie")).get(0);
		}

		int startIdx = 2;
		String[] filePaths = new String[args.length - startIdx];
		System.arraycopy(args, startIdx, filePaths, 0, args.length - startIdx);

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Cookie", cookies);
		if (args[1].indexOf("u") >= 0) {
			upload(xml.get("upload_url"), "POST", headers, convertListToMap(xml.get("upload_params")),
					getFiles(filePaths));
		} else if (args[1].indexOf("d") >= 0) {
			download(xml.get("download_url"), "POST", headers, convertListToMap(xml.get("download_params")), filePaths);
		}
	}
}
package com.nemustech.common.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * HttpClientHelper
 * 
 * @version
 * @deprecated replaced by {@link com.nemustech.common.util.NetUtil} in the common.jar
 */
@Deprecated
public class HttpClientHelper {
	protected static Log log = LogFactory.getLog(HttpClientHelper.class);

	public static HttpClient getHttpClient() {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		httpclient.getParams().setParameter(HTTP.USER_AGENT, "Mozilla/4.0");
//        httpclient.getParams().setParameter(HTTP.CONTENT_ENCODING, "gzip, deflate");
		httpclient.getParams().setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);

		return httpclient;
	}

	/**
	 * Get local context with cookie store
	 * 
	 * @return HttpContext
	 */
	public static HttpContext getLocalContext() {
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		return localContext;
	}

	/**
	 * POST 방식으로 request를 보낸다.
	 * 
	 * @param destURL 대상 URL
	 * @param params POST로 보낼 파라미터맵
	 * @param context Cookie와 같은 부가 정보
	 * @return HttpResponse
	 */
	public static HttpResponse requestPost(String destURL, Map<String, String> params, HttpContext context) {
		ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();

		for (Entry<String, String> param : params.entrySet()) {
			list.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}

		return requestPost(destURL, list, context);
	}

	/**
	 * POST 방식으로 request를 보낸다.
	 * 
	 * @param destURL 대상 URL
	 * @param params POST로 보낼 key-value 형식의 파라미터 리스트
	 * @param context Cookie와 같은 부가 정보
	 * @return HttpResponse
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HttpResponse requestPost(String destURL, List params, HttpContext context) {
		HttpClient httpclient = getHttpClient();
		HttpPost httppost = new HttpPost(destURL);
		long startTime = 0;
		long endTime = 0;

		try {
			UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			reqEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");

			httppost.setEntity(reqEntity);
			startTime = System.currentTimeMillis();
			HttpResponse response = httpclient.execute(httppost, context);
			endTime = System.currentTimeMillis();

			return response;
		} catch (Exception e) {
			endTime = System.currentTimeMillis();
			log.warn("requestPost exception!", e);

			return null;
		} finally {
//            httpclient.getConnectionManager().shutdown();
			log.debug("### Http Running Time: " + (endTime - startTime) + "ms");
		}
	}

	public static String getResponseBodyAsText(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		String responseTxt = null;

		try {
			byte[] resBytes = IOHelper.readToEnd(entity.getContent());
			responseTxt = new String(resBytes, "utf-8");
		} catch (Exception e) {
			log.warn("Failed to parse json!", e);
			responseTxt = "";
		} finally {
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (IOException e) {
					entity = null;
				}
			}
		}

		log.debug("Response Text: \n" + responseTxt);

		return responseTxt;
	}

	/**
	 * String -> Unicode 문자열(16진수)
	 * 
	 * @param str
	 * @return
	 */
	public static String toUniStr(String str) {
		String uni = "";

		for (int i = 0; i < str.length(); i++) {
			char chr = str.charAt(i);
			String hex = Integer.toHexString(chr);
			uni += "\\u" + hex;
		}

		return uni;
	}

	/**
	 * Unicode 문자열(16진수) -> String
	 * 
	 * @param uni
	 * @return
	 */
	public static String toNonUniStr(String uni) {
		String str = "";

		StringTokenizer str1 = new StringTokenizer(uni, "\\u");

		while (str1.hasMoreTokens()) {
			String str2 = str1.nextToken();
			int i = Integer.parseInt(str2, 16);
			str += (char) i;
		}

		return str;
	}
}

package org.oh.common.util;

import java.io.OutputStream;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.PropertyConfigurator;
import org.oh.common.exception.CommonException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 푸시 유틸
 */
public abstract class PushUtils {
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
			LogUtil.writeLog(e, PushUtils.class);
		}
	}

	public static String trcode = "PUSH01";
	public static String RESULT_OK = "ok";
	public static String RESULT_FAIL = "fail";
	public static String RESPONSE_MESSAGE = "\"result\":\"{0}\",\"error_code\":\"{1}\",\"message\":\"{2}\"}";
	public static String RESPONSE_MESSAGE_USER = "\"user\":\"{0}\",\"response\":{1}}";

	protected static Log log = LogFactory.getLog(PushUtils.class);

	public static ArrayNode send(String url, String message, String[] users) {
		ArrayNode responses = JsonUtil2.arrayNode();
		for (int i = 0; i < users.length; i++) {
			try {
				JsonNode response = send(url, message, users[i]);
				responses.add(getResponseMessageUser(users[i], response));
			} catch (CommonException e) {
				log.error(e.getMessage(), e);
			}
		}
		log.debug("responses : " + responses);

		return responses;
	}

	/**
	 * 푸시를 보낸다.
	 */
	public static JsonNode send(String url, String userId, String message) throws CommonException {
		OutputStream out = null;
		try {
			url += "/ReceivePushLegacyMessageServlet";

			// Push Server 전송
			// 요청 : {"push_type":"message","push_target":"single","user_id":"AP490800","push_data":{"message":"2"}}
			// 응답 : {"message":"","result":"ok","error_code":""}
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("", getRequestMessage(userId, message).toString());
//			Map<String, Object> result = HTTPUtils.callHttp(url, "POST", params);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("", getRequestMessage(userId, message).toString()));
			Map<String, Object> result = HTTPUtil.callHttp(url, HttpPost.METHOD_NAME, params);

			return JsonUtil2.readValue(HTTPUtils.getContentString(result));
		} catch (Exception e) {
			String errorCode = trcode + CommonException.PREFIX_SYSTEM;
			throw new CommonException(errorCode, LogUtil.buildMessage("Send push data error", e.getMessage()), e);
		} finally {
			HTTPUtil.closeQuietly(out);
		}
	}

	public static JsonNode getRequestMessage(String userId, String message) {
		ObjectNode pushData = JsonNodeFactory.instance.objectNode();
		pushData.put("message", message);
		pushData.put("mail", "0");
		pushData.put("approval", "0");
		pushData.put("crm", "0");

		ObjectNode pushMessage = JsonNodeFactory.instance.objectNode();
		pushMessage.put("user_id", userId);
		pushMessage.put("push_type", "message");
		pushMessage.put("push_target", "single");
		pushMessage.put("push_data", pushData);

		return pushMessage;
	}

	public static JsonNode getResponseMessageUser(String user, JsonNode response) {
		return JsonUtil2
				.readValue("{" + new MessageFormat(RESPONSE_MESSAGE_USER).format(new Object[] { user, response }));
	}

	public static JsonNode getResponseMessage(String result, String errorCode, String message) {
		return JsonUtil2.readValue(
				"{" + new MessageFormat(RESPONSE_MESSAGE).format(new Object[] { result, errorCode, message }));
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("HOME", "C:/dev/workspace/workspace_common/HOME");
		PropertyConfigurator.configure(System.getProperty("HOME") + "/conf/server/log4j.properties");

//		LogUtil.writeLog(send("https://appstoredev.dongwha-mh.com/push", "D4000737",
//				"푸시 테스트:" + StringUtil.getDateTime()));
//		System.out
//				.println(send("https://appstore.dongwha-mh.com/push", "D4000737", "푸시 테스트:" + StringUtil.getDateTime()));

		LogUtil.writeLog(send("http://172.27.41.171:8110/push", "Nsystemadmin", "[AP01]결재문서가 도착했습니다."));
//		LogUtil.writeLog(send("http://172.26.250.140:8110/push", "Nsystemadmin", "[AP01]결재문서가 도착했습니다."));
//		LogUtil.writeLog(send("http://172.26.250.123:8110/push", "Nsystemadmin", "[AP01]결재문서가 도착했습니다."));
	}
}
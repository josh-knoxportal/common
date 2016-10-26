package org.oh.common.test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.AfterClass;
import org.junit.Test;
import org.oh.common.file.Files;
import org.oh.common.thread.HTTPUtilFileTask;
import org.oh.common.thread.HTTPUtilTask;
import org.oh.common.util.FileUtil;
import org.oh.common.util.HTTPUtil;
import org.oh.common.util.HTTPUtils;
import org.oh.common.util.JsonUtil2;
import org.oh.common.util.LogUtil;
import org.oh.common.util.ThreadUtils;
import org.oh.common.util.Utils;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * 단위 테스트
 * 
 * @author skoh
 */
public class TestAPI {
	protected static ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);

	protected Log log = LogFactory.getLog(getClass());
	protected StopWatch sw = new StopWatch("all");

	/**
	 * URL
	 */
	protected String url;

	/**
	 * 메소드
	 */
	protected String method;

	/**
	 * 입력 포맷(JSON, ...)
	 */
	protected String requestFormat;

	/**
	 * 출력 포맷(JSON, ...)
	 */
	protected String responseFormat;

	/**
	 * 저장 경로
	 */
	protected String saveDir;

	/**
	 * 저장 확장자
	 */
	protected String saveExt;

	/**
	 * 바디 변환
	 */
	protected boolean convertBody;

	/**
	 * 파일 읽기
	 * 
	 * @param filePath JSON 파일경로
	 * @return JsonNode
	 * @throws Exception
	 */
	public static JsonNode readFile(String filePath) throws Exception {
		return JsonUtil2.readFile(filePath);
	}

	@AfterClass
	public static void destroy() throws Exception {
		ThreadUtils.shutdownThreadPool();
	}

	@Test
	public void test() throws Exception {
		test(arrayNode);
	}

	protected void test(ArrayNode arrayNode) throws Exception {
		for (JsonNode jsonNode : arrayNode) {
			test(jsonNode);
			LogUtil.writeLog("==================================================");
		}
	}

	/**
	 * 테스트
	 * 
	 * @param 입력데이타
	 * @throws Exception
	 */
	protected void test(JsonNode data) throws Exception {
		sw.start();

		url = data.path("url").textValue();
		LogUtil.writeLog("url: " + url);
		method = data.path("method").textValue();
		LogUtil.writeLog("method: " + method);
		saveDir = data.path("saveDir").textValue();
		LogUtil.writeLog("saveDir: " + saveDir);
		saveExt = data.path("saveExt").textValue();
		LogUtil.writeLog("saveExt: " + saveExt);
		requestFormat = data.path("requestFormat").textValue();
		LogUtil.writeLog("requestFormat: " + requestFormat);
		responseFormat = data.path("responseFormat").textValue();
		LogUtil.writeLog("responseFormat: " + responseFormat);
		convertBody = data.path("convertBody").booleanValue();
		LogUtil.writeLog("convertBody: " + convertBody);

		List<Future<Object>> futureList = new ArrayList<Future<Object>>();
		for (JsonNode json : data.path("list")) {
			HTTPUtilTask task = test2(json);
			futureList = ThreadUtils.executeThread(futureList, task);
		}

		print(futureList);

		sw.stop();
		LogUtil.writeLog(sw.shortSummary());
	}

	/**
	 * 테스트
	 * 
	 * @param json 입력데이타
	 * @return HTTPUtilTask 테스트 쓰레드
	 * @throws Exception
	 */
	protected HTTPUtilTask test2(JsonNode json) throws Exception {
		// HTTP 헤더
		JsonNode jsonNode = json.path("headers");
		LogUtil.writeLog("headers: " + JsonUtil2.toStringPretty(jsonNode));
		List<NameValuePair> headers = (JsonUtil2.isValidate(jsonNode)) ? JsonUtil2.convertJsonToList(jsonNode) : null;

		// HTTP 파라미터
		jsonNode = json.path("params");
		LogUtil.writeLog("params: " + JsonUtil2.toStringPretty(jsonNode));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if ("JSON".equalsIgnoreCase(requestFormat)) {
//			headers.add(new BasicNameValuePair("Content-Type", "application/json;charset=UTF-8"));
			params.add(new BasicNameValuePair("", jsonNode.toString()));
		} else {
			Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> field = fields.next();
				params.add(new BasicNameValuePair(field.getKey(), field.getValue().asText()));
			}
		}

		// 서버 호출
		if ("File".equalsIgnoreCase(requestFormat)) {
			// 파일
			List<Files> attachs = new ArrayList<Files>();
			if (jsonNode.get("file") != null) {
				for (JsonNode file : jsonNode.get("file")) {
					attachs.add(new Files(FileUtil.getPath(file.textValue()), FileUtil.getName(file.textValue()),
							IOUtils.toByteArray(new FileInputStream(file.textValue()))));
				}
			}

			return new HTTPUtilFileTask(url, method, headers, params, attachs);
		} else {
			return new HTTPUtilTask(url, method, headers, params);
		}
	}

	/**
	 * 출력
	 * 
	 * @param futureList 결과 리스트
	 * @throws Exception
	 */
	protected void print(List<Future<Object>> futureList) throws Exception {
		for (Future<Object> future : futureList) {
			Map<String, Object> result = (Map) future.get();
			String body = "";

			// 파일로 저장
			if (Utils.isValidate(saveDir) && Utils.isValidate(saveExt)) {
				byte[] bytes = null;
				if (convertBody) {
					body = HTTPUtils.getBodyString(result);
					body = convertbody(body);
					bytes = body.getBytes(HTTPUtils.getCharset());
				} else {
					bytes = HTTPUtils.getBody(result);
				}

				HTTPUtils.generateFile(
						saveDir + "/" + Utils.formatCurrentDate(Utils.SDF_DATE_MILLI_TIME) + "." + saveExt, bytes);
				// 콘솔에 출력
			} else {
				body = HTTPUtils.getBodyString(result);
				if (convertBody) {
					body = convertbody(body);
				}

				if (Utils.isValidate(responseFormat)) {
					if ("JSON".equalsIgnoreCase(responseFormat)) {
						body = JsonUtil2.toStringPretty(body);
					}
				}
				LogUtil.writeLog("header: " + Utils.toString(HTTPUtil.getHeader(result)));
				LogUtil.writeLog("body: " + body);
			}
		}
	}

	/**
	 * 바디를 변환한다.
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	protected String convertbody(String body) throws Exception {
		return body;
	}

	public static void main(String[] args) throws Exception {
		new TestAPI().test(readFile("src/test/resources/json/sample_list_post.json"));
	}
}

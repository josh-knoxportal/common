package org.oh.common.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.AfterClass;
import org.oh.common.thread.HTTPUtilTask;
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
	 * 파일 읽기
	 * 
	 * @param filePath JSON 파일경로
	 * 
	 * @return JsonNode
	 * 
	 * @throws Exception
	 */
	public static JsonNode readFile(String filePath) throws Exception {
		return JsonUtil2.readFile(filePath);
	}

	@AfterClass
	public static void destroy() throws Exception {
		ThreadUtils.shutdownThreadPool();
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
	 * 
	 * @throws Exception
	 */
	protected void test(JsonNode data) throws Exception {
		sw.start();

		List<Future<Object>> futureList = new ArrayList<Future<Object>>();
		String url = data.path("url").textValue();
		String method = data.path("method").textValue();
		String saveDir = data.path("saveDir").textValue();
		String saveExt = data.path("saveExt").textValue();
		String requestFormat = data.path("requestFormat").textValue();
		String responseFormat = data.path("responseFormat").textValue();

		for (JsonNode json : data.path("list")) {
			HTTPUtilTask task = test(json, url, method, requestFormat);
			futureList = ThreadUtils.executeThread(futureList, task);
		}

		print(futureList, saveDir, saveExt, responseFormat);

		sw.stop();
		LogUtil.writeLog(sw.shortSummary());
	}

	/**
	 * 테스트
	 * 
	 * @param json 입력데이타
	 * @param url URL
	 * @param method 메소드
	 * @param requestFormat 입력포맷(JSON, ...)
	 * 
	 * @return HTTPUtilTask 테스트 쓰레드
	 * 
	 * @throws Exception
	 */
	protected HTTPUtilTask test(JsonNode json, String url, String method, String requestFormat) throws Exception {
		// HTTP 헤더
		JsonNode jsonNode = json.path("headers");
		List<NameValuePair> headers = (JsonUtil2.isValidate(jsonNode)) ? JsonUtil2.convertJsonToList(jsonNode) : null;

		// HTTP 파라미터
		jsonNode = json.path("params");
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
		return new HTTPUtilTask(url, method, headers, params);
	}

	/**
	 * 출력
	 * 
	 * @param futureList 결과 리스트
	 * @param saveDir 저장 경로
	 * @param saveExt 저장 확장자
	 * @param responseFormat 출력 포맷(JSON, ...)
	 * 
	 * @throws Exception
	 */
	protected void print(List<Future<Object>> futureList, String saveDir, String saveExt, String responseFormat)
			throws Exception {
		for (Future<Object> future : futureList) {
			Map<String, Object> result = (Map) future.get();

			// 파일로 저장
			if (Utils.isValidate(saveDir) && Utils.isValidate(saveExt)) {
				HTTPUtils.generateFile(
						saveDir + "/" + Utils.formatCurrentDate(Utils.SDF_DATE_MILLI_TIME) + "." + saveExt,
						(byte[]) result.get("content"));
				// 콘솔에 출력
			} else {
				String content = HTTPUtils.getContentString(result);
				if (Utils.isValidate(responseFormat)) {
					if ("JSON".equalsIgnoreCase(responseFormat)) {
						content = JsonUtil2.toStringPretty(content);
					}
				}
				LogUtil.writeLog("content: " + content);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new TestAPI().test(readFile("src/test/resources/json/sample_list_post.json"));
	}
}

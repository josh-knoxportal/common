package com.nemustech.adapter.http;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import com.nemustech.adapter.exception.AdapterException;
import com.nemustech.common.util.JsonUtil2;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * HTTP 기본 매퍼
 * : HTTP 서비스와 연계(Json 응답)
 */
public class HttpJsonMapper extends AbstractHttpMapper<JsonNode> {
	public HttpJsonMapper() {
		super();
	}

	public HttpJsonMapper(String trcode) {
		super(trcode);
	}

	public HttpJsonMapper(String trcode, String charset) {
		super(trcode, charset);
	}

	public HttpJsonMapper(String trcode, String charset, String... excludes) {
		super(trcode, charset, excludes);
	}

	public HttpJsonMapper(String trcode, String charset, List<NameValuePair> includes, String... excludes) {
		super(trcode, charset, includes, excludes);
	}

	@Override
	public JsonNode handleResponse(HttpResponse response) throws AdapterException {
		try {
			JsonNode resNode = JsonUtil2.readValue(getContent(response).asString());

			verifyResponse(resNode);

			return resNode.path("success");
		} catch (AdapterException e) {
			throw e;
		} catch (Exception e) {
			throw new AdapterException("HTTP" + AdapterException.PREFIX_SYSTEM + "02",
					"Mapping http request data error", e);
		}
	}

	@Override
	protected void verifyResponse(JsonNode response) throws AdapterException {
		// TODO HTTP 호출 결과에 대한 검증 로직 추가
	}
}
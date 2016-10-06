package org.oh.adapter.http;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
import org.oh.adapter.exception.AdapterException;
import org.oh.common.file.Files;

/**
 * HTTP 기본 매퍼
 * : HTTP 업로드 서비스와 연계(Response 응답)
 */
public class HttpUploadMapper extends HttpResponseMapper {
	protected List<Files> attaches = null;

	public HttpUploadMapper() {
		super();
	}

	public HttpUploadMapper(String trcode) {
		super(trcode);
	}

	public HttpUploadMapper(String trcode, String charset) {
		super(trcode, charset);
	}

	public HttpUploadMapper(String trcode, String charset, String... excludes) {
		super(trcode, charset, excludes);
	}

	public HttpUploadMapper(String trcode, String charset, List<NameValuePair> includes, String... excludes) {
		super(trcode, charset, includes, excludes);
	}

	public HttpUploadMapper(String trcode, List<Files> attaches, String charset, List<NameValuePair> includes,
			String... excludes) {
		super(trcode, charset, includes, excludes);
		this.attaches = attaches;
	}

	@Override
	public HttpRequestBase setRequestParam(HttpRequestBase request, Object paramObj) throws AdapterException {
		return bodyMultipart(request, paramObj, attaches);
	}

	@Override
	protected void verifyResponse(DefaultHttpResponse resHttp) throws AdapterException {
		// TODO HTTP 호출 결과에 대한 검증 로직 추가
	}
}
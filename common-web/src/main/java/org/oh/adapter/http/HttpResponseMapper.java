package org.oh.adapter.http;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.oh.adapter.exception.AdapterException;
import org.oh.common.util.HTTPUtil;

/**
 * HTTP 기본 매퍼
 * : HTTP 서비스와 연계(Response 응답)
 */
public class HttpResponseMapper extends AbstractHttpMapper<DefaultHttpResponse> {
	public HttpResponseMapper() {
		super();
	}

	public HttpResponseMapper(String trcode) {
		super(trcode);
	}

	public HttpResponseMapper(String trcode, String charset) {
		super(trcode, charset);
	}

	public HttpResponseMapper(String trcode, String charset, String... excludes) {
		super(trcode, charset, excludes);
	}

	public HttpResponseMapper(String trcode, String charset, List<NameValuePair> includes, String... excludes) {
		super(trcode, charset, includes, excludes);
	}

	@Override
	public DefaultHttpResponse handleResponse(HttpResponse response) throws AdapterException {
		InputStream contentStream = null;
		try {
			HttpEntity resEntity = response.getEntity();
			contentStream = resEntity.getContent();

			DefaultHttpResponse resHttp = new DefaultHttpResponse();
			resHttp.setLocale(response.getLocale());
			resHttp.setContent(HTTPUtil.toByteArray(contentStream));

			ContentType contentType = ContentType.getOrDefault(resEntity);
			resHttp.setMimeType(contentType.getMimeType());
			if (contentType.getCharset() != null)
				resHttp.setCharset(contentType.getCharset().toString());

			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				if (header == null)
					continue;

//				if ("Content-Type".equalsIgnoreCase(header.getName())) {
//					HeaderElement headerElement = header.getElements()[0];
//					if (headerElement != null) {
//						resHttp.setMimeType(headerElement.getName());
//						resHttp.setCharset(headerElement.getParameterByName("charset").getValue());
//					}
//				} else {
				for (HeaderElement headerElement : header.getElements()) {
					if (headerElement == null)
						continue;

					if ("JSESSIONID".equalsIgnoreCase(headerElement.getName())) {
						resHttp.setJsessionId(headerElement.getValue());
						break;
					}
				}
			}
//			}

			verifyResponse(resHttp);

			return resHttp;
		} catch (AdapterException e) {
			throw e;
		} catch (Exception e) {
			throw new AdapterException("HTTP" + AdapterException.PREFIX_SYSTEM + "03",
					"Mapping http response data  error", e);
		} finally {
			IOUtils.closeQuietly(contentStream);
		}
	}

	@Override
	protected void verifyResponse(DefaultHttpResponse resHttp) throws AdapterException {
		// TODO HTTP 호출 결과에 대한 검증 로직 추가
	}
}
package org.oh.adapter.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.params.HttpConnectionParamBean;
import org.apache.http.util.EntityUtils;
import org.oh.adapter.exception.AdapterException;
import org.oh.adapter.util.AdapterUtil;
import org.oh.common.download.Attachment;
import org.oh.common.util.PropertyUtils;
import org.oh.common.util.Utils;
import org.springframework.beans.DirectFieldAccessor;

public abstract class AbstractHttpMapper<T> implements IHttpMapper<T> {
	static {
		// 커넥션 타임아웃 설정
		HttpConnectionParamBean param = new HttpConnectionParamBean(new HTTPAdapterImpl().getHttpClient().getParams());
		if (Utils.isValidate(PropertyUtils.getInstance().getString("http.connection.timeout")))
			param.setConnectionTimeout(PropertyUtils.getInstance().getInt("http.connection.timeout") * 1000);
		if (Utils.isValidate(PropertyUtils.getInstance().getString("http.socket.timeout")))
			param.setSoTimeout(PropertyUtils.getInstance().getInt("http.socket.timeout") * 1000);
	}

	public static final String ERROR_MESSAGE = "레거시 오류메세지가 없습니다.";

	protected static Log log = LogFactory.getLog(AbstractHttpMapper.class);

	protected String trcode = null;
	protected Charset charset = null;
	protected List<NameValuePair> includes = null;
	protected String[] excludes = null;

	protected Form from = Form.form();

	public AbstractHttpMapper() {
		this(null);
	}

	public AbstractHttpMapper(String trcode) {
		this(trcode, null);
	}

	public AbstractHttpMapper(String trcode, String charset) {
		this(trcode, null, (String[]) null);
	}

	public AbstractHttpMapper(String trcode, String charset, String... excludes) {
		this(trcode, null, null, excludes);
	}

	public AbstractHttpMapper(String trcode, String charset, List<NameValuePair> includes, String... excludes) {
		this.trcode = trcode;
		this.charset = Charset.forName(Utils.isValidate(charset) ? charset : "UTF-8");
		this.includes = includes;
		this.excludes = excludes;
	}

	protected HttpRequestBase body(HttpRequestBase request, final HttpEntity entity) {

		if (request instanceof HttpEntityEnclosingRequest) {
			((HttpEntityEnclosingRequest) request).setEntity(entity);
		} else {
			throw new IllegalStateException(request.getMethod() + " request cannot enclose an entity");
		}

		return request;
	}

	/**
	 * Request 본문을 생성하여 전송을 위한 HttpRequest 객체 생성
	 * 
	 * @param request HttpRequest 객체
	 * @param formParams 본문 form 피라미터
	 * @param charset 본문에 적용할 Charset
	 * @return HttpRequest 객체
	 */
	protected HttpRequestBase bodyForm(HttpRequestBase request, final Iterable<? extends NameValuePair> formParams,
			final Charset charset) {
		return body(request, new UrlEncodedFormEntity(formParams, charset));
	}

	protected HttpRequestBase bodyForm(HttpRequestBase request, final Iterable<? extends NameValuePair> formParams) {
		return bodyForm(request, formParams, Charset.forName("UTF-8"));
	}

	protected HttpRequestBase bodyForm(HttpRequestBase request, final NameValuePair... formParams) {
		return bodyForm(request, Arrays.asList(formParams), Charset.forName("UTF-8"));
	}

	protected HttpRequestBase bodyString(HttpRequestBase request, final String s, final ContentType contentType) {
		return body(request, new StringEntity(s, contentType));
	}

	protected HttpRequestBase bodyFile(HttpRequestBase request, final File file, final ContentType contentType) {
		return body(request, new FileEntity(file, contentType));
	}

	protected HttpRequestBase bodyByteArray(HttpRequestBase request, final byte[] b) {
		return body(request, new ByteArrayEntity(b));
	}

	protected HttpRequestBase bodyStream(HttpRequestBase request, final InputStream instream) {
		return body(request, new InputStreamEntity(instream, -1));
	}

	protected Content getContent(final HttpResponse response) throws ClientProtocolException, IOException {
		log.debug("statusCode:" + response.getStatusLine().getStatusCode());
		StatusLine statusLine = response.getStatusLine();
		HttpEntity entity = response.getEntity();
		if (statusLine.getStatusCode() >= 300) {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
		if (entity != null) {
			return new Content(EntityUtils.toByteArray(entity), ContentType.getOrDefault(entity));
		} else {
			return Content.NO_CONTENT;
		}
	}

	/**
	 * Http Response 에 대한 검증
	 * 
	 * @param response 검증할 HttpResponse
	 * @throws HttpResponseException HttpResponse 검증에 대한 Exception.
	 */
	protected void validResponseStatus(final HttpResponse response) throws HttpResponseException {
		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() >= 300) {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

	protected String convertObjectToXmlString(Object obj, String parentName, String... excludes) {

		StringBuffer sb = new StringBuffer("<" + parentName + ">");

		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(obj);

		boolean isExclude = false;
		for (Field field : fields) {
			isExclude = false;
			for (String exclude : excludes) {
				if (field.getName().equalsIgnoreCase(exclude)) {
					isExclude = true;
					break;
				}
			}
			if (isExclude)
				continue;

			Object value = fieldAccessor.getPropertyValue(field.getName());
			sb.append("<" + field.getName() + ">");
			sb.append(value);
			sb.append("</" + field.getName() + ">");
		}

		sb.append("</" + parentName + ">");

		return sb.toString();
	}

	// 확장 ///

	/**
	 * POJO 파라미터 입력(POST)
	 */
	protected HttpRequestBase bodyForm(HttpRequestBase request, Object paramObj) throws AdapterException {
		return bodyForm(request, AdapterUtil.convertObjectToList(paramObj, includes, excludes), charset);
	}

	/**
	 * 파일 업로드(POST)
	 */
	protected HttpRequestBase bodyMultipart(HttpRequestBase request, Object paramObj, List<Attachment> attachs)
			throws AdapterException {
		List<NameValuePair> paramList = AdapterUtil.convertObjectToList(paramObj, includes, excludes);

		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, charset);
		try {
			for (NameValuePair param : paramList) {
				multipartEntity.addPart(param.getName(), new StringBody(param.getValue(), charset));
			}
		} catch (Exception e) {
			throw new AdapterException("", "Add multipart parameter \"" + paramList + "\" error", e);
		}

		for (Attachment attachment : attachs) {
			multipartEntity.addPart(attachment.getFileName(),
					new ByteArrayBody(attachment.getBytes(), attachment.getFileName()));
		}

		return body(request, multipartEntity);
	}

	@Override
	public HttpRequestBase setRequestParam(HttpRequestBase request, Object paramObj) throws AdapterException {
//		return request; // GET
		return bodyForm(request, paramObj); // POST
	}

	/**
	 * HTTP 호출 결과에 대한 검증
	 */
	protected abstract void verifyResponse(T response) throws AdapterException;
}

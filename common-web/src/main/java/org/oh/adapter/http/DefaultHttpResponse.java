package org.oh.adapter.http;

import java.util.Locale;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.oh.adapter.exception.AdapterException;
import org.oh.common.util.Utils;

/**
 * HTTP 응답메세지 모델
 */
public class DefaultHttpResponse {
	protected Locale locale = null;
	protected byte[] content = null;
	protected String mimeType = null;
	protected String charset = null;
	protected long contentLength = 0L;
	protected String jsessionId = null;

	public DefaultHttpResponse() {
	}

	public DefaultHttpResponse(byte[] content) {
		setContent(content);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public byte[] getContent() {
		return content;
	}

	public String getContentString() throws AdapterException {
		if (!Utils.isValidate(content))
			return "";

		try {
			return (Utils.isValidate(charset)) ? new String(content, charset) : new String(content);
		} catch (Exception e) {
			throw new AdapterException(AdapterException.ERROR, "Read content bytes error", e);
		}
	}

	public void setContent(byte[] content) {
		this.content = content;
		this.contentLength = content.length;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public long getContentLength() {
		return contentLength;
	}

	public String getJsessionId() {
		return jsessionId;
	}

	public void setJsessionId(String jsessionId) {
		this.jsessionId = jsessionId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "content");
	}
}
package com.nemustech.common.thread;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.http.NameValuePair;
import com.nemustech.common.util.HTTPUtil;
import com.nemustech.common.util.LogUtil;

public class HTTPUtilTask implements Callable<Object> {
	protected String url = null;
	protected String method = null;
	protected List<NameValuePair> headers = null;
	protected List<NameValuePair> params = null;
	protected String charset = null;

	public HTTPUtilTask(String url, String method, List<NameValuePair> headers, List<NameValuePair> params) {
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.params = params;
	}

	public HTTPUtilTask(String url, String method, List<NameValuePair> headers, List<NameValuePair> params,
			String charset) {
		this(url, method, headers, params);

		this.charset = charset;
	}

	@Override
	public Object call() throws Exception {
		Map<String, Object> result = null;
		try {
			result = HTTPUtil.callHttp(url, method, headers, params, charset);
		} catch (Exception e) {
			LogUtil.writeLog(toString(), e, getClass());
		}

		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
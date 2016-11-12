package com.nemustech.common.thread;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import com.nemustech.common.file.Files;
import com.nemustech.common.util.HTTPUtil;
import com.nemustech.common.util.LogUtil;

public class HTTPUtilFileTask extends HTTPUtilTask {
	protected List<Files> attachs = null;

	public HTTPUtilFileTask(String url, String method, List<NameValuePair> headers, List<NameValuePair> params,
			List<Files> attachs) {
		super(url, method, headers, params);

		this.attachs = attachs;
	}

	public HTTPUtilFileTask(String url, String method, List<NameValuePair> headers, List<NameValuePair> params,
			List<Files> attachs, String charset) {
		this(url, method, headers, params, attachs);

		this.charset = charset;
	}

	@Override
	public Object call() throws Exception {
		Map<String, Object> result = null;
		try {
			result = HTTPUtil.upload(url, method, headers, params, attachs, charset);
		} catch (Exception e) {
			LogUtil.writeLog(toString(), e, getClass());
		}

		return result;
	}
}
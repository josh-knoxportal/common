package org.oh.common.thread;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.oh.common.download.Attachment;
import org.oh.common.util.HTTPUtil;
import org.oh.common.util.LogUtil;

public class HTTPUtilFileTask extends HTTPUtilTask {
	protected List<Attachment> attachs = null;

	public HTTPUtilFileTask(String url, String method, List<NameValuePair> headers, List<NameValuePair> params,
			List<Attachment> attachs) {
		super(url, method, headers, params);

		this.attachs = attachs;
	}

	public HTTPUtilFileTask(String url, String method, List<NameValuePair> headers, List<NameValuePair> params,
			List<Attachment> attachs, String charset) {
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
package com.nemustech.web.file;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.nemustech.common.file.Files;
import com.nemustech.common.file.URLDownloader;
import com.nemustech.common.util.StringUtil;

/**
 * 파일URL 다운로드
 * 
 * <pre>
 * http://{ip}:{port}/{context}/{model}/down/fileurl 로 매핑된다.
 * 예) http://localhost:8050/common-web/sample2/down/fileurl?mode=1&file_name=테스트.xls&file_url=http://localhost:8050/common-web/sample2/fileid/?mode=1&file_name=%ED%85%8C%EC%8A%A4%ED%8A%B801.txt&file_id=20171207163414992BYIQF
 * </pre>
 */
@Component
public class FileurlDownloader extends AbstractDownloader {
	protected String charset = null;

	public FileurlDownloader() {
		super();
	}

	@Override
	public void download(String target, String uid, Map<String, Object> params) throws Exception {
		int index = Integer.parseInt(params.get("index").toString());
		HttpServletRequest request = (HttpServletRequest) params.get("HttpServletRequest");
		HttpServletResponse response = (HttpServletResponse) params.get("HttpServletResponse");
		String fileName = String.valueOf(params.get("file_name"));
		String fileUrl = request.getParameter("file_url");
		fileUrl = StringUtil.replace(fileUrl, "@", "&");
		String fileType = getMimeType(request, fileName);
		ByteArrayInputStream in = null;

		try {
			URLDownloader urlDownloader = new URLDownloader();
			Files files = urlDownloader.download(fileName, fileUrl, charset);
			in = new ByteArrayInputStream(files.getBytes());

			send(request, response, fileName, fileType, in, files.getSize(), index);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
}
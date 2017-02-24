package com.nemustech.web.file;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.nemustech.common.file.Files;
import com.nemustech.common.file.URLDownloader;
import com.nemustech.common.util.StringUtil;
import org.springframework.stereotype.Component;

/**
 * 파일URL 다운로드
 * : http://{context_root}/{context_path}/download/fileurl/? 로 매핑된다.
 * 예) http://127.0.0.1:8080/common/download/fileurl/?mode=1&file_name=테스트.xls&file_url=127.0.0.1:8090/context/test.xls
 */
@Component
public class FileurlDownloader extends FileDownloader implements Downloader {
	protected static Log log = LogFactory.getLog(FileurlDownloader.class);

	protected String charset = null;

	public FileurlDownloader() {
		super();
	}

	public void download(String target, String uid, Map<String, Object> params) throws Exception {
		log.info(String.format("========Start downloading from %s : %s ", target, uid));
		log.debug("params: " + params);

		int mode = 0;
		int fileStartPos = Integer.parseInt(params.get("index").toString());
		HttpServletRequest request = (HttpServletRequest) params.get("HttpServletRequest");
		HttpServletResponse response = (HttpServletResponse) params.get("HttpServletResponse");
		String fileName = String.valueOf(params.get("file_name"));
		String fileUrl = request.getParameter("file_url");
		fileUrl = StringUtil.replace(fileUrl, "@", "&");
		String fileType = getMimeType(request, fileName);
		ByteArrayInputStream in = null;

		try {
			mode = Integer.parseInt(params.get("mode").toString());
		} catch (Exception e) {
			mode = 0;
		}

		log.debug("uid: " + uid);
		log.debug("mode: " + mode);
		log.debug("file_name: " + fileName);
		log.debug("file_url: " + fileUrl);

		try {
			// URL에서 파일을 다운로드할 경우
			URLDownloader urlDownloader = new URLDownloader();
			Files files = urlDownloader.download(fileName, fileUrl, charset);
			in = new ByteArrayInputStream(files.getBytes());

			send(response, fileName, fileType, in, files.getFile_size(), fileStartPos);
		} finally {
			IOUtils.closeQuietly(in);
		}

		log.info("Sucess downloading from " + target + "=============");
	}
}
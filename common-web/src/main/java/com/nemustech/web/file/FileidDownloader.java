package com.nemustech.web.file;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nemustech.common.storage.FileStorage;

/**
 * 파일ID 다운로드
 * 
 * <pre>
 * http://{ip}:{port}/{context}/{model}/down/fileid 로 매핑된다.
 * 예) http://localhost:8050/common-web/sample2/down/fileid?mode=1&file_name=테스트01.txt&file_id=20171207163414992BYIQF
 * </pre>
 */
@Component
public class FileidDownloader extends AbstractDownloader {
	@Autowired
	protected FileStorage fileStorage;

	public FileidDownloader() {
		super();
	}

	@Override
	public void download(String target, String uid, Map<String, Object> params) throws Exception {
		int index = Integer.parseInt(params.get("index").toString());
		String fileName = String.valueOf(params.get("file_name"));
		HttpServletRequest request = (HttpServletRequest) params.get("HttpServletRequest");
		HttpServletResponse response = (HttpServletResponse) params.get("HttpServletResponse");
		String fileId = request.getParameter("file_id");
		String fileType = getMimeType(request, fileName);
		ByteArrayInputStream in = null;

		try {
			byte[] bytes = fileStorage.load(fileId);
			in = new ByteArrayInputStream(bytes);

			send(request, response, fileName, fileType, in, bytes.length, index);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
}

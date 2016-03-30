package org.oh.common.download;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.common.storage.StorageAccessor;
import org.oh.common.util.HTTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 파일ID 다운로드
 * : http://{context_root}/{context_path}/download/fileid/? 로 매핑된다.
 * 예) http://127.0.0.1:8080/common/download/fileid/?mode=1&file_name=테스트.xls&file_id=ZBWMO00000001371200140695
 */
@Component
public class FileidDownloader extends AbstractDownloader implements Downloader {
	protected static Log log = LogFactory.getLog(FileidDownloader.class);

	@Autowired
	protected StorageAccessor storageAccessor;

	public FileidDownloader() {
		super();
	}

	public void download(String target, String uid, Map<String, Object> params) throws Exception {
		log.info(String.format("========Start downloading from %s : %s ", target, uid));
		log.debug("params : " + params);

		int mode = 0;
		int fileStartPos = Integer.parseInt(params.get("index").toString());
		HttpServletRequest request = (HttpServletRequest) params.get("HttpServletRequest");
		HttpServletResponse response = (HttpServletResponse) params.get("HttpServletResponse");
		String fileName = String.valueOf(params.get("file_name"));
		String fileId = request.getParameter("file_id");
		String fileType = getMimeType(request, fileName);
		ByteArrayInputStream in = null;

		try {
			mode = Integer.parseInt(params.get("mode").toString());
		} catch (Exception e) {
			mode = 0;
		}

		log.debug("uid : " + uid);
		log.debug("mode : " + mode);
		log.debug("file_name : " + fileName);
		log.debug("file_id : " + fileId);

		try {
			// 스토래지에서 파일을 다운로드할 경우
			byte[] bytes = storageAccessor.load(fileId);
			log.debug("file_info : " + storageAccessor.getFileInfo(fileId));
			in = new ByteArrayInputStream(bytes);

			send(response, fileName, fileType, in, bytes.length, fileStartPos);
		} finally {
			HTTPUtil.closeQuietly(in);
		}

		log.info("Sucess downloading from " + target + "=============");
	}
}

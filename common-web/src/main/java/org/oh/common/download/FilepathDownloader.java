package org.oh.common.download;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.common.Constants;
import org.oh.common.helper.IOHelper;
import org.oh.common.util.PropertyUtils;
import org.oh.common.util.Utils;
import org.springframework.stereotype.Component;

/**
 * 파일경로 다운로드
 * : http://{ip}:{port}/{context_root}/{context_path}/download/filepath/? 로 매핑된다.
 * 예) http://127.0.0.1:8080/common/download/filepath/?mode=1&file_name=/filepath/테스트.xlsx
 */
@Component
public class FilepathDownloader extends AbstractDownloader implements Downloader {
	protected static Log log = LogFactory.getLog(FilepathDownloader.class);

	public FilepathDownloader() {
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
		String fileType = getMimeType(request, fileName);

		// 보안 적용("/" 또는 "." 로 시작하는 파일명은 사용 불가)
		if (Utils.isValidate(fileName) && fileName.matches("^[/.].*"))
			throw new Exception("Not absolute path \"" + fileName + "\" error");

		try {
			mode = Integer.parseInt(params.get("mode").toString());
		} catch (Exception e) {
			mode = 0;
		}

		log.debug("uid: " + uid);
		log.debug("mode: " + mode);
		log.debug("file_name: " + fileName);

		ByteArrayInputStream in = null;
		try {
			// 로컬에서 파일을 다운로드할 경우
			byte[] bytes = load(fileName);
			in = new ByteArrayInputStream(bytes);

			send(response, fileName, fileType, in, bytes.length, fileStartPos);
		} finally {
			IOUtils.closeQuietly(in);
		}

		log.info("Sucess downloading from " + target + "=============");
	}

	public byte[] load(String fileName) {
		log.debug("Start::load()");

		FileInputStream fin = null;
		byte[] bytes = null;

		try {
			// 파일 이름
			File file = new File(PropertyUtils.getInstance().getString(Constants.PROPERTY_DOWNLOAD_PATH)
					+ File.separator + fileName);
			log.debug("  > filePath: " + file.getAbsolutePath());

			fin = new FileInputStream(file);
			bytes = IOHelper.readToEnd(fin);
		} catch (IOException e) {
			log.error("Error load file", e);
		} finally {
			IOUtils.closeQuietly(fin);
		}

		log.debug("  > RV(bytes length): " + ((bytes == null) ? 0 : bytes.length));
		log.debug("End::load()");

		return bytes;
	}
}
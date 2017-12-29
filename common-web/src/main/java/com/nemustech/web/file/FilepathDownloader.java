package com.nemustech.web.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.nemustech.common.helper.IOHelper;
import com.nemustech.common.storage.LocalFileStorage;

/**
 * 파일경로 다운로드
 * 
 * <pre>
 * http://{ip}:{port}/{context}/{model}/down/filepath 로 매핑된다.
 * 예) http://localhost:8050/common-web/sample2/down/filepath?mode=1&file_name=2017/12/20171207163414992BYIQF.file
 * </pre>
 */
@Component
public class FilepathDownloader extends AbstractDownloader {
	public FilepathDownloader() {
		super();
	}

	@Override
	public void download(String target, String uid, Map<String, Object> params) throws Exception {
		int index = Integer.parseInt(params.get("index").toString());
		HttpServletRequest request = (HttpServletRequest) params.get("HttpServletRequest");
		HttpServletResponse response = (HttpServletResponse) params.get("HttpServletResponse");
		String fileName = String.valueOf(params.get("file_name"));
		String fileType = getMimeType(request, fileName);

		// 보안 적용("/" 또는 "." 로 시작하는 파일명은 사용 불가)
//		if (Utils.isValidate(fileName) && fileName.matches("^[/.].*"))
//			throw new Exception("Not absolute path \"" + fileName + "\" error");

		ByteArrayInputStream in = null;
		try {
			byte[] bytes = load(fileName);
			in = new ByteArrayInputStream(bytes);

			send(request, response, fileName, fileType, in, bytes.length, index);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public byte[] load(String fileName) {
		FileInputStream fin = null;
		byte[] bytes = null;

		try {
			// 파일 이름
			File file = new File(LocalFileStorage.getStorageRootPath()
					+ File.separator + fileName);
			log.debug("filePath: " + file.getAbsolutePath());

			fin = new FileInputStream(file);
			bytes = IOHelper.readToEnd(fin);
		} catch (IOException e) {
			log.error("Error load file", e);
		} finally {
			IOUtils.closeQuietly(fin);
		}

		return bytes;
	}
}
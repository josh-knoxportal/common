package org.oh.common.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.common.helper.IOHelper;
import org.oh.common.util.HTTPUtil;

/**
 * 로컬 파일을 읽는다.
 * 
 * @version 1.0.0
 *
 */
public class LocalFileDownloader implements AttachmentDownloader {
	protected static Log log = LogFactory.getLog(LocalFileDownloader.class);

	/**
	 * @param filePath 파일의 로컬 경로(절대 경로 권장)
	 */
	public Attachment download(String fileName, String filePath) throws Exception {
		log.trace("Start::download()");
		log.trace("  > fileName : " + fileName);
		log.trace("  > filePath : " + filePath);

		Attachment file = new Attachment(fileName, 0, null);
		FileInputStream fis = null;
		byte[] data = null;

		try {
			fileName = filePath + File.separator + file.getFileName();
			fis = new FileInputStream(fileName);
			data = IOHelper.readToEnd(fis);
			file.setBytes(data);
		} catch (IOException e) {
			log.error("IOException > ", e);
			log.trace("Throw IOException!");

			throw e;
		} finally {
			HTTPUtil.closeQuietly(fis);
		}

		file.setSize(data.length);

		log.trace("  > RV(file.size) : " + file.getSize());
		log.trace("End::download()");

		return file;
	}
}

package org.oh.common.download;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.common.helper.IOHelper;
import org.oh.common.util.HTTPUtil;

/**
 * URL 기반으로 되어 있는 첨부 파일을 download할 수 있는 downloader
 * 
 * 
 * @version 1.0.0
 * 
 */
public class URLDownloader implements AttachmentDownloader {
	protected static Log log = LogFactory.getLog(URLDownloader.class);

	/**
	 * 읽기 버퍼의 크기
	 */
	public static final int READ_BLOCK = 8192;

	public Attachment download(String fileName, String URL) throws Exception {
		log.info("Start::download()");
		log.trace("  > fileName : " + fileName);
		log.trace("  > URL : " + URL);

		Attachment attch = new Attachment();

		try {
			String[] httpTokens = URL.split("://", 2);
			String[] hostTokens = httpTokens[1].split("/", 2);
			String[] portTokens = hostTokens[0].split(":", 2);
			String host = hostTokens[0];
			int port = 80;

			if (portTokens.length == 2) {
				host = portTokens[0];
				port = Integer.parseInt(portTokens[1]);
			}

			URL url = new URL(httpTokens[0], host, port,
					"/" + URLEncoder.encode(hostTokens[1], "UTF-8").replaceAll("\\+", "%20").replaceAll("%2F", "/"));
			byte[] rawData = IOHelper.readToEnd(url.openStream());

			log.trace("Decoded URL : " + url.toString());
			log.trace("File Name : " + fileName);
			log.trace("File Size : " + rawData.length);

			attch.setName(fileName);
			attch.setSize(rawData.length);
			attch.setBytes(rawData);
			log.debug("  > RV(attch) : " + attch);
		} catch (MalformedURLException e) {
			log.error("MalformedURLException > ", e);
			log.trace("Throw MalformedURLException!");

			throw e;
		} catch (IOException e) {
			log.error("IOException > ", e);
			log.trace("Throw IOException!");

			throw e;
		} finally {
			log.info("End::download()");
		}

		return attch;
	}

	// 확장 ///

	/**
	 * SSL 지원
	 * 
	 * @param fileName
	 * @param URL
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public Attachment download(String fileName, String URL, String charset) throws Exception {
		log.info("Start::download()");
		log.trace("  > fileName : " + fileName);
		log.trace("  > URL : " + URL);

		byte[] rawData = (byte[]) HTTPUtil.callHttp(URL, null, null, null, charset).get("content");
		log.trace("Decoded URL : " + URL);
		log.trace("File Name : " + fileName);
		log.trace("File Size : " + rawData.length);

		Attachment attch = new Attachment();
		attch.setName(fileName);
		attch.setSize(rawData.length);
		attch.setBytes(rawData);
		log.debug("  > RV(attch) : " + attch);

		return attch;
	}
}

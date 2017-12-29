package com.nemustech.web.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nemustech.common.helper.IOHelper;

/**
 * Download 서비스에서 필요한 기능을 구현한 클래스.
 * 서버의 download 프로토콜 및 개발 가이드라인의 구현 방법을 확인한다.
 * 
 * @version 1.0.0
 */
public abstract class AbstractDownloader implements Downloader {
	/**
	 * 읽기 버퍼의 크기로 8KB
	 */
	public static final int BUFFER_SIZE = 8192;

//	protected static FileDownloader _INSTANCE = new FileDownloader();

	protected Log log = LogFactory.getLog(getClass());

	protected int bufSize = 0;

	/**
	 * Singleton 객체를 돌려 준다.
	 * 
	 * @return Singleton 객체
	 */
//	public static FileDownloader getInstance() {
//		return _INSTANCE;
//	}

	/**
	 * 파일의 확장자를 바탕으로 MIME(Multipurpose Internet Mail Extensions) 타입을 알아 낸다.
	 * 
	 * @param request HttpRequest 객체
	 * @param fileName MIME 확장자를 포함한 파일 이름
	 * @return 파일에 대한 MIME 타입
	 * @see <a href="http://ko.wikipedia.org/wiki/MIME">MIME type</a>
	 */
	public static String getMimeType(HttpServletRequest request, String fileName) {
		String mimetype = request.getSession().getServletContext().getMimeType(fileName);

		if (mimetype == null || mimetype.length() == 0) {
			return "application/octet-stream;";
		}

		else {
			return mimetype;
		}
	}

	/**
	 * 생성자
	 */
	public AbstractDownloader() {
		this(BUFFER_SIZE);
	}

	/**
	 * 생성자
	 * 
	 * @param bufSize Stream 방식으 data를 읽어서 전송할 때 읽기 버퍼의 크기
	 */
	public AbstractDownloader(int bufSize) {
		this.bufSize = bufSize;
	}

	/**
	 * FileChannel에서 data를 읽어서 HTTP로 전송한다.
	 * 
	 * @param request 요청한 HttpRequest
	 * @param response 전송할 HttpResponse
	 * @param name 파일 이름
	 * @param type 파일 Mime type
	 * @param ch data를 읽을 FileChannel
	 * @param start 전송 시작 위치
	 */
	public void send(HttpServletRequest request, HttpServletResponse response, String name, String type, FileChannel ch,
			long start) throws Exception {
		long size;

		try {
			size = ch.size() - start;

			if (size < 0) {
				size = 0;
			}
		} catch (IOException e) {
			size = 0;
		}

		send(request, response, name, type, ch, size, start);
	}

	/**
	 * FileChannel에서 data를 읽어서 HTTP로 전송한다.
	 * 
	 * @param request 요청한 HttpRequest
	 * @param response 전송할 HttpResponse
	 * @param name 파일 이름
	 * @param type 파일 Mime type
	 * @param ch data를 읽을 FileChannel
	 * @param size data 크기
	 * @param start 전송 시작 위치
	 */
	public void send(HttpServletRequest request, HttpServletResponse response, String name, String type, FileChannel ch,
			long size, long start) throws Exception {
		log.info("Start::send()");
		log.trace("  > response: " + response);
		log.trace("  > name: " + name);
		log.trace("  > type: " + type);
		log.trace("  > data(FileChannel): " + ch);
		log.trace("  > size: " + size);
		log.trace("  > start: " + start);

		WritableByteChannel outCh = null;

		setHeader(request, response, name, type, size);

		if (ch != null) {
			try {
				response.setHeader("Content-Length", "" + size);

				outCh = Channels.newChannel(response.getOutputStream());
				ch.transferTo(start, size, outCh);
			} catch (Exception e) {
				log.error("Send exception: " + e);
			} finally {
				IOUtils.closeQuietly(outCh);
			}
		}

		log.info("End::send()");
	}

	/**
	 * InputStream에서 data를 읽어서 HTTP로 전송한다.
	 * 
	 * @param request 요청한 HttpRequest
	 * @param response 전송할 HttpResponse
	 * @param name 파일 이름
	 * @param type 파일 Mime type
	 * @param in data를 읽을 InputStream
	 * @param start 전송 시작 위치
	 */
	public void send(HttpServletRequest request, HttpServletResponse response, String name, String type, InputStream in,
			long start) {
		log.info("Start::send()");
		log.trace("  > response: " + response);
		log.trace("  > name: " + name);
		log.trace("  > type: " + type);
		log.trace("  > data(InputStream): " + in);
		log.trace("  > start: " + start);

		int contentLength = 0;
		WritableByteChannel outCh = null;
		ReadableByteChannel byteCh = null;

		if (in != null) {
			try {
				ByteBuffer bb = ByteBuffer.allocate(bufSize);
				in.skip(start);
				byteCh = Channels.newChannel(in);

				while (byteCh.read(bb) != -1) {
					bb = IOHelper.resizeBuffer(bb); // get new buffer for read
				}

				contentLength = bb.position();
				bb.flip();

				response.setHeader("Content-Length", "" + contentLength);
				setHeader(request, response, name, type, contentLength);

				log.trace("Content-Length: " + contentLength);
				log.trace("ByteBuffer's info.: " + bb);

				outCh = Channels.newChannel(response.getOutputStream());
				outCh.write(bb);
				bb.clear();
			} catch (Exception e) {
				log.error("Send exception! Cause: ", e);
			} finally {
				IOUtils.closeQuietly(byteCh);
				IOUtils.closeQuietly(outCh);
			}
		}

		log.info("End::send()");
	}

	/**
	 * InputStream에서 data를 읽어서 HTTP로 전송한다.
	 * 
	 * @param request 요청한 HttpRequest
	 * @param response 전송할 HttpResponse
	 * @param name 파일 이름
	 * @param type 파일 Mime type
	 * @param in data를 읽을 InputStream
	 * @param size data 크기
	 * @param start 전송 시작 위치
	 */
	public void send(HttpServletRequest request, HttpServletResponse response, String name, String type, InputStream in,
			long size, long start) throws Exception {
		log.info("Start::send()");
		log.trace("  > response: " + response);
		log.trace("  > name: " + name);
		log.trace("  > type: " + type);
		log.trace("  > data(InputStream): " + in);
		log.trace("  > start: " + start);

		WritableByteChannel outCh = null;
		ReadableByteChannel byteCh = null;

		setHeader(request, response, name, type, size);
		response.setHeader("Content-Length", "" + size);

		if (in != null) {
			try {
				int i = 0;
				int readLen = 0;
				ByteBuffer bb = ByteBuffer.allocate(bufSize);
				in.skip(start);
				outCh = Channels.newChannel(response.getOutputStream());
				byteCh = Channels.newChannel(in);

				// TODO in.mark(size) 메소드로 가능한 지 검토 필요
				while ((i = byteCh.read(bb)) > 0) {
					readLen += i;
					bb.flip();

					if (size > readLen) {
						outCh.write(bb);
					}

					else {
						bb.limit(i);
						outCh.write(bb);
						break;
					}

					bb.clear();
				}
			} catch (Exception e) {
				log.error("Send exception! Cause: ", e);
			} finally {
				IOUtils.closeQuietly(byteCh);
				IOUtils.closeQuietly(outCh);
			}
		}

		log.info("End::send()");
	}

	protected void setHeader(HttpServletRequest request, HttpServletResponse response, String name, String type,
			long size) throws Exception {
		String userAgent = request.getHeader("User-Agent");
		boolean ie = userAgent.indexOf("MSIE") > -1;
		String fileName = null;
		if (ie) {
			fileName = URLEncoder.encode(name, "utf-8");
		} else {
			fileName = new String(name.getBytes("utf-8"), "iso-8859-1");
		}
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ";");

		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("file_size", String.valueOf(size));
		response.setHeader("file_name", name);
		response.setHeader("file_type", type);

		response.setContentType(type + "; charset=utf-8");
	}
}

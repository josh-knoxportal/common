package org.oh.web.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

/**
 * Request의 inputStream을 reload. (request body를 재사용할때)
 * 
 * @author skoh
 */
public class HttpServletRequestWrapper2 extends HttpServletRequestWrapper {
	protected ByteArrayInputStream bais = null;

	public HttpServletRequestWrapper2(HttpServletRequest request) throws IOException {
		super(request);

		byte[] bytes = new String("").getBytes();
		ServletInputStream sis = super.getInputStream();
		if (sis != null)
			bytes = IOUtils.toByteArray(sis);

		bais = new ByteArrayInputStream(bytes);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ServletInputStreamWrapper();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(bais));
	}

	protected class ServletInputStreamWrapper extends ServletInputStream {
		@Override
		public int read() throws IOException {
			return bais.read();
		}

		@Override
		public int read(byte b[], int off, int len) throws IOException {
			return bais.read(b, off, len);
		}

		@Override
		public long skip(long n) throws IOException {
			return bais.skip(n);
		}

		@Override
		public synchronized int available() {
			return bais.available();
		}

		@Override
		public void close() throws IOException {
			bais.close();
		}

		@Override
		public synchronized void mark(int readlimit) {
			bais.mark(readlimit);
		}

		@Override
		public synchronized void reset() throws IOException {
			bais.reset();
		}

		@Override
		public boolean markSupported() {
			return bais.markSupported();
		}

		// Servlet 3.1.0
//		@Override
//		public boolean isFinished() {
//			return true;
//		}
//
//		@Override
//		public boolean isReady() {
//			return true;
//		}
//
//		@Override
//		public void setReadListener(ReadListener readListener) {
//		}
	}
}
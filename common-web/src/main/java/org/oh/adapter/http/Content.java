package org.oh.adapter.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.entity.ContentType;

public class Content {
	public static final Content NO_CONTENT = new Content(new byte[] {}, ContentType.DEFAULT_BINARY);

	private final byte[] raw;
	private final ContentType type;

	public Content(final byte[] raw, final ContentType type) {
		this.raw = raw;
		this.type = type;
	}

	public ContentType getType() {
		return this.type;
	}

	public byte[] asBytes() {
		return this.raw.clone();
	}

	public String asString() {
		Charset charset = this.type.getCharset();
		if (charset == null) {
			charset = Charset.forName("UTF-8");
		}
		try {
			return new String(this.raw, charset.name());
		} catch (UnsupportedEncodingException ex) {
			return new String(this.raw);
		}
	}

	public InputStream asStream() {
		return new ByteArrayInputStream(this.raw);
	}

	@Override
	public String toString() {
		return asString();
	}
}

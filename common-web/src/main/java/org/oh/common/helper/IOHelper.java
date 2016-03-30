package org.oh.common.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.oh.common.util.HTTPUtil;

/**
 * Java NIO 기반으로 IO 작업을 수행할 수 있는 IO Wrapper
 * 
 * 
 * @see java.nio
 */
public class IOHelper {
	/**
	 * 읽기 버퍼의 크기
	 */
	public static final int READ_BLOCK = 8192;

	/**
	 * NIO기반으로 stream에서 모든 data를 읽어서 돌려 준다.
	 * 버퍼의 크기를 자동으로 늘려 가면서 data를 읽어 들인다.
	 * 
	 * @param is 읽은 stream
	 * @return 모든 data
	 * @throws IOException by {@code Channel.read()}
	 */
	public static byte[] readToEnd(InputStream is) throws IOException {
		// create channel for input stream
		ReadableByteChannel bc = Channels.newChannel(is);
		ByteBuffer bb = ByteBuffer.allocate(READ_BLOCK);

		while (bc.read(bb) != -1) {
			bb = resizeBuffer(bb); // get new buffer for read
		}

		byte[] result = new byte[bb.position()];
		bb.flip();
		bb.get(result);
		bb.clear();
		HTTPUtil.closeQuietly(bc);

		return result;
	}

	/**
	 * 버퍼의 크기를 자동으로 조정하면서 버퍼 단위로 data를 읽어 들인다.
	 * 
	 * @param in data를 읽을 버퍼
	 * @return data를 읽은 버퍼
	 */
	public static ByteBuffer resizeBuffer(ByteBuffer in) {
		ByteBuffer result = in;

		if (in.remaining() < READ_BLOCK) {
			// create new buffer
			result = ByteBuffer.allocate(in.capacity() * 2);
			// set limit to current position in buffer and set position to zero.
			in.flip();
			// put original buffer to new buffer
			result.put(in);
		}

		return result;
	}
}
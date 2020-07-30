package hitool.core.io;

import java.nio.ByteBuffer;

/**
 *  ByteBuffer和byte 数组相互转换
 */
public class ByteBufferUtils {

	public static ByteBuffer getByteBuffer(byte[] bytes) {
		return ByteBuffer.wrap(bytes);
	}

	public static byte[] getBytes(ByteBuffer buf) {
		// Retrieve all bytes in the buffer
		buf.clear();
		// Create a byte array
		byte[] bytes = new byte[buf.capacity()];
		// transfer bytes from this buffer into the given destination array
		buf.get(bytes, 0, bytes.length);
		return bytes;
	}

	public static byte[] getBytes(ByteBuffer buf, byte[] bytes) {
		// Retrieve all bytes in the buffer
		buf.clear();
		// transfer bytes from this buffer into the given destination array
		buf.get(bytes, 0, bytes.length);
		return bytes;
	}

}

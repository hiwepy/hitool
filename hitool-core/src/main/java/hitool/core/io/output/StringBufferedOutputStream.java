package hitool.core.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class StringBufferedOutputStream extends OutputStream {

	/**
	 * The output stream to be filtered.
	 */
	protected OutputStream output;
	protected StringBuffer buffer = new StringBuffer();

	public StringBufferedOutputStream(OutputStream output) {
		this.output = output;
	}

	@Override
	public void close() throws IOException {
		try {
			flush();
		} catch (IOException ignored) {
		}
		// 一定要手动close掉返回的OutputStream，然后再调用completePendingCommand方法，若不是按照这个顺序，则不对
		output.close();
	}

	@Override
	public void flush() throws IOException {

	}

	@Override
	public void write(int b) throws IOException {
		buffer.append(b);
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

}

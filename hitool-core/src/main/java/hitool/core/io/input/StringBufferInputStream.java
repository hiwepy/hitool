package hitool.core.io.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class StringBufferInputStream extends InputStream {

	/**
	 * The input stream to be filtered.
	 */
	protected Reader reader;
	protected String charset = null;
	protected StringBuffer buffer = new StringBuffer();

	public StringBufferInputStream(Reader input) throws IOException {
		this.reader = makeBuffered(input);
	}

	public StringBufferInputStream(InputStream input) throws IOException {
		this.reader = new InputStreamReader(input);
	}

	public StringBufferInputStream(InputStream input, String charset)
			throws IOException {
		this.reader = new InputStreamReader(input, charset);
	}

	@Override
	public void close() throws IOException {
		// 一定要手动close掉返回的OutputStream，然后再调用completePendingCommand方法，若不是按照这个顺序，则不对
		reader.close();
	}

	@Override
	public int read() throws IOException {
		int bt = reader.read();
		buffer.append((char) bt);
		return bt;
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public static BufferedReader makeBuffered(Reader in) {
		return (in instanceof BufferedReader) ? (BufferedReader) in
				: new BufferedReader(in);
	}

}

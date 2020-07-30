/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.io.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class StringBufferReader extends BufferedReader {

	protected StringBuffer buffer = new StringBuffer();

	public StringBufferReader(Reader input) {
		super(makeBuffered(input));
	}

	public StringBufferReader(Reader input, int sz) {
		super(makeBuffered(input), sz);
	}

	@Override
	public int read() throws IOException {
		int bt = super.read();
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

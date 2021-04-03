/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.io.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class StringBufferedWriter extends BufferedWriter {

	protected StringBuffer buffer = new StringBuffer();

	public StringBufferedWriter(Writer output) {
		super(makeBuffered(output));
	}

	public StringBufferedWriter(Writer output, int sz) {
		super(makeBuffered(output), sz);
	}

	@Override
	public void write(int b) throws IOException {
		buffer.append(b);
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public static BufferedWriter makeBuffered(Writer output) {
		return (output instanceof BufferedWriter) ? (BufferedWriter) output
				: new BufferedWriter(output);
	}

}

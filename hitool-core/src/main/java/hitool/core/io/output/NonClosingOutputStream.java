package hitool.core.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NonClosingOutputStream extends FilterOutputStream {

	public NonClosingOutputStream(OutputStream out) {
		super(out);
	}

	@Override
	public void write(byte[] b, int off, int let) throws IOException {
		// It is critical that we override this method for performance
		out.write(b, off, let);
	}

	@Override
	public void close() throws IOException {
	}
}
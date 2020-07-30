package hitool.core.io.input;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NonClosingInputStream extends FilterInputStream {

	public NonClosingInputStream(InputStream input) {
		super(input);
	}

	@Override
	public void close() throws IOException {
	}

}
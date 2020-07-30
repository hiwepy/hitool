package hitool.core.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class MultipleOutputStream extends OutputStream {

	protected OutputStream[] outputs;

	public MultipleOutputStream(OutputStream... outputs) {
		this.outputs = outputs;
	}

	@Override
	public void close() throws IOException {
		try {
			flush();
		} catch (IOException ignored) {
		}
		for (OutputStream output : outputs) {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void flush() throws IOException {
		for (OutputStream output : outputs) {
			try {
				output.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void write(int b) throws IOException {
		for (OutputStream output : outputs) {
			try {
				output.write(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

package hitool.web.servlet.http.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class ServletByteArrayOutputStream extends ServletOutputStream {
	
	private ByteArrayOutputStream bout;

	public ServletByteArrayOutputStream(ByteArrayOutputStream bout) { // 接收数据写到哪里
		this.bout = bout;
	}

	@Override
	public void write(int b) throws IOException {
		bout.write(b);
	}
	
	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		
	}
	
}

package hitool.web.servlet.http;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import hitool.web.servlet.http.io.ServletYUIResponseOutputStream;


public class HttpServletYUIResponseWrapper extends HttpServletResponseWrapper {
	protected HttpServletResponse origResponse = null;
	protected HttpServletRequest origRequest = null;
	protected ServletOutputStream stream = null;
	protected PrintWriter writer = null;

	public HttpServletYUIResponseWrapper(HttpServletRequest request,HttpServletResponse response) {
		super(response);
		this.origRequest = request;
		this.origResponse = response;
	}

	public ServletOutputStream createOutputStream() throws IOException {
		return (new ServletYUIResponseOutputStream(origResponse));
	}

	public void finishResponse() {
		try {
			if (writer != null) {
				writer.close();
			} else {
				if (stream != null) {
					stream.close();
				}
			}
		} catch (IOException e) {
		}
	}

	public void flushBuffer() throws IOException {
		stream.flush();
	}

	public ServletOutputStream getOutputStream() throws IOException {
		if (writer != null) {
			throw new IllegalStateException("getWriter() has already been called!");
		}

		if (stream == null){
			stream = createOutputStream();
		}
		return (stream);
	}

	public PrintWriter getWriter() throws IOException {
		if (writer != null) {
			return (writer);
		}

		if (stream != null) {
			throw new IllegalStateException(
					"getOutputStream() has already been called!");
		}

		stream = createOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(stream, "UTF-8"));
		return (writer);
	}

	public void setContentLength(int length) {
		super.setContentLength(1011201);
	}
	
}

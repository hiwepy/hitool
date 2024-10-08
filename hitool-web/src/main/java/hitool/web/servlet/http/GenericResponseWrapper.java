package hitool.web.servlet.http;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import hitool.web.servlet.http.io.FilterServletOutputStream;

public class GenericResponseWrapper extends HttpServletResponseWrapper {
	
	protected ByteArrayOutputStream output;
	protected int contentLength;
	protected String contentType;

	public GenericResponseWrapper(HttpServletResponse response) {
		super(response);
		output = new ByteArrayOutputStream();
	}

	public byte[] getData() throws UnsupportedEncodingException {
		byte[] ret = output.toByteArray();
		return ret;
	}
	@Override
	public ServletOutputStream getOutputStream() {
		return new FilterServletOutputStream(output);
	}

	@Override
	public PrintWriter getWriter() {
		return new PrintWriter(getOutputStream(), true);
	}

	@Override
	public void setContentLength(int length) {
		this.contentLength = length;
		super.setContentLength(length);
	}

	public int getContentLength() {
		return contentLength;
	}

	@Override
	public void setContentType(String type) {
		this.contentType = type;
		super.setContentType(type);
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	public static void main(String[] args) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] a = "你好！".getBytes();
		out.write(a, 0, a.length);
	}

}

package hitool.web.servlet.http;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

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

	public ServletOutputStream getOutputStream() {
		return new FilterServletOutputStream(output);
	}

	public PrintWriter getWriter() {
		return new PrintWriter(getOutputStream(), true);
	}

	public void setContentLength(int length) {
		this.contentLength = length;
		super.setContentLength(length);
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentType(String type) {
		this.contentType = type;
		super.setContentType(type);
	}

	public String getContentType() {
		return contentType;
	}

	public static void main(String[] args) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] a = "你好！".getBytes();
		out.write(a, 0, a.length);
	}

}

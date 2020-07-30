package hitool.core.web.servlet.http;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oreilly.servlet.ServletUtils;

import hitool.core.io.FilemimeUtils;
import hitool.core.web.multipart.MultipartHeader;
import hitool.core.web.multipart.response.MultipartResponse;

public class HttpServletCosResponseWrapper extends HttpServletResponseWrapper implements MultipartResponse {

	protected static Logger log = LoggerFactory.getLogger(HttpServletCosResponseWrapper.class);
	protected boolean attachment = true;
	// 采用COS提供的ServletUtils类完成文件下载
	// 在ServletUtils中一共提供了7个静态方法，可以实现不同场景的文件下载以及其它需求
	// 其中使用outputFile()可以下载本地的文件，使用outputURL()可以下载网络上的文件
	// ServletUtils.outputURL(new URL("http://29.duote.org/javadmscgj.exe"),
	// response.getOutputStream());
	public HttpServletCosResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	public HttpServletCosResponseWrapper(HttpServletResponse response,boolean attachment) {
		super(response);
		this.attachment = attachment;
	}
	
	@Override
	public void outputFile(String filepath) throws IOException {
		MultipartHeader header = new MultipartHeader(getHttpServletResponse(), filepath,this.isAttachment()).setHeader();
		ServletUtils.returnFile(filepath, header.getOutputStream());
	}

	@Override
	public void outputFile(URL url) throws IOException {
		MultipartHeader header = new MultipartHeader(getHttpServletResponse(), url.getPath(),this.isAttachment()).setHeader();
		ServletUtils.returnURL(url, header.getOutputStream());
	}

	/**
	 *  向浏览器响应，输出下载的URL
	 * @param filePath
	 * @param title
	 * @throws IOException
	 */
	@Override
	public void outputURL(String filePath,String title) throws IOException {
		HttpServletResponse response = getHttpServletResponse();
		response.setContentType("text/html;charset=utf-8");
		ServletOutputStream out = response.getOutputStream();
		out.println("<meta  http-equiv=\"Content-Type\"content=\"text/html;charset=utf-8\">");
		out.println("<html><head><title>");
		out.println(title);
		out.println("</title>");
		out.println("</head><body>");
		out.print("<a href=\"");
		out.print(filePath);
		out.print("\"  title=\"Download Path\" >Download path: ");
		out.print(filePath);
		out.print("</a>");
		out.println("</body></html>");
		out.flush();
		out.close();
	}

	@Override
	public void outputResult(String text,String contentType) {
		HttpServletResponse response = getHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			response.setContentType(contentType);
			out = response.getWriter();
			out.write(text);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
		}
	}

	@Override
	public void outputText(String text) {
		outputResult( text == null ? "" : text,FilemimeUtils.getExtensionMimeType(".text"));
	}

	@Override
	public void outputJSON(String text) {
		outputResult(text == null ? "" : text,FilemimeUtils.getExtensionMimeType(".json"));
	}

	@Override
	public void outputHTML(String text) {
		outputResult(text == null ? "" : text,FilemimeUtils.getExtensionMimeType(".html"));
	}

	@Override
	public void outputXML(String text) {
		outputResult( text == null ? "" : text,FilemimeUtils.getExtensionMimeType(".xml"));
	}

	@Override
	public void outputBytes(byte[] bytes,String mimetype) throws IOException {
		HttpServletResponse response = getHttpServletResponse();
		response.setContentType(mimetype);
		ServletOutputStream out = response.getOutputStream();
		BufferedOutputStream bo = new BufferedOutputStream(out, 1024);
		bo.write(bytes);
		bo.flush();
		bo.close();
		out.close();
	}

	@Override
	public void outputRedirect(String location) throws IOException   {
		getHttpServletResponse().sendRedirect(location);
	}

	@Override
	public void outputDispatcher(HttpServletRequest request,String path) throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, getResponse());
	}

	@Override
	public void outputFreemarker(HttpServletRequest request,String velocity)  throws ServletException, IOException  {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void outputVelocity(HttpServletRequest request,String velocity)  throws ServletException, IOException  {
		// TODO Auto-generated method stub

	}

	public boolean isAttachment() {
		return attachment;
	}

	protected  HttpServletResponse getHttpServletResponse(){
		return (HttpServletResponse)getResponse();
	}
	
}

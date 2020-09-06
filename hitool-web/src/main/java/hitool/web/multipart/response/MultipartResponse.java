package hitool.web.multipart.response;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface MultipartResponse {
	
	public void outputFile(String filename) throws IOException;
	
	public void outputFile(URL url)  throws IOException;
	
	public void outputURL(String filePath, String title) throws IOException;
	
	public void outputResult(String text,String contentType);
	
	public void outputText(String text);
	
	public void outputJSON(String text);
	
	public void outputHTML(String text);
	
	public void outputXML(String text);
	
	public void outputBytes(byte[] bytes,String mimetype) throws IOException;
	
	public void outputRedirect(String redirect) throws IOException ;
	
	public void outputDispatcher(HttpServletRequest request,String path)  throws ServletException, IOException ;
	
	public void outputFreemarker(HttpServletRequest request,String freemarker)  throws ServletException, IOException ;
	
	public void outputVelocity(HttpServletRequest request,String velocity)  throws ServletException, IOException ;

}




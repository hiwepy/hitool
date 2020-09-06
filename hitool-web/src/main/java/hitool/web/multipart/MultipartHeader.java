package hitool.web.multipart;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import hitool.core.io.FilemimeUtils;
import hitool.web.MultipartContentUtils;

public class MultipartHeader {
	
	private HttpServletResponse response;
	private String filepath;
	private String outFileName;
	private boolean attachment = true;
	
	public MultipartHeader(HttpServletResponse response,String filepath){
		this(response,filepath,null);
	}
	
	public MultipartHeader(HttpServletResponse response,String filepath,boolean attachment){
		this(response,filepath,null,attachment);
	}
	
	public MultipartHeader(HttpServletResponse response,String filepath,String outFileName){
		this(response,filepath,outFileName,true);
	}
	
	public MultipartHeader(HttpServletResponse response,String filepath,String outFileName,boolean attachment){
		this.response = response;
		this.filepath = filepath;
		this.outFileName = outFileName;
		this.attachment = attachment;
	}
	
	public MultipartHeader setHeader() throws UnsupportedEncodingException{
		String fileName = filepath.substring(filepath.lastIndexOf(File.separator)+1);
		String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
		outFileName = ((outFileName==null)||outFileName.trim()=="")?fileName:outFileName+"."+fileType;
		String contentType = FilemimeUtils.getFileMimeType(fileName);
        if(contentType != null)   {
        	response.setContentType(contentType);
        }else{
        	response.setContentType("application/octet-stream");
        }
        response.setHeader( "Content-Disposition", MultipartContentUtils.getContentDisposition(isAttachment(), outFileName));
        return this;
	} 
	
	public ServletOutputStream getOutputStream() throws IOException{
		return response.getOutputStream();
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getFilepath() {
		return filepath;
	}

	public String getOutFileName() {
		return outFileName;
	}

	public boolean isAttachment() {
		return attachment;
	}
	
	
}

package hitool.core.web;

import java.io.UnsupportedEncodingException;

public class MultipartContentUtils {
	
	/**
	 * 方法用途和描述: 获取内容描述
	 * @param name
	 * @return
	 * <br>attachment：附件
	 * <br>inline：内联（意指打开时在浏览器中打开）
	 * @throws UnsupportedEncodingException 
	 */
	public static String getContentDisposition(boolean attachment,String fileName) throws UnsupportedEncodingException {
		return (new StringBuilder(attachment?"attachment":"inline"+";filename=\"").append(new String(fileName.getBytes("UTF-8"),"ISO8859-1").toString()).append("\"")).toString();
	}

}




package hitool.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.web.multipart.Multipart;

public final class MultipartRequestUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(MultipartRequestUtils.class);

	/*
     * Part of HTTP content type header.
     */
    public static final String MULTIPART = "multipart/";
    
	/*
     * Test if current HttpServletRequest is a multipart request.
     */
    public static boolean isMultipartRequest(HttpServletRequest request) {
    	if (!"post".equals(request.getMethod().toLowerCase())) {
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        if (contentType.toLowerCase().startsWith(MULTIPART)) {
            return true;
        }
        return false;
    }
    
	public static boolean supportMultipart(){
		// detect multipart support: 
		boolean multipart = false;
        try {
            Class.forName("org.apache.commons.fileupload.servlet.ServletFileUpload");
            LOG.info("Using CommonsFileUpload to handle multipart http request.");
            multipart = true;
        }catch (ClassNotFoundException e) {
        	try {
                Class.forName("com.oreilly.servlet.MultipartRequest.MultipartRequest");
                LOG.info("Using COS MultipartRequest to handle multipart http request.");
                multipart = true;
            }catch (ClassNotFoundException e2) {
            	 multipart = false;
                LOG.info("COS MultipartRequest not found. Multipart http request can not be handled.");
            }
        }
        return multipart;
	}
	
	public static Multipart multipart(){
		// detect multipart support: 
        try {
            Class.forName("org.apache.commons.fileupload.servlet.ServletFileUpload");
            LOG.info("Using CommonsFileUpload to handle multipart http request.");
            return Multipart.FILEUPLOAD;
        }catch (ClassNotFoundException e) {
        	try {
                Class.forName("com.oreilly.servlet.MultipartRequest.MultipartRequest");
                LOG.info("Using COS MultipartRequest to handle multipart http request.");
                return Multipart.COS;
            }catch (ClassNotFoundException e2) {
                LOG.info("COS MultipartRequest not found. Multipart http request can not be handled.");
                return null;
            }
        }
	}
	

	public static void main(String[] args) {
		System.out.println(Multipart.FILEUPLOAD.name());
	}
}




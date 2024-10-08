package hitool.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MultipartRequestUtils {
	
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

}




package hitool.web.exception;

import java.io.IOException;

/*
 * This exception is thrown when multipart post data exceeds the value
 * given by the Content-Length header
 *
 * @deprecated Use the Commons FileUpload based multipart handler instead. This
 *             class will be removed after Struts 1.2.
 */
@SuppressWarnings("serial")
public class ContentLengthExceededException extends IOException {
    
    protected String message;
    
    public ContentLengthExceededException() {
        message = "The Content-Length has been exceeded for this request";
    }
    
    public ContentLengthExceededException(long contentLength) {
        message = "The Content-Length of " + contentLength + " bytes has been " +
            "exceeded";
    }
    
    public String getMessage() {
        return message;
    }
}
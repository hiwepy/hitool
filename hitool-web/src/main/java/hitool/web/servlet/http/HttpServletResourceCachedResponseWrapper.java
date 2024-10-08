 package hitool.web.servlet.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import hitool.web.servlet.http.io.ServletByteArrayOutputStream;

public class HttpServletResourceCachedResponseWrapper  extends HttpServletResponseWrapper{
    private ByteArrayOutputStream bout = new ByteArrayOutputStream();  //捕获输出的缓存
    private PrintWriter pw;
    private HttpServletResponse response;
    public HttpServletResourceCachedResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
    }
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletByteArrayOutputStream(bout);
    }
    @Override
    public PrintWriter getWriter() throws IOException {
        pw = new PrintWriter(new OutputStreamWriter(bout,this.response.getCharacterEncoding()));
        return pw;
    }
    
    public byte[] getBuffer(){
        try{
            if(pw!=null){
                pw.close();
            }
            return bout.toByteArray();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package hitool.web.servlet3;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hitool.web.servlet3.async.DefaultWriterAppender;

@SuppressWarnings("serial")
public class AsyncMessagePushServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Cache-Control", "private");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Connection", "Keep-Alive");
        response.setHeader("Proxy-Connection", "Keep-Alive");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
 
        writer.println("<!-- Comet is a programming technique that enables web servers to send data to the client without having any need for the client to request it. -->\n");
        writer.flush();
 
        if (!request.isAsyncSupported()) {
            // log.info("the servlet is not supported Async");
            return;
        }
 
        request.startAsync(request, response);
        if (request.isAsyncStarted()) {
            final AsyncContext asyncContext = request.getAsyncContext();
            asyncContext.setTimeout(1L * 60L * 1000L);// 60sec
 
            asyncContext.addListener(new AsyncListener() {
                public void onComplete(AsyncEvent event) throws IOException {
                    DefaultWriterAppender.ASYNC_CONTEXT_QUEUE.remove(asyncContext);
                }
 
                public void onTimeout(AsyncEvent event) throws IOException {
                    DefaultWriterAppender.ASYNC_CONTEXT_QUEUE.remove(asyncContext);
                }
 
                public void onError(AsyncEvent event) throws IOException {
                    DefaultWriterAppender.ASYNC_CONTEXT_QUEUE.remove(asyncContext);
                }
 
                public void onStartAsync(AsyncEvent event) throws IOException {
                }
            });
            DefaultWriterAppender.ASYNC_CONTEXT_QUEUE.add(asyncContext);
        } else {
            // log.error("the ruquest is not AsyncStarted !");
        }
	}

}

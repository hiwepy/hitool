 package hitool.web.servlet3.async;
  
 import java.io.Writer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.AsyncContext;
  
 /**
 * @author Gao Youbo
 * @since 2013-4-23
 * @description
 * @TODO
 */
 public abstract class WriterAppender {
     /**
      * 异步Servlet上下文队列
      */
     public static final Queue<AsyncContext> ASYNC_CONTEXT_QUEUE = new ConcurrentLinkedQueue<AsyncContext>();
  
     /**
      * AsyncContextQueue Writer
      */
     protected Writer writer = new AsyncContextQueueWriter(ASYNC_CONTEXT_QUEUE);
 }
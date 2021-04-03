 package hitool.web.servlet3.async;
  
 import java.io.IOException;
  
 /*
 * @author Gao Youbo
 * @since 2013-4-23
 * @description
 * @TODO
 */
 public class DefaultWriterAppender extends WriterAppender {
     public void write(String message) {
         try {
             writer.write(message);
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
     }
 }
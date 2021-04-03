 package hitool.web.servlet3.async;
 import java.io.IOException;
  
 /*
 * @author Gao Youbo
 * @since 2013-4-24
 * @description
 * @TODO
 */
 public class AsyncMessage {
	 
     private static WriterAppender wa = null;
  
     /*
      * push message
      * 
      * @param message
      */
     public static void push(String message) {
         if (wa == null) {
             wa = new DefaultWriterAppender();
         }
         try {
             wa.writer.write(messageEscape(message));
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
     }
  
     /*
      * 执行推送JS
      * 
      * @param script
      */
     public static void pushScript(String script) {
         if (wa == null) {
             wa = new DefaultWriterAppender();
         }
         try {
             wa.writer.write(script);
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
     }
  
     /*
      * @param message
      * @return
      */
     private static String messageEscape(String message) {
         return "<script type='text/javascript'>window.parent.pushMessage(\"" + message.replaceAll("\n", "").replaceAll("\r", "") + "\");</script>";
     }
 }
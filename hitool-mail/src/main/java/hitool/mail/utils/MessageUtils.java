package hitool.mail.utils;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MessageUtils {

	/*
	 * 将邮件内容生成eml文件
	 * @param message 邮件内容
	 */
	public static File write(Message message, File dir) throws MessagingException, FileNotFoundException, IOException {
		File output = new File(dir, MimeUtility.decodeText(message.getSubject()) + ".eml");
		message.writeTo(new FileOutputStream(output));
		return output;
	}

}

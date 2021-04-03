package hitool.mail.def;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.smtp.SMTPMessage;

import hitool.mail.JavaMailClientAdapter;
import hitool.mail.JavaMailKey;
import hitool.mail.conf.EmailBody;
import hitool.mail.utils.JavaMailUtils;

/*
 * Java邮件发送-内置对象实现
 */
public class JavaMailClientImpl extends JavaMailClientAdapter {
	
	protected Logger LOG = LoggerFactory.getLogger(JavaMailClientImpl.class);
	
	/*
	 * 以文本格式发送邮件
	 * @param email 待发送的邮件的信息
	 */
	@Override
	public boolean sendText(EmailBody email) throws Exception {
		try {
			sendMail(email, false);
			return true;
		} catch (MessagingException ex) {
			LOG.error(ex.getMessage());
		}
		return false;
	}
	
	/*
	  * 以HTML格式发送邮件  
	  * @param email 待发送的邮件信息  
	  */ 
	@Override
	public boolean sendMime(EmailBody email) throws Exception {
		try {
			// 发送邮件
			sendMail(email, true);
			return true;
		} catch (MessagingException ex) {
			LOG.error(ex.getMessage());
		}
		return false;
	}
	
	@Override
	public boolean sendMime(InputStream email) throws Exception{
		try {
			
			LOG.debug("准备邮件发送...");
			
			Properties props = getPropsProvider().props();
			
			LOG.debug("发件平台：" + props.getProperty(JavaMailKey.MAIL_HOST_DESC, "unknown"));
			
			// 创建Session实例对象
			Session session = JavaMailUtils.getSession(props);
			// 创建MimeMessage实例对象
			MimeMessage mimeMessage = null;
			try {
				mimeMessage = new MimeMessage(session, email);
			} catch (Exception ex) {
				throw new MessagingException("Could not parse raw MIME content", ex);
			}
			// 设置发送时间
			mimeMessage.setSentDate(new Date());
			// 发送邮件
			Transport.send(mimeMessage);

			LOG.debug("邮件发送成功！");
			return true;
		} catch (MessagingException ex) {
			LOG.error(ex.getMessage());
		}
		return false;
	}

	protected void sendMail(EmailBody email,boolean isHtml) throws Exception{
		
		LOG.debug("准备邮件发送...");
		
		Properties props = getPropsProvider().props();
		
		// 创建Session实例对象
		Session session = JavaMailUtils.getSession(props);
		// 创建MimeMessage实例对象
		MimeMessage mimeMessage = new SMTPMessage(session);
		if(LOG.isDebugEnabled()) {
			LOG.debug("发件平台：" + props.getProperty(JavaMailKey.MAIL_HOST_DESC, "unknown"));
		}
		// 设置邮件主题
		mimeMessage.setSubject(email.getSubject());
		// 设置发送时间
		mimeMessage.setSentDate(new Date());
		// 设置反垃圾邮件
		setAntispam(mimeMessage, email);
			
		if(LOG.isDebugEnabled()) {
			LOG.debug("发件人：" + email.getFrom().getName() + " <" + email.getFrom().getEmail() + ">");
		}
		
		// 设置发件人，此设置解决了大多数邮箱的垃圾拦截
		InternetAddress from = new InternetAddress("\"" + MimeUtility.encodeText(email.getFrom().getName()) + "\" <" + email.getFrom().getEmail() + ">");
		mimeMessage.setFrom(from);
		// 设置回复人(收件人回复此邮件时,默认收件人)
		mimeMessage.setReplyTo(new InternetAddress[] {from});
		// 要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
		if(email.getFrom().isNotification()){
			mimeMessage.setHeader(HEADER_DISPOSITION_NOTIFICATION_TO, email.getFrom().getEmail());
		}
		
		// 设置收件人的名片和地址
		if(email.getMailto() != null){
 			List<String> mailtoList = new ArrayList<String>();
 			for (String mailto : email.getMailto().keySet()) {
 				// 添加一个收件人
 				mailtoList.add("\"" + MimeUtility.encodeText(mailto) + "\" <" + email.getMailto().get(mailto) + ">");
 			}
 			mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(StringUtils.join(mailtoList, ",")));
 			
 			if(LOG.isDebugEnabled()) {
 				LOG.debug("收件人：" + StringUtils.join(mailtoList, ","));
 			}
 		}
		
		// 设置抄送人的名片和地址
 		if(email.getMailcc() != null){
			List<String> mailccList = new ArrayList<String>();
			for (String mailcc : email.getMailcc().keySet()) {
				// 添加一个抄送
				mailccList.add("\"" + MimeUtility.encodeText(mailcc) + "\" <" + email.getMailcc().get(mailcc) + ">");
			}
			mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(StringUtils.join(mailccList, ",")));
			
			if(LOG.isDebugEnabled()) {
				LOG.debug("抄送人：" + StringUtils.join(mailccList, ","));
			}
		}
		
		// 设置密送人的名片和地址
		if(email.getMailBcc() != null){
			List<String> mailBccList = new ArrayList<String>();
			for (String mailcc : email.getMailBcc().keySet()) {
				// 添加一个密送
				mailBccList.add("\"" + MimeUtility.encodeText(mailcc) + "\" <" + email.getMailBcc().get(mailcc) + ">");
			}
			mimeMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(StringUtils.join(mailBccList, ",")));
			
			if(LOG.isDebugEnabled()) {
				LOG.debug("密送人：" + StringUtils.join(mailBccList, ","));
			}
		}
		
		if (isHtml) {
			
			// 创建 用于组合文本和图片，"related"型的MimeMultipart对象  
			Multipart mainPart = new MimeMultipart("related");
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart mimePart = new MimeBodyPart();
			// 设置HTML内容和格式/编码方式
			mimePart.setContent(email.getContent(), "text/html; charset=utf-8");
			//将BodyPart加入到MimeMultipart对象中(可以加入多个BodyPart)
			mainPart.addBodyPart(mimePart);
			
			
			// 有嵌入图片
			if (email.getInlineMap() != null && !email.getInlineMap().isEmpty()) {
				for (String inlineFilename : email.getInlineMap().keySet()) {
					// 加入插图
					mainPart.addBodyPart(createImageMimeBodyPart(inlineFilename, email.getInlineMap().get(inlineFilename)));
				}
			}
			// 有附件
			if (email.getAttached() != null && !email.getAttached().isEmpty()) {
				for (String attachmentFilename : email.getAttached().keySet()) {
					// 加入附件
					mainPart.addBodyPart(createAttachmentBodyPart(attachmentFilename, email.getInlineMap().get(attachmentFilename)));
				}
			}
			
			// 将MiniMultipart对象设置为邮件内容
			mimeMessage.setContent(mainPart);
		}
		else {
			// 设置纯文本内容为邮件正文
			mimeMessage.setText(email.getContent());
		}
		// 发送邮件
		if (Boolean.parseBoolean(props.getProperty(JavaMailKey.MAIL_SMTP_AUTH, "false"))) {
			String user = props.getProperty(JavaMailKey.MAIL_SMTP_USER, props.getProperty(JavaMailKey.MAIL_USER));
	        String password = props.getProperty(JavaMailKey.MAIL_SMTP_PASSWORD, props.getProperty(JavaMailKey.MAIL_PASSWORD));
			Transport.send(mimeMessage, user, password);
		} else{
			Transport.send(mimeMessage);
		}
		email.setMessageID(mimeMessage.getMessageID());
		
		if(LOG.isDebugEnabled()) {
			LOG.debug("邮件发送成功！");
		}
	}
	
	// 添加内嵌图片
	protected MimeBodyPart createImageMimeBodyPart(String imageName, File path)
			throws MessagingException, UnsupportedEncodingException {
		DataSource fds = new FileDataSource(path);
		MimeBodyPart mbp = new MimeBodyPart();
		DataHandler dh = new DataHandler(fds);
		mbp.setDataHandler(dh);
		// 设置对应的资源文件的唯一标识符，即 MIME 协议对于邮件的结构组织格式中的 Content-ID 头字段；
		mbp.setHeader("Content-ID", imageName);
		mbp.setFileName(MimeUtility.encodeText(fds.getName()));
		return mbp;
	}
	
	protected MimeBodyPart createAttachmentBodyPart(String fileName, File path)
			throws MessagingException, UnsupportedEncodingException {
		DataSource fds = new FileDataSource(path);
		DataHandler dh = new DataHandler(fds);
		MimeBodyPart attch = new MimeBodyPart();
		attch.setDataHandler(dh);
		// 这里的方法调用和插入图片是不同的，使用MimeUtility.encodeWord()来解决附件名称的中文问题 
		attch.setFileName(MimeUtility.encodeWord(fileName));
		return attch;
	}
	
	@Override
	public void receive(String subject, String content, String sendTo) throws Exception {

	}
	
}

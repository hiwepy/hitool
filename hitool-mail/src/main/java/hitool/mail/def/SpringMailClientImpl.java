package hitool.mail.def;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;

import hitool.mail.JavaMailClientAdapter;
import hitool.mail.JavaMailKey;
import hitool.mail.conf.EmailBody;

/**
 * Java邮件发送-Spring实现
 */
public class SpringMailClientImpl extends JavaMailClientAdapter {

	protected JavaMailSenderImpl mailSender;
	protected Logger LOG = LoggerFactory.getLogger(SpringMailClientImpl.class);
	
	/**
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
	
	/**  
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
			
			// 创建MimeMessage实例对象
			MimeMessage mimeMessage = mailSender.createMimeMessage(email);
			// 设置utf-8或GBK编码，否则邮件会有乱码
			// 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用，
			// multipart模式 
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			// 设置发送时间
			helper.setSentDate(new Date());
			// 发送邮件
			mailSender.send(mimeMessage);

	        LOG.debug("邮件发送成功！");
	        
			return true;
		} catch (MessagingException ex) {
			LOG.error(ex.getMessage());
		}
		return false;
	}
	
	protected void sendMail(EmailBody email,boolean isHtml) throws Exception{
		
		LOG.debug("准备邮件发送...");
		
		if(getPropsProvider() != null && getPropsProvider().props() != null){
			
			Properties props = getPropsProvider().props();
			
			// SMTP发送邮件的服务器的IP和端口
			mailSender.setHost(props.getProperty(JavaMailKey.MAIL_HOST));
			mailSender.setPort(Integer.parseInt(props.getProperty(JavaMailKey.MAIL_PORT)));
			// 登陆SMTP邮件发送服务器的用户名和密码
			mailSender.setUsername(props.getProperty(JavaMailKey.MAIL_USER));
			mailSender.setPassword(props.getProperty(JavaMailKey.MAIL_PASSWORD));
			// 邮件会话属性配置 : http://www.websina.com/bugzero/kb/javamail-properties.html
			mailSender.setJavaMailProperties(props);
			
			LOG.debug("发件平台：" + props.getProperty(JavaMailKey.MAIL_HOST_DESC, "unknown"));
			
		}
		
		// 创建MimeMessage实例对象
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		MimeMessageHelper helper = null;
		if (isHtml) {
			// 设置utf-8或GBK编码，否则邮件会有乱码
			// 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用，
			// multipart模式 
			helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		}else{
			helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
		}
		
		// 设置邮件主题
		helper.setSubject(email.getSubject());
		// 设置发送时间
		helper.setSentDate(new Date());
		// 设置优先级(1:紧急 3:普通 5:低)
		helper.setPriority(Integer.parseInt(StringUtils.isNotEmpty(email.getPriority()) ? email.getPriority() : "3"));
		// 设置反垃圾邮件
		setAntispam(mimeMessage, email);
		
		LOG.debug("发件人：" + email.getFrom().getName() + " <" + email.getFrom().getEmail() + ">");
		
		// 设置发件人，此设置解决了大多数邮箱的垃圾拦截
		InternetAddress from = new InternetAddress("\"" + MimeUtility.encodeText(email.getFrom().getName()) + "\" <" + email.getFrom().getEmail() + ">");
		
		helper.setFrom(from);
		// 设置回复人(收件人回复此邮件时,默认收件人)
        helper.setReplyTo(from);
        // 要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
 		if(email.getFrom().isNotification()){
 			mimeMessage.setHeader(HEADER_DISPOSITION_NOTIFICATION_TO, email.getFrom().getEmail());
 		}
        
        // 设置收件人的名片和地址
 		if(!CollectionUtils.isEmpty(email.getMailto())){
 			List<String> mailtoList = new ArrayList<String>();
 			for (String mailto : email.getMailto().keySet()) {
 				// 添加一个收件人
 				mailtoList.add("\"" + MimeUtility.encodeText(mailto) + "\" <" + email.getMailto().get(mailto) + ">");
 			}
 			helper.setTo(InternetAddress.parse(StringUtils.join(mailtoList, ",")));
 			LOG.debug("收件人：" + StringUtils.join(mailtoList, ","));
 		}
     		
		// 设置抄送人的名片和地址
		if(!CollectionUtils.isEmpty(email.getMailcc())){
			List<String> mailccList = new ArrayList<String>();
			for (String mailcc : email.getMailcc().keySet()) {
				// 添加一个抄送人
				mailccList.add("\"" + MimeUtility.encodeText(mailcc) + "\" <" + email.getMailcc().get(mailcc) + ">");
			}
			helper.setCc(InternetAddress.parse(StringUtils.join(mailccList, ",")));
			LOG.debug("抄送人：" + StringUtils.join(mailccList, ","));
		}
		// 设置密送人的名片和地址
		if(!CollectionUtils.isEmpty(email.getMailBcc())){
			List<String> mailBccList = new ArrayList<String>();
			for (String mailcc : email.getMailBcc().keySet()) {
				// 添加一个密送人
				mailBccList.add("\"" + MimeUtility.encodeText(mailcc) + "\" <" + email.getMailBcc().get(mailcc) + ">");
			}
			helper.setBcc(InternetAddress.parse(StringUtils.join(mailBccList, ",")));
			LOG.debug("密送人：" + StringUtils.join(mailBccList, ","));
		}
		
		if(isHtml){
			
			// 邮件内容，注意加参数true，表示启用html格式;如果内容中包含类似"<img src=\"cid:aaa\"/>"格式，需要InlineMap中有cid对应的文件
			helper.setText(email.getContent(), true);
			
			// 有嵌入图片
			if (email.getInlineMap() != null && !email.getInlineMap().isEmpty()) {
				
				for (String inlineFilename : email.getInlineMap().keySet()) {
					// 加入插图
			        helper.addInline(inlineFilename, email.getInlineMap().get(inlineFilename));
				}
			}
			// 有附件
			if (email.getAttached() != null && !email.getAttached().isEmpty()) {
				for (String attachmentFilename : email.getAttached().keySet()) {
					// 加入附件
					// 这里的方法调用和插入图片是不同的，使用MimeUtility.encodeWord()来解决附件名称的中文问题 
					helper.addAttachment(MimeUtility.encodeText(attachmentFilename), email.getAttached().get(attachmentFilename));
				}
			}
			
		} else {
			helper.setText(email.getContent());
		}
        // 发送邮件
        mailSender.send(mimeMessage);
        email.setMessageID(mimeMessage.getMessageID());
        
        LOG.debug("邮件发送成功！");
        
	}
	
	@Override
	public void receive(String subject, String content, String sendTo) throws Exception {
		// TODO Auto-generated method stub

	}
	

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
	
}

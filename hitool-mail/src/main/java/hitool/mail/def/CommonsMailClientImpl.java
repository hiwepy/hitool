package hitool.mail.def;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.mail.JavaMailClientAdapter;
import hitool.mail.JavaMailKey;
import hitool.mail.conf.EmailBody;

/**
 * Java邮件发送-commons-email实现
 */
public class CommonsMailClientImpl extends JavaMailClientAdapter {

	protected Logger LOG = LoggerFactory.getLogger(JavaMailClientImpl.class);
	
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
	
protected void sendMail(EmailBody email,boolean isHtml) throws Exception{
		
		LOG.debug("准备邮件发送...");
		
		Email mailSender = null;
		
		if (isHtml) {
			
			//SimpleEmail email = new SimpleEmail();//创建简单邮件,不可附件、HTML文本等  
	        //MultiPartEmail  email = new MultiPartEmail();//创建能加附件的邮件,可多个、网络附件亦可  
	        /*ImageHtmlEmail:HTML文本的邮件、通过2代码转转内联图片, 
	          ImageHtmlEmail网上都是官网上例子而官网上例子比较模糊 
	          ImageHtmlEmail email=new ImageHtmlEmail();
	        */  
			//创建能加附件内容为HTML文本的邮件、HTML直接内联图片但必须用setHtmlMsg()传邮件内容 
			mailSender = new HtmlEmail();
		}else{
			mailSender = new SimpleEmail();
		}
		
		Properties props = getPropsProvider().props();
		
		// 设置邮件内容编码，防止乱码  
		mailSender.setCharset("UTF-8");
		// 设置邮件服务器主机名
		mailSender.setHostName(props.getProperty(JavaMailKey.MAIL_HOST));
		// SMTP邮件服务器默认端口
		mailSender.setSmtpPort(Integer.parseInt(props.getProperty(JavaMailKey.MAIL_PORT)));
		// 发送超时时间，默认2500
		mailSender.setSocketTimeout(Integer.parseInt(props.getProperty(JavaMailKey.MAIL_SMTP_TIMEOUT, "25000")));
		// 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
		mailSender.setDebug(Boolean.parseBoolean(props.getProperty(JavaMailKey.MAIL_DEBUG, "false")));
		// 是否使用 STARTTLS安全连接
		mailSender.setStartTLSEnabled(Boolean.parseBoolean(props.getProperty(JavaMailKey.MAIL_SMTP_STARTTLS_ENABLE, "false")));
		// 是否使用SSL加密  
		mailSender.setSSLOnConnect(Boolean.parseBoolean(props.getProperty(JavaMailKey.MAIL_SMTP_SSL_ENABLE, "false")));
		// 登录邮件服务器的用户名和密码（保证邮件服务器POP3/SMTP服务开启） 
		if (Boolean.parseBoolean(props.getProperty(JavaMailKey.MAIL_SMTP_AUTH, "false"))) {
			String user = props.getProperty(JavaMailKey.MAIL_SMTP_USER, props.getProperty(JavaMailKey.MAIL_USER));
	        String password = props.getProperty(JavaMailKey.MAIL_SMTP_PASSWORD, props.getProperty(JavaMailKey.MAIL_PASSWORD));
			mailSender.setAuthentication(user, password);
		}
		
		LOG.debug("发件平台：" + props.getProperty(JavaMailKey.MAIL_HOST_DESC, "unknown"));
		
		// 设置邮件主题
		mailSender.setSubject(email.getSubject());
		// 设置发送时间
		mailSender.setSentDate(new Date());
		// 设置反垃圾邮件
		//=============反垃圾邮件处理====================
		// 设置优先级(1:紧急 3:普通 5:低)
		mailSender.addHeader(HEADER_PRIORITY, email != null && StringUtils.isNotEmpty(email.getPriority()) ? email.getPriority() : "3");
		mailSender.addHeader(HEADER_MSMAIL_PRIORITY, "Normal");
		// 声明邮件地址和头信息披上outlook的马甲;
		mailSender.addHeader(HEADER_MAILER, "Microsoft Outlook Express 6.00.2900.2869");
		mailSender.addHeader(HEADER_MIMEOLE, "Produced By Microsoft MimeOLE V6.00.2900.2869");
		mailSender.addHeader(HEADER_DATE, format.format(new Date()));
		
		
		LOG.debug("发件人：" + email.getFrom().getName() + " <" + email.getFrom().getEmail() + ">");
		
		// 设置发件人，此设置解决了大多数邮箱的垃圾拦截
		mailSender.setFrom(email.getFrom().getEmail(), email.getFrom().getName());
		// 设置回复人(收件人回复此邮件时,默认收件人)
		mailSender.addReplyTo(email.getFrom().getEmail(), email.getFrom().getName());
		// 要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
		if(email.getFrom().isNotification()){
			mailSender.addHeader(HEADER_DISPOSITION_NOTIFICATION_TO, email.getFrom().getEmail());
		}
		
		// 设置收件人的名片和地址
		if(email.getMailto() != null){
 			List<String> mailtoList = new ArrayList<String>();
 			for (String mailto : email.getMailto().keySet()) {
 				// 添加一个收件人
 				mailtoList.add("\"" + MimeUtility.encodeText(mailto) + "\" <" + email.getMailto().get(mailto) + ">");
 			}
 			mailSender.setTo(Arrays.asList(InternetAddress.parse(StringUtils.join(mailtoList, ","))));
 			LOG.debug("收件人：" + StringUtils.join(mailtoList, ","));
 		}
 		
		// 设置抄送的名片和地址
		if(email.getMailcc() != null){
			List<String> mailccList = new ArrayList<String>();
			for (String mailcc : email.getMailcc().keySet()) {
				// 添加一个抄送
				mailccList.add("\"" + MimeUtility.encodeText(mailcc) + "\" <" + email.getMailcc().get(mailcc) + ">");
			}
			mailSender.setCc(Arrays.asList(InternetAddress.parse(StringUtils.join(mailccList, ","))));
			LOG.debug("抄送人：" + StringUtils.join(mailccList, ","));
		}
		
		// 设置密送的名片和地址
		if(email.getMailBcc() != null){
			List<String> mailBccList = new ArrayList<String>();
			for (String mailcc : email.getMailBcc().keySet()) {
				// 添加一个密送
				mailBccList.add("\"" + MimeUtility.encodeText(mailcc) + "\" <" + email.getMailBcc().get(mailcc) + ">");
			}
			mailSender.setBcc(Arrays.asList(InternetAddress.parse(StringUtils.join(mailBccList, ","))));
			LOG.debug("密送人：" + StringUtils.join(mailBccList, ","));
		}
		
		if (isHtml) {
			
	        /*
	         * HtmlEmail、ImageHtmlEmail有setHtmlMsg()方法，且可以直接内联图片,可网上都搞那么复杂说不行如
	         * <img src='http://www.apache.org/images/asf_logo_wide.gif' />本人测试新浪、搜狐、QQ邮箱等都能显示 
	         * 
	         * 如果使用setMsg()传邮件内容，则HtmlEmail内嵌图片的方法 
	         * URL url = new URL("http://www.jianlimuban.com/图片");
	         * String cid = email.embed(url, "名字");
	         * email.setHtmlMsg("<img src='cid:"+cid+"' />");
	         */  
			
			HtmlEmail htmlEmail = (HtmlEmail)mailSender;
			
			htmlEmail.setHtmlMsg(email.getContent());
			// set the alternative message
			htmlEmail.setTextMsg("Your email client does not support HTML messages");
			
			// 有附件
			if (email.getAttached() != null && !email.getAttached().isEmpty()) {
				for (String attachmentFilename : email.getAttached().keySet()) {
					// 加入附件
					htmlEmail.attach(email.getAttached().get(attachmentFilename));
				}
			}
			
		}
		else {
			// 设置纯文本内容为邮件正文
			mailSender.setMsg(email.getContent());
		}
		
		// 发送邮件
		mailSender.send();
		email.setMessageID(mailSender.getMimeMessage().getMessageID());
		
        LOG.debug("邮件发送成功！");
		
	}
	
	@Override
	public boolean sendMime(InputStream email) throws Exception {
		throw new RuntimeException("not suport .");
	}
	

	@Override
	public void receive(String subject, String content, String sendTo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	
}

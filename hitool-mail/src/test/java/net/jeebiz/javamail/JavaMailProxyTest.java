package net.jeebiz.javamail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * http://dennis-zane.iteye.com/blog/57493
 */
public class JavaMailProxyTest {

	/**
	 * 通过代理发送邮件
	 */
	public static void main(String[] args) throws Exception {
		// Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		// final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		
		/*
		 *  我们是没办法使用javamail通过一般的代理服务器发送邮件的，比如下面的代码是没有效果的：
		 */
		/*Properties props = System.getProperties();
		props.setProperty("proxySet","true");
        props.setProperty("ProxyHost","192.168.155.1");
        props.setProperty("ProxyPort","1080");*/
        //或者这样，也是没用
        /*Properties props = System.getProperties();
        props.setProperty("proxySet","true");
        props.setProperty("http.proxyHost","192.168.155.1");
        props.setProperty("http.proxyPort","808");*/

		/*
		 * 不过可以通过socks网关来访问外网的email服务器，当然，前提是你安装了一个socks服务器
		 * 此处使用ccproxy代理测试
		 */
		
		// 设置代理服务器
		Properties props = System.getProperties();
		props.setProperty("proxySet", "true");
		props.setProperty("socksProxyHost", "10.71.19.215");
		props.setProperty("socksProxyPort", "1080");
		props.setProperty("mail.smtp.host", "smtp.163.com");

		// props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		// props.setProperty("mail.smtp.socketFactory.fallback", "false");
		// props.setProperty("mail.smtp.port", "465");
		// props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		// props.put("mail.store.protocol", "pop3");
		// props.put("mail.transport.protocol", "smtp");
		final String username = "xxx@163.com";
		final String password = "xxx";

		// 使用验证
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		MimeMessage message = new MimeMessage(session);
		Address address = new InternetAddress("xxx@163.com");
		Address toAaddress = new InternetAddress("xxx@qq.com");

		message.setFrom(address);
		message.setRecipient(MimeMessage.RecipientType.TO, toAaddress);
		message.setSubject("测试");
		message.setText("test");
		message.setSentDate(new Date());

		Transport.send(message);
		System.out.println("邮件发送！");

	}

}

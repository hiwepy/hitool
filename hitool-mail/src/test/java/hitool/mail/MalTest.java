package hitool.mail;

import org.apache.commons.mail2.core.EmailException;
import org.apache.commons.mail2.jakarta.EmailAttachment;
import org.apache.commons.mail2.jakarta.HtmlEmail;
import org.apache.commons.mail2.jakarta.MultiPartEmail;
import org.apache.commons.mail2.jakarta.SimpleEmail;

import java.net.MalformedURLException;
import java.net.URL;


public class MalTest {
	public String send(){
		String result = null;
		SimpleEmail email = new SimpleEmail();
		//设置主机名称
		//email.setHostName(rc.read("send"));
		//设置用户名，密码
		//email.setAuthentication(rc.read("username"), rc.read("password"));
		//设置字符编码方式
		email.setCharset("GB2312");
		try {
			/*//设置发送源地址
			email.setFrom(from);
			//设置目标地址
			email.addTo(to);
			//设置主题
			email.setSubject(subject);
			//设置邮件内容
			email.setMsg(msg);*/
			//发送邮件
			email.send();
			result = "successful";
		} catch (Exception e) {
			e.printStackTrace();
			result = "not successful";
		}
		
		return result;
	}
	
	public String sendEnclosure() throws EmailException {
		String result = null;
		EmailAttachment emailattachment = new EmailAttachment();
           //设置附件路径
	       //emailattachment.setPath(file);
	       //System.out.println(path);
	       emailattachment.setDisposition(EmailAttachment.ATTACHMENT);
	       //附件描述
	       emailattachment.setDescription("This is Smile picture");
	       /* 
            * 设置附件的中文乱码问题，解决附件的中文名称 乱码问题 
            */  
           //this.getName().getBytes()使用的是系统缺省的编码处理,这里是GBK
           //emailattachment.setName("=?GBK?B?"+enc.encode(file.getBytes())+"?=");  
             
           //attachment.setName(this.getName()); //不处理字符集的话,发送的附件中文名称是乱码 
	       
	       
	       //创建一个email
	       MultiPartEmail multipartemail = new MultiPartEmail();
	       //设置主机名称
	       //multipartemail.setHostName(UserInfo.sendHost);
	       //设置字符编码
	       multipartemail.setCharset("GB2312");
	       //设置发送邮件目的地址
	       //multipartemail.addTo(to);
	       //设置发送又见源地址
	       // multipartemail.setFrom(from);
	       //设置用户名和密码
	       //multipartemail.setAuthentication(UserInfo.username, UserInfo.password);
	       //设置主题
	       //multipartemail.setSubject(subject);
	       //设置邮件内容
	       //multipartemail.setMsg(msg);
	       //添加附件
	       multipartemail.attach(emailattachment);
	       //发送邮件
	       multipartemail.send();

	       result = "The attachmentEmail send sucessful!!!";

	       return result;
	}
	
	public String sendHtml() throws EmailException, MalformedURLException{
		// 创建邮件信息
		String result = null;
		HtmlEmail email = new HtmlEmail();
		//email.setHostName(UserInfo.sendHost);
		//email.addTo(to);
		//email.setFrom(from);
		//email.setSubject(subject);
		// embed the image and get the content id
		URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
		String cid = email.embed(url, "Apache logo");
		// set the html message
		email.setHtmlMsg("<html>The apache logo - <img src=\"cid:"+cid+"\"></html>");
		// set the alternative message
		email.setTextMsg("Your email client does not support HTML messages");
		// send the email
		email.send();
		
		return result;
	}
}

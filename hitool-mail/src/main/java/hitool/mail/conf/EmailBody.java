package hitool.mail.conf;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class EmailBody implements Serializable {

	/**
	 * 邮件优先级(1:紧急 3:普通 5:低)
	 */
	protected String priority;
	/**
	 * 邮件主题
	 */
	protected String subject;
	/**
	 * 邮件内容,普通文本或者html
	 */
	protected String content;
	/**
	 * 邮件输入流,支持外部邮件内容
	 */
	protected InputStream eml;
	/**
	 * 发件人名称和邮箱
	 */
	protected EmailFrom from;
	/**
	 * 收件人名称和邮箱
	 */
	protected Map<String, String> mailto;
	/**
	 * 抄送人名称和邮箱
	 */
	protected Map<String, String> mailcc;
	/**
	 * 密送人名称和邮箱
	 */
	protected Map<String, String> mailBcc;
	/**
	 * 嵌入图片
	 */
	protected Map<String, File> inlineMap;
	/**
	 * 附件
	 */
	protected Map<String, File> attached;
	/**
	 * 邮件发生成功后响应的唯一ID
	 */
	protected String messageID;
	
	/**
	 * 
	 */
	public EmailBody() {
		this.mailto = new HashMap<String, String>();
		this.mailcc = new HashMap<String, String>();
		this.mailBcc = new HashMap<String, String>();
	}

	/**
	 * @param priority	: 邮件优先级(1:紧急 3:普通 5:低)
	 * @param subject 	: 邮件主题
	 * @param content	: 邮件内容,普通文本或者html
	 * @param from		: 发件人名称和邮箱
	 * @param mailto	: 收件人名称和邮箱
	 */
	public EmailBody(String priority, String subject, String content, EmailFrom from, Map<String, String> mailto) {
		this.priority 	= priority;
		this.subject 	= subject;
		this.content 	= content;
		this.from 		= from;
		this.mailto 	= mailto;
	}

	/**
	 * @param priority	: 邮件优先级(1:紧急 3:普通 5:低)
	 * @param subject 	: 邮件主题
	 * @param content	: 邮件内容,普通文本或者html
	 * @param from		: 发件人名称和邮箱
	 * @param mailto	: 收件人名称和邮箱
	 * @param mailcc	: 抄送人名称和邮箱
	 */
	public EmailBody(String priority, String subject, String content,
			EmailFrom from, Map<String, String> mailto, Map<String, String> mailcc) {
		this.priority = priority;
		this.subject = subject;
		this.content = content;
		this.from = from;
		this.mailto = mailto;
		this.mailcc = mailcc;
	}
	
	/**
	 * @param priority	: 邮件优先级(1:紧急 3:普通 5:低)
	 * @param from		: 发件人名称和邮箱
	 * @param mailto	: 收件人名称和邮箱
	 * @param mailcc	: 抄送人名称和邮箱
	 * @param mailBcc	: 秘送人名称和邮箱
	 * @param subject 	: 邮件主题
	 * @param contentBody: 邮件内容,普通文本或者html
	 */
	public EmailBody(String priority, String subject, String content,
			EmailFrom from, Map<String, String> mailto, Map<String, String> mailcc,Map<String, String> mailBcc) {
		this.priority = priority;
		this.subject = subject;
		this.content = content;
		this.from = from;
		this.mailto = mailto;
		this.mailcc = mailcc;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public EmailFrom getFrom() {
		return from;
	}

	public void setFrom(EmailFrom from) {
		this.from = from;
	}
	
	public void setFrom(String name, String email, boolean notification) {
		this.from = new EmailFrom(name, email, notification);
	}

	public Map<String, String> getMailto() {
		return mailto;
	}

	public void setMailto(Map<String, String> mailto) {
		this.mailto = mailto;
	}

	public void setMailto(String name, String email) {
		this.mailto.put(name, email);
	}
	
	public Map<String, String> getMailcc() {
		return mailcc;
	}

	public void setMailcc(Map<String, String> mailcc) {
		this.mailcc = mailcc;
	}
	
	public void setMailcc(String name, String email) {
		this.mailcc.put(name, email);
	}

	public Map<String, String> getMailBcc() {
		return mailBcc;
	}

	public void setMailBcc(Map<String, String> mailBcc) {
		this.mailBcc = mailBcc;
	}
	
	public void setMailBcc(String name, String email) {
		this.mailBcc.put(name, email);
	}

	public Map<String, File> getInlineMap() {
		return inlineMap;
	}

	public void setInlineMap(Map<String, File> inlineMap) {
		this.inlineMap = inlineMap;
	}

	public Map<String, File> getAttached() {
		return attached;
	}

	public void setAttached(Map<String, File> attached) {
		this.attached = attached;
	}

	public InputStream getEml() {
		return eml;
	}

	public void setEml(InputStream eml) {
		this.eml = eml;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	
	
}

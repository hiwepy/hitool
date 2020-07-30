/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package hitool.mail;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;

import hitool.mail.conf.EmailBody;
import hitool.mail.provider.EmailPropertiesProvider;

public abstract class JavaMailClientAdapter implements JavaMailClient {

	protected SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
	protected EmailPropertiesProvider propsProvider;
	
	@Override
	public boolean sendText(EmailBody email) throws Exception {
		return false;
	}

	@Override
	public boolean sendMime(EmailBody email) throws Exception {
		return false;
	}

	@Override
	public boolean sendMime(InputStream email) throws Exception {
		return false;
	}

	@Override
	public void receive(String subject, String content, String sendTo) throws Exception {

	}

	/**
	 * 设置反垃圾邮件
	 */
	public void setAntispam(Message message,EmailBody email) throws MessagingException {
		
		//=============反垃圾邮件处理====================
		// 设置优先级(1:紧急 3:普通 5:低)
		message.setHeader(HEADER_PRIORITY, email != null && StringUtils.isNotEmpty(email.getPriority()) ? email.getPriority() : "3");
		message.setHeader(HEADER_MSMAIL_PRIORITY, "Normal");
		// 声明邮件地址和头信息披上outlook的马甲;
		message.setHeader(HEADER_MAILER, "Microsoft Outlook Express 6.00.2900.2869");
		message.setHeader(HEADER_MIMEOLE, "Produced By Microsoft MimeOLE V6.00.2900.2869");
		message.setHeader(HEADER_DATE, format.format(new Date()));
		
	}

	@Override
	public EmailPropertiesProvider getPropsProvider() {
		return propsProvider;
	}

	public void setPropsProvider(EmailPropertiesProvider propsProvider) {
		this.propsProvider = propsProvider;
	}
	
}

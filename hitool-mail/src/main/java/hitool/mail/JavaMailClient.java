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

import hitool.mail.conf.EmailBody;
import hitool.mail.provider.EmailPropertiesProvider;

public interface JavaMailClient {

	public static final String HEADER_PRIORITY = "X-Priority";
	public static final String HEADER_MSMAIL_PRIORITY = "X-MSMail-Priority";
	public static final String HEADER_MAILER = "X-Mailer";
	public static final String HEADER_MIMEOLE = "X-MimeOLE";
	public static final String HEADER_DATE = "Date";
	public static final String HEADER_DISPOSITION_NOTIFICATION_TO = "Disposition-Notification-To";

	public EmailPropertiesProvider getPropsProvider();

	public boolean sendText(EmailBody email) throws Exception;

	public boolean sendMime(EmailBody email) throws Exception;

	public boolean sendMime(InputStream email) throws Exception;

	public void receive(String subject, String content, String sendTo) throws Exception;

}

/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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
package hitool.core.resources.property.holder;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.lang3.ObjectUtils;

public class MessageFormatHolder {
	
	private static final MessageFormat INVALID_MESSAGE_FORMAT = new MessageFormat("");

	protected static Logger LOG = LoggerFactory.getLogger(MessageFormatHolder.class);

	private boolean alwaysUseMessageFormat = false;

	private final Map<String, MessageFormat> messageFormatsPerMessage = new HashMap<String, MessageFormat>();

	/**
	 * Cache to hold already generated MessageFormats.
	 * This Map is keyed with the ResourceBundle, which holds a Map that is
	 * keyed with the message code, which in turn holds a Map that is keyed
	 * with the Locale and holds the MessageFormat values. This allows for
	 * very efficient hash lookups without concatenated keys.
	 * @see #getMessageFormat
	 */
	protected final ConcurrentMap<Properties, Map<String, MessageFormat>> cachedBundleMessageFormats = new ConcurrentHashMap<Properties, Map<String, MessageFormat>>();

	
	private volatile static MessageFormatHolder singleton;

	public static MessageFormatHolder getInstance() {
		if (singleton == null) {
			synchronized (MessageFormatHolder.class) {
				if (singleton == null) {
					singleton = new MessageFormatHolder();
				}
			}
		}
		return singleton;
	}
	
	private MessageFormatHolder() {
		
	}
	
	public MessageFormat getMessageFormat(Properties resource, String code) throws MissingResourceException {
		Map<String, MessageFormat> codeMap = this.cachedBundleMessageFormats.get(resource);
		if (codeMap != null) {
			MessageFormat result = codeMap.get(code);
			if (result != null) {
				return result;
			}
		}
		String msg = getStringOrNull(resource, code);
		if (msg != null) {
			if (codeMap == null) {
				codeMap = new HashMap<String, MessageFormat>();
				this.cachedBundleMessageFormats.put(resource, codeMap);
			}
			MessageFormat result = createMessageFormat(msg);
			codeMap.put(code, result);
			return result;
		}
		return null;
	}
	
	public void setAlwaysUseMessageFormat(boolean alwaysUseMessageFormat) {
		this.alwaysUseMessageFormat = alwaysUseMessageFormat;
	}

	public boolean isAlwaysUseMessageFormat() {
		return this.alwaysUseMessageFormat;
	}

	public String renderDefaultMessage(String defaultMessage, Object[] args) {
		return formatMessage(defaultMessage, args);
	}

	public String formatMessage(String msg, Object[] args) {
		if (msg == null || (!this.alwaysUseMessageFormat && ObjectUtils.isEmpty(args))) {
			return msg;
		}
		MessageFormat messageFormat = null;
		synchronized (this.messageFormatsPerMessage) {
			messageFormat = this.messageFormatsPerMessage.get(msg);
			if (messageFormat == null) {
				try {
					messageFormat = createMessageFormat(msg);
				}
				catch (IllegalArgumentException ex) {
					// invalid message format - probably not intended for formatting,
					// rather using a message structure with no arguments involved
					if (this.alwaysUseMessageFormat) {
						throw ex;
					}
					// silently proceed with raw message if format not enforced
					messageFormat = INVALID_MESSAGE_FORMAT;
				}
				messageFormatsPerMessage.put(msg, messageFormat);
			}
		}
		if (messageFormat == INVALID_MESSAGE_FORMAT) {
			return msg;
		}
		synchronized (messageFormat) {
			return messageFormat.format(resolveArguments(args));
		}
	}

	/**
	 * Create a MessageFormat for the given message and Locale.
	 * @param msg the message to create a MessageFormat for
	 * @param locale the Locale to create a MessageFormat for
	 * @return the MessageFormat instance
	 */
	public MessageFormat createMessageFormat(String msg) {
		return new MessageFormat((msg != null ? msg : ""));
	}

	/**
	 * Template method for resolving argument objects.
	 * <p>The default implementation simply returns the given argument array as-is.
	 * Can be overridden in subclasses in order to resolve special argument types.
	 * @param args the original argument array
	 * @param locale the Locale to resolve against
	 * @return the resolved argument array
	 */
	public Object[] resolveArguments(Object[] args) {
		return args;
	}
	
	protected String getStringOrNull(Properties resource, String key) {
		try {
			return resource.getProperty(key, "");
		}
		catch (MissingResourceException ex) {
			// Assume key not found
			// -> do NOT throw the exception to allow for checking parent message source.
			return null;
		}
	}
	
}

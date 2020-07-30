/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.io.resource;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import hitool.core.lang3.ObjectUtils;
import hitool.core.resources.NoSuchMessageException;

public abstract class AbstractMessageSource extends MessageSourceSupport implements MessageSource {
	
	protected MessageSource parentMessageSource;
	protected Properties commonMessages;
	protected boolean useCodeAsDefaultMessage = false;

	@Override
	public MessageSource getParentMessageSource() {
		return parentMessageSource;
	}
	
	@Override
	public void setParentMessageSource(MessageSource parent) {
		this.parentMessageSource = parent;
	}
	
	/**
	 * Specify locale-independent common messages, with the message code as key
	 * and the full message String (may contain argument placeholders) as value.
	 * <p>May also link to an externally defined Properties object, e.g. defined
	 * through a {@link org.springframework.beans.factory.config.PropertiesFactoryBean}.
	 */
	public void setCommonMessages(Properties commonMessages) {
		this.commonMessages = commonMessages;
	}

	/**
	 * Return a Properties object defining locale-independent common messages, if any.
	 */
	protected Properties getCommonMessages() {
		return this.commonMessages;
	}
	
	/**
	 * Set whether to use the message code as default message instead of
	 * throwing a NoSuchMessageException. Useful for development and debugging.
	 * Default is "false".
	 * <p>Note: In case of a MessageSourceResolvable with multiple codes
	 * (like a FieldError) and a MessageSource that has a parent MessageSource,
	 * do <i>not</i> activate "useCodeAsDefaultMessage" in the <i>parent</i>:
	 * Else, you'll get the first code returned as message by the parent,
	 * without attempts to check further codes.
	 * <p>To be able to work with "useCodeAsDefaultMessage" turned on in the parent,
	 * AbstractMessageSource and AbstractApplicationContext contain special checks
	 * to delegate to the internal {@link #getMessageInternal} method if available.
	 * In general, it is recommended to just use "useCodeAsDefaultMessage" during
	 * development and not rely on it in production in the first place, though.
	 * @see #getMessage(String, Object[], Locale)
	 * @see org.springframework.validation.FieldError
	 */
	public void setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage) {
		this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
	}

	/**
	 * Return whether to use the message code as default message instead of
	 * throwing a NoSuchMessageException. Useful for development and debugging.
	 * Default is "false".
	 * <p>Alternatively, consider overriding the {@link #getDefaultMessage}
	 * method to return a custom fallback message for an unresolvable code.
	 * @see #getDefaultMessage(String)
	 */
	protected boolean isUseCodeAsDefaultMessage() {
		return this.useCodeAsDefaultMessage;
	}

	@Override
	public String getMessage(String code, Object[] args) throws NoSuchMessageException {
		String msg = getMessageInternal(code, args);
		if (msg != null) {
			return msg;
		}
		String fallback = getDefaultMessage(code);
		if (fallback != null) {
			return fallback;
		}
		throw new NoSuchMessageException(code);
	}

	@Override
	public String getMessage(String code, Object[] args, String defaultMessage) {
		String msg = getMessageInternal(code, args);
		if (msg != null) {
			return msg;
		}
		if (defaultMessage == null) {
			String fallback = getDefaultMessage(code);
			if (fallback != null) {
				return fallback;
			}
		}
		return messageFormatHolder.renderDefaultMessage(defaultMessage, args);
	}
	
	protected String getMessageInternal(String code, Object[] args) {
		if (code == null) {
			return null;
		}
		
		Object[] argsToUse = args;
		
		if (!isAlwaysUseMessageFormat() && ObjectUtils.isEmpty(args)) {
			// Optimized resolution: no arguments to apply,
			// therefore no MessageFormat needs to be involved.
			// Note that the default implementation still uses MessageFormat;
			// this can be overridden in specific subclasses.
			String message = resolveCodeWithoutArguments(code);
			if (message != null) {
				return message;
			}
		}

		else {
			// Resolve arguments eagerly, for the case where the message
			// is defined in a parent MessageSource but resolvable arguments
			// are defined in the child MessageSource.
			argsToUse = resolveArguments(args);

			MessageFormat messageFormat = resolveCode(code);
			if (messageFormat != null) {
				synchronized (messageFormat) {
					return messageFormat.format(argsToUse);
				}
			}
		}

		// Check locale-independent common messages for the given message code.
		Properties commonMessages = getCommonMessages();
		if (commonMessages != null) {
			String commonMessage = commonMessages.getProperty(code);
			if (commonMessage != null) {
				return messageFormatHolder.formatMessage(commonMessage, args);
			}
		}

		// Not found -> check parent, if any.
		return getMessageFromParent(code, argsToUse);
	}

	/**
	 * Try to retrieve the given message from the parent MessageSource, if any.
	 * @param code the code to lookup up, such as 'calculator.noRateSet'
	 * @param args array of arguments that will be filled in for params
	 * within the message
	 * @param locale the Locale in which to do the lookup
	 * @return the resolved message, or {@code null} if not found
	 * @see #getParentMessageSource()
	 */
	protected String getMessageFromParent(String code, Object[] args) {
		MessageSource parent = getParentMessageSource();
		if (parent != null) {
			if (parent instanceof AbstractMessageSource) {
				// Call internal method to avoid getting the default code back
				// in case of "useCodeAsDefaultMessage" being activated.
				return ((AbstractMessageSource) parent).getMessageInternal(code, args);
			}
			else {
				// Check parent MessageSource, returning null if not found there.
				return parent.getMessage(code, args, null);
			}
		}
		// Not found in parent either.
		return null;
	}

	/**
	 * Return a fallback default message for the given code, if any.
	 * <p>Default is to return the code itself if "useCodeAsDefaultMessage" is activated,
	 * or return no fallback else. In case of no fallback, the caller will usually
	 * receive a NoSuchMessageException from {@code getMessage}.
	 * @param code the message code that we couldn't resolve
	 * and that we didn't receive an explicit default message for
	 * @return the default message to use, or {@code null} if none
	 * @see #setUseCodeAsDefaultMessage
	 */
	protected String getDefaultMessage(String code) {
		if (isUseCodeAsDefaultMessage()) {
			return code;
		}
		return null;
	}

	/**
	 * Searches through the given array of objects, finds any MessageSourceResolvable
	 * objects and resolves them.
	 * <p>Allows for messages to have MessageSourceResolvables as arguments.
	 * @param args array of arguments for a message
	 * @param locale the locale to resolve through
	 * @return an array of arguments with any MessageSourceResolvables resolved
	 */
	@Override
	protected Object[] resolveArguments(Object[] args) {
		if (args == null) {
			return new Object[0];
		}
		List<Object> resolvedArgs = new ArrayList<Object>(args.length);
		for (Object arg : args) {
			resolvedArgs.add(arg);
		}
		return resolvedArgs.toArray(new Object[resolvedArgs.size()]);
	}

	protected String resolveCodeWithoutArguments(String code) {
		MessageFormat messageFormat = resolveCode(code);
		if (messageFormat != null) {
			synchronized (messageFormat) {
				return messageFormat.format(new Object[0]);
			}
		}
		return null;
	}

	/**
	 * Subclasses must implement this method to resolve a message.
	 * <p>Returns a MessageFormat instance rather than a message String,
	 * to allow for appropriate caching of MessageFormats in subclasses.
	 * <p><b>Subclasses are encouraged to provide optimized resolution
	 * for messages without arguments, not involving MessageFormat.</b>
	 * See the {@link #resolveCodeWithoutArguments} javadoc for details.
	 * @param code the code of the message to resolve
	 * @param locale the Locale to resolve the code for
	 * (subclasses are encouraged to support internationalization)
	 * @return the MessageFormat for the message, or {@code null} if not found
	 * @see #resolveCodeWithoutArguments(String, java.util.Locale)
	 */
	protected abstract MessageFormat resolveCode(String code);
	 
}

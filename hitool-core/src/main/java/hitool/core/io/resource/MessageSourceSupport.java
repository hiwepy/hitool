/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.io.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.resources.property.holder.MessageFormatHolder;

/**
 *  修改Spring的org.springframework.context.support.MessageSourceSupport去除Locale支持
 */
public abstract class MessageSourceSupport {

	protected static Logger LOG = LoggerFactory.getLogger(MessageSourceSupport.class);

	protected boolean alwaysUseMessageFormat = false;

	protected MessageFormatHolder messageFormatHolder = MessageFormatHolder.getInstance();
	
	public void setAlwaysUseMessageFormat(boolean alwaysUseMessageFormat) {
		this.alwaysUseMessageFormat = alwaysUseMessageFormat;
		messageFormatHolder.setAlwaysUseMessageFormat(alwaysUseMessageFormat);
	}

	protected boolean isAlwaysUseMessageFormat() {
		return this.alwaysUseMessageFormat;
	}

	/**
	 * Template method for resolving argument objects.
	 * <p>The default implementation simply returns the given argument array as-is.
	 * Can be overridden in subclasses in order to resolve special argument types.
	 * @param args the original argument array
	 * @param locale the Locale to resolve against
	 * @return the resolved argument array
	 */
	protected Object[] resolveArguments(Object[] args) {
		return args;
	}
	
}

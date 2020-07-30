/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.io.resource;

import hitool.core.resources.NoSuchMessageException;

/**
 */
public interface MessageSource{
	
	/**
	 * Set the parent that will be used to try to resolve messages
	 * that this object can't resolve.
	 * @param parent the parent MessageSource that will be used to
	 * resolve messages that this object can't resolve.
	 * May be {@code null}, in which case no further resolution is possible.
	 */
	void setParentMessageSource(MessageSource parent);

	/**
	 * Return the parent of this MessageSource, or {@code null} if none.
	 */
	MessageSource getParentMessageSource();

	String getMessage(String code, Object[] args, String defaultMessage);

	String getMessage(String code, Object[] args) throws NoSuchMessageException;

	
}

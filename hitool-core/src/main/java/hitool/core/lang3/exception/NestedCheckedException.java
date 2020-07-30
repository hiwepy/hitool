/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hitool.core.lang3.exception;

import java.text.MessageFormat;

/**
 * Helper class for implementing exception classes which are capable of
 * holding nested exceptions. Necessary because we can't share a base
 * class among different exception types.
 *
 * <p>Mainly for use within the framework.
 * <p>被检查的异常(Checked Exception),重写部分方法提高异常处理效率</p>
 * @author Juergen Hoeller
 * @modify 
 * @since 2.0
 * @see NestedRuntimeException
 * @see NestedCheckedException
 * @see NestedIOException
 * @see org.springframework.web.util.NestedServletException
 */
public abstract class NestedCheckedException extends Exception {
	
	/** Use serialVersionUID from Spring 1.2 for interoperability */
	private static final long serialVersionUID = 7100714597678207546L;

	static {
		// Eagerly load the NestedExceptionUtils class to avoid classloader deadlock
		// issues on OSGi when calling getMessage(). Reported by Don Brown; SPR-5607.
		NestedExceptionUtils.class.getName();
	}
	
	
	public NestedCheckedException() {
		fillInStackTrace();
	}

	/**
	 * Construct a {@code NestedCheckedException} with the specified detail message.
	 * @param msg the detail message
	 */
	public NestedCheckedException(String msg) {
		super(msg);
	}
	
	public NestedCheckedException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Construct a {@code NestedCheckedException} with the specified detail message
	 * and nested exception.
	 * @param msg the detail message
	 * @param cause the nested exception
	 */
	public NestedCheckedException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public NestedCheckedException(String message,Object... arguments){
		super(getText(message, arguments));
	}

	public NestedCheckedException(Throwable cause,String message,Object... arguments){
		super(getText(message, arguments), cause);
	}

	/**
	 * Return the detail message, including the message from the nested exception
	 * if there is one.
	 */
	@Override
	public String getMessage() {
		return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
	}

	/**
	 * Retrieve the innermost cause of this exception, if any.
	 * @return the innermost exception, or {@code null} if none
	 */
	public Throwable getRootCause() {
		Throwable rootCause = null;
		Throwable cause = getCause();
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

	/**
	 * Retrieve the most specific cause of this exception, that is,
	 * either the innermost cause (root cause) or this exception itself.
	 * <p>Differs from {@link #getRootCause()} in that it falls back
	 * to the present exception if there is no root cause.
	 * @return the most specific cause (never {@code null})
	 * @since 2.0.3
	 */
	public Throwable getMostSpecificCause() {
		Throwable rootCause = getRootCause();
		return (rootCause != null ? rootCause : this);
	}

	/**
	 * Check whether this exception contains an exception of the given type:
	 * either it is of the given class itself or it contains a nested cause
	 * of the given type.
	 * @param exType the exception type to look for
	 * @return whether there is a nested exception of the specified type
	 */
	public boolean contains(Class<?> exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof NestedCheckedException) {
			return ((NestedCheckedException) cause).contains(exType);
		}
		else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}

	protected static String getText(String message, Object... arguments){
		String value = (arguments == null || arguments.length == 0) ? message : MessageFormat.format(message, arguments);
		return (value == null) ? ""  : value;
	}

}
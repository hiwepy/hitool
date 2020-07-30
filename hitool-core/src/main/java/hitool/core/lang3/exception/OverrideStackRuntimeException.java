/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.exception;

import java.text.MessageFormat;

@SuppressWarnings("serial")
public abstract class OverrideStackRuntimeException extends RuntimeException {

	public OverrideStackRuntimeException() {
		fillInStackTrace();
	}

	public OverrideStackRuntimeException(String msg) {
		super(msg);
	}
	
	public OverrideStackRuntimeException(Throwable cause) {
		super(cause);
	}
	
	public OverrideStackRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public OverrideStackRuntimeException(String message,Object... arguments){
		super(getText(message, arguments));
	}

	public OverrideStackRuntimeException(Throwable cause,String message,Object... arguments){
		super(getText(message, arguments), cause);
	}

	/**
	 * <pre>
	 * 自定义改进的Exception对象 覆写了 fillInStackTrace方法
	 * 1. 不填充stack
	 * 2. 取消同步
	 * </pre>
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

	protected static String getText(String message, Object... arguments){
		String value = (arguments == null || arguments.length == 0) ? message : MessageFormat.format(message, arguments);
		return (value == null) ? ""  : value;
	}

}
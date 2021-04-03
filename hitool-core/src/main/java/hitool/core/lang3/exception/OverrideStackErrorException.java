/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.exception;

import java.text.MessageFormat;

/*
 *  系统错误(Error),重写部分方法提高异常处理效率
 */
@SuppressWarnings("serial")
public abstract class OverrideStackErrorException extends Error {

	public OverrideStackErrorException() {
		fillInStackTrace();
	}
	
	public OverrideStackErrorException(String message) {
		super(message);
	}
	
	public OverrideStackErrorException(Throwable cause) {
		super(cause);
	}
	
	public OverrideStackErrorException(String message,Throwable cause) {
		super(message, cause);
	}
	
	public OverrideStackErrorException(String message,Object... arguments){
		super(getText(message, arguments));
	}

	public OverrideStackErrorException(Throwable cause,String message,Object... arguments){
		super(getText(message, arguments), cause);
	}
	
	/*
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
/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.exception;

/**
 * 被检查的业务异常(Checked Exception)
 */
@SuppressWarnings("serial")
public class BusinessCheckException extends OverrideStackCheckedException {
	
	public BusinessCheckException() {
		super();
	}
	
	public BusinessCheckException(String message) {
		super(message);
	}
	
	public BusinessCheckException(Throwable cause) {
		super(cause);
	}
	
	public BusinessCheckException(String message,Throwable cause) {
		super(message, cause);
	}
	
	public BusinessCheckException(String message,Object... arguments){
		super(message, arguments);
	}

	public BusinessCheckException(Throwable cause,String message,Object... arguments){
		super(cause, message, arguments);
	}
	
}
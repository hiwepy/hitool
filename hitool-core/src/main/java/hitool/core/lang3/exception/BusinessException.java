/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.exception;


/**
 * 忽略检查的业务异常(Runtime Exception)
 */
@SuppressWarnings("serial")
public class BusinessException extends OverrideStackRuntimeException {
	
	public BusinessException() {
		super();
	}
	
	public BusinessException(String message) {
		super(message);
	}
	
	public BusinessException(Throwable cause) {
		super(cause);
	}
	
	public BusinessException(String message,Throwable cause) {
		super(message, cause);
	}
	
	public BusinessException(String message,Object... arguments){
		super(message, arguments);
	}

	public BusinessException(Throwable cause,String message,Object... arguments){
		super(cause, message, arguments);
	}

}

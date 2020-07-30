/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.exception;


/**
 * 服务层运行异常
 */
@SuppressWarnings("serial")
public class ServiceException extends NestedRuntimeException {
	
	public ServiceException() {
		super();
	}
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(Throwable cause) {
		super(cause);
	}
	
	public ServiceException(String message,Throwable cause) {
		super(message, cause);
	}

}

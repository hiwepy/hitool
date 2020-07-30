/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.exception;

/**
 * 数据库访问异常(RuntimeException)
 */
@SuppressWarnings("serial")
public class DataAccessException extends NestedRuntimeException {
	
	public DataAccessException(String message) {
		super(message);
	}
	
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}
	
	public DataAccessException(String key,String defaultMessage) {
		super(getText(key, defaultMessage));
	}

	public DataAccessException(String key,Object... replaceParas){
		super(getText(key, "", replaceParas));
	}

	public DataAccessException(Throwable cause,String key,Object... replaceParas){
		super(getText(key, "", replaceParas), cause);
	}
	
}

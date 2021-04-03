/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.exception;

@SuppressWarnings("serial")
public class UILayerException extends NestedRuntimeException {
	
	public UILayerException(String message) {
		super(message);
	}
	
	public UILayerException(String message, Throwable cause) {
		super(message, cause);
	}

	public UILayerException(Throwable cause) {
		super(cause);
	}
	
	public UILayerException(String key,String defaultMessage) {
		super(getText(key, defaultMessage));
	}

	public UILayerException(String key,Object... replaceParas){
		super(getText(key, "", replaceParas));
	}

	public UILayerException(Throwable cause,String key,Object... replaceParas){
		super(getText(key, "", replaceParas), cause);
	}
	
}

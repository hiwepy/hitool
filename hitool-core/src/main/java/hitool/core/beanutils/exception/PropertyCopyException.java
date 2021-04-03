/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils.exception;

import java.text.MessageFormat;

/*
 * 
 * 属性拷贝异常
 */
@SuppressWarnings("serial")
public class PropertyCopyException extends RuntimeException {
	/*
	 * 国际化信息key
	 */
	protected String msgKey = null;
	
	public PropertyCopyException(String message) {
		super(message);
	}

	public PropertyCopyException(Throwable cause) {
		super(cause);
	}

	public PropertyCopyException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyCopyException(String message, Throwable cause, String... replaceParas) {
		super(constructErrMsg(message, replaceParas));
	}

	/*
	 * 构造异常信息字符串
	 * @return
	 */
	private static String constructErrMsg(String msg, String... replaceParas){
		if(replaceParas!=null&&replaceParas.length>0){
			msg = msgFormat(msg, replaceParas);
		}
		return msg;
	}
	
	/*
	 * 格式化带有占位符的信息
	 */
	private static String msgFormat(String msg, String... replaceParas){
		MessageFormat form = new MessageFormat(msg);
		return form.format(replaceParas);
	}

	public String getMsgKey() {
		return msgKey;
	}

}
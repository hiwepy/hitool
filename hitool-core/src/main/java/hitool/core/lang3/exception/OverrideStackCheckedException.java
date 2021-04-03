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

@SuppressWarnings("serial")
public abstract class OverrideStackCheckedException extends Exception {
	
	public OverrideStackCheckedException() {
		fillInStackTrace();
	}

	public OverrideStackCheckedException(String msg) {
		super(msg);
	}
	
	public OverrideStackCheckedException(Throwable cause) {
		super(cause);
	}
	
	public OverrideStackCheckedException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public OverrideStackCheckedException(String message,Object... arguments){
		super(getText(message, arguments));
	}

	public OverrideStackCheckedException(Throwable cause,String message,Object... arguments){
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
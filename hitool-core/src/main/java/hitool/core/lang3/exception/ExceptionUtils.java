/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/*
 * 异常信息格式化工具
 */
public class ExceptionUtils extends org.apache.commons.lang3.exception.ExceptionUtils {
	
	// 从当前系统中获取换行符，默认是"\n"
	protected static String lineSeparator = System.getProperty("line.separator", "\n");

	public static String getFullHtmlStackTrace(Throwable throwable) {
		StringBuilder builder = new StringBuilder();
		builder.append(throwable.toString()).append(lineSeparator);
		StackTraceElement[] trace = throwable.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			// System.out.println("\tat " + trace[i]);
			builder.append("\tat ").append(trace[i]).append(lineSeparator);
		}
		Throwable ourCause = throwable.getCause();
		if (ourCause != null) {
			trace = ourCause.getStackTrace();
			for (int i = 0; i < trace.length; i++) {
				// System.out.println("\tat " + trace[i]);
				builder.append("\tat ").append(trace[i]).append(lineSeparator);
			}
		}
		return builder.toString().replace(lineSeparator, "<br/>").replace("\tat", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	}
	
	public static Throwable unwrapThrowable(Throwable wrapped) {
		Throwable unwrapped = wrapped;
		while (true) {
			if (unwrapped instanceof InvocationTargetException) {
				unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
			} else if (unwrapped instanceof UndeclaredThrowableException) {
				unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
			} else {
				return unwrapped;
			}
		}
	}
	
	/*
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}
	
	/*
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	/*
	 * 将ErrorStack转化为String.
	 */
	public static String getStackTraceAsString(Throwable e) {
		if (e == null){
			return "";
		}
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/*
	 * 判断异常是否由某些底层的异常引起.
	 */
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}
	
}

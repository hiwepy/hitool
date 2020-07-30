/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils.reflection;

import java.lang.reflect.InvocationTargetException;

public abstract class ReflectionExceptionUtils {
	
	public static void handleConstructorException(Exception ex) {
		if (ex instanceof NoSuchMethodException){
			throw new IllegalStateException("Method not found: "+ ex.getMessage());
		}
		if (ex instanceof IllegalAccessException){
			throw new IllegalStateException("Could not access method: "+ ex.getMessage());
		}
		if (ex instanceof InvocationTargetException){
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException){
			throw ((RuntimeException) ex);
		}
		handleUnexpectedException(ex);
	}
	
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException){
			throw new IllegalStateException("Method not found: "+ ex.getMessage());
		}
		if (ex instanceof IllegalAccessException){
			throw new IllegalStateException("Could not access method: "+ ex.getMessage());
		}
		if (ex instanceof InvocationTargetException){
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException){
			throw ((RuntimeException) ex);
		}
		handleUnexpectedException(ex);
	}

	public static void handleInvocationTargetException(InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}

	public static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException){
			throw ((RuntimeException) ex);
		}
		if (ex instanceof Error){
			throw ((Error) ex);
		}
		handleUnexpectedException(ex);
	}

	public static void rethrowException(Throwable ex) throws Exception {
		if (ex instanceof Exception){
			throw ((Exception) ex);
		}
		if (ex instanceof Error){
			throw ((Error) ex);
		}
		handleUnexpectedException(ex);
	}

	private static void handleUnexpectedException(Throwable ex) {
		throw new IllegalStateException("Unexpected exception thrown", ex);
	}
	
}

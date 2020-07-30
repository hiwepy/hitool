/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils.exception;

/**
 * Bean实例化异常
 */
@SuppressWarnings("serial")
public class BeanInstantiationException  extends RuntimeException {
	
	private Class<?> beanClass;
	/**
	 * Create a new BeanInstantiationException.
	 * @param beanClass the offending bean class
	 * @param msg the detail message
	 */
	public BeanInstantiationException(Class<?> beanClass, String msg) {
		this(beanClass, msg, null);
	}

	/**
	 * Create a new BeanInstantiationException.
	 * @param beanClass the offending bean class
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public BeanInstantiationException(Class<?> beanClass, String msg, Throwable cause) {
		super("Failed to instantiate [" + beanClass.getName() + "]: " + msg, cause);
		this.beanClass = beanClass;
	}


	/**
	 * Return the offending bean class.
	 */
	public Class<?> getBeanClass() {
		return this.beanClass;
	}

}

 

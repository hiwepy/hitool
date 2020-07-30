/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.beanutils.exception.BeanInstantiationException;
import hitool.core.beanutils.exception.PropertyCopyException;
import hitool.core.beanutils.reflection.ClassUtils;
import hitool.core.beanutils.reflection.ReflectionUtils;
import hitool.core.lang3.Assert;
import hitool.core.lang3.StringUtils;

/**
 * Java Bean 应用类 扩展 Apache Commons BeanUtils, 提供一些反射方面缺失功能的封装.
 */
public abstract class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	protected static Logger LOG = LoggerFactory.getLogger(BeanUtils.class);

	private BeanUtils() {
	}

	private static final Set<Class<?>> unknownEditorTypes = Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>(64));

	private static final ConcurrentMap<Class<?> , BeanInfo> COMPLIED_BEANINFO = new ConcurrentHashMap<Class<?> , BeanInfo>();
	
	/**
	 * Convenience method to instantiate a class using its no-arg constructor.
	 * As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * @param clazz class to instantiate
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	public static <T> T instantiate(Class<T> clazz) throws BeanInstantiationException {
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isInterface()) {
			throw new BeanInstantiationException(clazz, "Specified class is an interface");
		}
		try {
			return clazz.newInstance();
		}
		catch (InstantiationException ex) {
			throw new BeanInstantiationException(clazz, "Is it an abstract class?", ex);
		}
		catch (IllegalAccessException ex) {
			throw new BeanInstantiationException(clazz, "Is the constructor accessible?", ex);
		}
	}

	/**
	 * Instantiate a class using its no-arg constructor.
	 * As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * <p>Note that this method tries to set the constructor accessible
	 * if given a non-accessible (that is, non-public) constructor.
	 * @param clazz class to instantiate
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	public static <T> T instantiateClass(Class<T> clazz) throws BeanInstantiationException {
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isInterface()) {
			throw new BeanInstantiationException(clazz, "Specified class is an interface");
		}
		try {
			return instantiateClass(clazz.getDeclaredConstructor());
		}
		catch (NoSuchMethodException ex) {
			throw new BeanInstantiationException(clazz, "No default constructor found", ex);
		}
	}

	/**
	 * Instantiate a class using its no-arg constructor and return the new instance
	 * as the the specified assignable type.
	 * <p>Useful in cases where
	 * the type of the class to instantiate (clazz) is not available, but the type
	 * desired (assignableTo) is known.
	 * <p>As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * <p>Note that this method tries to set the constructor accessible
	 * if given a non-accessible (that is, non-public) constructor.
	 * @param clazz class to instantiate
	 * @param assignableTo type that clazz must be assignableTo
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	@SuppressWarnings("unchecked")
	public static <T> T instantiateClass(Class<?> clazz, Class<T> assignableTo) throws BeanInstantiationException {
		Assert.isAssignable(assignableTo, clazz);
		return (T) instantiateClass(clazz);
	}

	/**
	 * Convenience method to instantiate a class using the given constructor.
	 * As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * <p>Note that this method tries to set the constructor accessible
	 * if given a non-accessible (that is, non-public) constructor.
	 * @param ctor the constructor to instantiate
	 * @param args the constructor arguments to apply
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeanInstantiationException {
		Assert.notNull(ctor, "Constructor must not be null");
		try {
			ReflectionUtils.makeAccessible(ctor);
			return ctor.newInstance(args);
		}
		catch (InstantiationException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Is it an abstract class?", ex);
		}
		catch (IllegalAccessException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Is the constructor accessible?", ex);
		}
		catch (IllegalArgumentException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Illegal arguments for constructor", ex);
		}
		catch (InvocationTargetException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Constructor threw exception", ex.getTargetException());
		}
	}

	/**
	 * Find a method with the given method name and the given parameter types,
	 * declared on the given class or one of its superclasses. Prefers public methods,
	 * but will return a protected, package access, or private method too.
	 * <p>Checks {@code Class.getMethod} first, falling back to
	 * {@code findDeclaredMethod}. This allows to find public methods
	 * without issues even in environments with restricted Java security settings.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @param paramTypes the parameter types of the method to find
	 * @return the Method object, or {@code null} if not found
	 * @see Class#getMethod
	 * @see #findDeclaredMethod
	 */
	public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
		try {
			return clazz.getMethod(methodName, paramTypes);
		}
		catch (NoSuchMethodException ex) {
			return findDeclaredMethod(clazz, methodName, paramTypes);
		}
	}

	/**
	 * Find a method with the given method name and the given parameter types,
	 * declared on the given class or one of its superclasses. Will return a public,
	 * protected, package access, or private method.
	 * <p>Checks {@code Class.getDeclaredMethod}, cascading upwards to all superclasses.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @param paramTypes the parameter types of the method to find
	 * @return the Method object, or {@code null} if not found
	 * @see Class#getDeclaredMethod
	 */
	public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
		try {
			return clazz.getDeclaredMethod(methodName, paramTypes);
		}
		catch (NoSuchMethodException ex) {
			if (clazz.getSuperclass() != null) {
				return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
			}
			return null;
		}
	}

	/**
	 * Find a method with the given method name and minimal parameters (best case: none),
	 * declared on the given class or one of its superclasses. Prefers public methods,
	 * but will return a protected, package access, or private method too.
	 * <p>Checks {@code Class.getMethods} first, falling back to
	 * {@code findDeclaredMethodWithMinimalParameters}. This allows for finding public
	 * methods without issues even in environments with restricted Java security settings.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @return the Method object, or {@code null} if not found
	 * @throws IllegalArgumentException if methods of the given name were found but
	 * could not be resolved to a unique method with minimal parameters
	 * @see Class#getMethods
	 * @see #findDeclaredMethodWithMinimalParameters
	 */
	public static Method findMethodWithMinimalParameters(Class<?> clazz, String methodName)
			throws IllegalArgumentException {

		Method targetMethod = findMethodWithMinimalParameters(clazz.getMethods(), methodName);
		if (targetMethod == null) {
			targetMethod = findDeclaredMethodWithMinimalParameters(clazz, methodName);
		}
		return targetMethod;
	}

	/**
	 * Find a method with the given method name and minimal parameters (best case: none),
	 * declared on the given class or one of its superclasses. Will return a public,
	 * protected, package access, or private method.
	 * <p>Checks {@code Class.getDeclaredMethods}, cascading upwards to all superclasses.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @return the Method object, or {@code null} if not found
	 * @throws IllegalArgumentException if methods of the given name were found but
	 * could not be resolved to a unique method with minimal parameters
	 * @see Class#getDeclaredMethods
	 */
	public static Method findDeclaredMethodWithMinimalParameters(Class<?> clazz, String methodName)
			throws IllegalArgumentException {

		Method targetMethod = findMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
		if (targetMethod == null && clazz.getSuperclass() != null) {
			targetMethod = findDeclaredMethodWithMinimalParameters(clazz.getSuperclass(), methodName);
		}
		return targetMethod;
	}

	/**
	 * Find a method with the given method name and minimal parameters (best case: none)
	 * in the given list of methods.
	 * @param methods the methods to check
	 * @param methodName the name of the method to find
	 * @return the Method object, or {@code null} if not found
	 * @throws IllegalArgumentException if methods of the given name were found but
	 * could not be resolved to a unique method with minimal parameters
	 */
	public static Method findMethodWithMinimalParameters(Method[] methods, String methodName)
			throws IllegalArgumentException {

		Method targetMethod = null;
		int numMethodsFoundWithCurrentMinimumArgs = 0;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				int numParams = method.getParameterTypes().length;
				if (targetMethod == null || numParams < targetMethod.getParameterTypes().length) {
					targetMethod = method;
					numMethodsFoundWithCurrentMinimumArgs = 1;
				}
				else {
					if (targetMethod.getParameterTypes().length == numParams) {
						// Additional candidate with same length
						numMethodsFoundWithCurrentMinimumArgs++;
					}
				}
			}
		}
		if (numMethodsFoundWithCurrentMinimumArgs > 1) {
			throw new IllegalArgumentException("Cannot resolve method '" + methodName +
					"' to a unique method. Attempted to resolve to overloaded method with " +
					"the least number of parameters, but there were " +
					numMethodsFoundWithCurrentMinimumArgs + " candidates.");
		}
		return targetMethod;
	}

	/**
	 * Parse a method signature in the form {@code methodName[([arg_list])]},
	 * where {@code arg_list} is an optional, comma-separated list of fully-qualified
	 * type names, and attempts to resolve that signature against the supplied {@code Class}.
	 * <p>When not supplying an argument list ({@code methodName}) the method whose name
	 * matches and has the least number of parameters will be returned. When supplying an
	 * argument type list, only the method whose name and argument types match will be returned.
	 * <p>Note then that {@code methodName} and {@code methodName()} are <strong>not</strong>
	 * resolved in the same way. The signature {@code methodName} means the method called
	 * {@code methodName} with the least number of arguments, whereas {@code methodName()}
	 * means the method called {@code methodName} with exactly 0 arguments.
	 * <p>If no method can be found, then {@code null} is returned.
	 * @param signature the method signature as String representation
	 * @param clazz the class to resolve the method signature against
	 * @return the resolved Method
	 * @see #findMethod
	 * @see #findMethodWithMinimalParameters
	 */
	public static Method resolveSignature(String signature, Class<?> clazz) {
		Assert.hasText(signature, "'signature' must not be empty");
		Assert.notNull(clazz, "Class must not be null");
		int firstParen = signature.indexOf("(");
		int lastParen = signature.indexOf(")");
		if (firstParen > -1 && lastParen == -1) {
			throw new IllegalArgumentException("Invalid method signature '" + signature +
					"': expected closing ')' for args list");
		}
		else if (lastParen > -1 && firstParen == -1) {
			throw new IllegalArgumentException("Invalid method signature '" + signature +
					"': expected opening '(' for args list");
		}
		else if (firstParen == -1 && lastParen == -1) {
			return findMethodWithMinimalParameters(clazz, signature);
		}
		else {
			String methodName = signature.substring(0, firstParen);
			String[] parameterTypeNames =
					StringUtils.commaDelimitedListToStringArray(signature.substring(firstParen + 1, lastParen));
			Class<?>[] parameterTypes = new Class<?>[parameterTypeNames.length];
			for (int i = 0; i < parameterTypeNames.length; i++) {
				String parameterTypeName = parameterTypeNames[i].trim();
				try {
					parameterTypes[i] = ClassUtils.forName(parameterTypeName, clazz.getClassLoader());
				}
				catch (Throwable ex) {
					throw new IllegalArgumentException("Invalid method signature: unable to resolve type [" +
							parameterTypeName + "] for argument " + i + ". Root cause: " + ex);
				}
			}
			return findMethod(clazz, methodName, parameterTypes);
		}
	}

	/**
	 * Find a JavaBeans {@code PropertyDescriptor} for the given method,
	 * with the method either being the read method or the write method for
	 * that bean property.
	 * @param method the method to find a corresponding PropertyDescriptor for
	 * @param clazz the (most specific) class to introspect for descriptors
	 * @return the corresponding PropertyDescriptor, or {@code null} if none
	 * @throws BeansException if PropertyDescriptor lookup fails
	 * @since 3.2.13
	 */
	public static PropertyDescriptor findPropertyForMethod(Method method, Class<?> clazz) throws PropertyCopyException {
		Assert.notNull(method, "Method must not be null");
		PropertyDescriptor[] pds = getPropertyDescriptors(clazz);
		for (PropertyDescriptor pd : pds) {
			if (method.equals(pd.getReadMethod()) || method.equals(pd.getWriteMethod())) {
				return pd;
			}
		}
		return null;
	}

	/**
	 * Find a JavaBeans PropertyEditor following the 'Editor' suffix convention
	 * (e.g. "mypackage.MyDomainClass" -> "mypackage.MyDomainClassEditor").
	 * <p>Compatible to the standard JavaBeans convention as implemented by
	 * {@link java.beans.PropertyEditorManager} but isolated from the latter's
	 * registered default editors for primitive types.
	 * @param targetType the type to find an editor for
	 * @return the corresponding editor, or {@code null} if none found
	 */
	public static PropertyEditor findEditorByConvention(Class<?> targetType) {
		if (targetType == null || targetType.isArray() || unknownEditorTypes.contains(targetType)) {
			return null;
		}
		ClassLoader cl = targetType.getClassLoader();
		if (cl == null) {
			try {
				cl = ClassLoader.getSystemClassLoader();
				if (cl == null) {
					return null;
				}
			}
			catch (Throwable ex) {
				// e.g. AccessControlException on Google App Engine
				if (LOG.isDebugEnabled()) {
					LOG.debug("Could not access system ClassLoader: " + ex);
				}
				return null;
			}
		}
		String editorName = targetType.getName() + "Editor";
		try {
			Class<?> editorClass = cl.loadClass(editorName);
			if (!PropertyEditor.class.isAssignableFrom(editorClass)) {
				if (LOG.isWarnEnabled()) {
					LOG.warn("Editor class [" + editorName +
							"] does not implement [java.beans.PropertyEditor] interface");
				}
				unknownEditorTypes.add(targetType);
				return null;
			}
			return (PropertyEditor) instantiateClass(editorClass);
		}
		catch (ClassNotFoundException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("No property editor [" + editorName + "] found for type " +
						targetType.getName() + " according to 'Editor' suffix convention");
			}
			unknownEditorTypes.add(targetType);
			return null;
		}
	}

	/**
	 * Check if the given type represents a "simple" property:
	 * a primitive, a String or other CharSequence, a Number, a Date,
	 * a URI, a URL, a Locale, a Class, or a corresponding array.
	 * <p>Used to determine properties to check for a "simple" dependency-check.
	 * @param clazz the type to check
	 * @return whether the given type represents a "simple" property
	 * @see org.springframework.beans.factory.support.RootBeanDefinition#DEPENDENCY_CHECK_SIMPLE
	 * @see org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#checkDependencies
	 */
	public static boolean isSimpleProperty(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
	}

	/**
	 * Check if the given type represents a "simple" value type:
	 * a primitive, a String or other CharSequence, a Number, a Date,
	 * a URI, a URL, a Locale or a Class.
	 * @param clazz the type to check
	 * @return whether the given type represents a "simple" value type
	 */
	public static boolean isSimpleValueType(Class<?> clazz) {
		return ClassUtils.isPrimitiveOrWrapper(clazz) || clazz.isEnum() ||
				CharSequence.class.isAssignableFrom(clazz) ||
				Number.class.isAssignableFrom(clazz) ||
				Date.class.isAssignableFrom(clazz) ||
				clazz.equals(URI.class) || clazz.equals(URL.class) ||
				clazz.equals(Locale.class) || clazz.equals(Class.class);
	}

	/**
	 *  获取声明属性
	 * @param clazz
	 * @return
	 */
	public static String[] getDeclaredPropertys(Class<?> clazz) {
		String[] props = null;
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);
		Vector<String> propVector = new Vector<String>();
		for (PropertyDescriptor propDes : propertyDescriptors) {
			// 如果是class属性 跳过
			if (propDes.getName().equals("class")
					|| propDes.getReadMethod() == null
					|| propDes.getWriteMethod() == null) {
				continue;
			}
			propVector.add(propDes.getName());
		}
		props = new String[propVector.size()];
		propVector.toArray(props);
		return props;
	}

	/**
	 * 检测类是否包含属性
	 * @param clazz
	 * @param propertyName
	 * @return
	 */
	public static boolean hasProperty(Class<?> clazz, String propertyName) {
		boolean has = false;
		try {
			new PropertyDescriptor(propertyName, clazz);
			has = true;
		} catch (IntrospectionException e) {
			LOG.info(clazz.getName() + ":" + e.getMessage());
			return false;
		}
		return has;
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
		BeanInfo beanInfo = getBeanInfo(clazz);
		return beanInfo.getPropertyDescriptors();
	}

	public static BeanInfo getBeanInfo(Class<?> clazz) {
		BeanInfo beanInfo = null;
		try {
 			beanInfo = Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
			LOG.error(e.getMessage(), e);
		}
		return beanInfo;
	}
	
	public static BeanInfo getCachedBeanInfo(Class<?> clazz) {
		BeanInfo ret = null;
		try {
			ret = COMPLIED_BEANINFO.get(clazz);
 			if (ret != null) {
 				return ret;
 			} 
 			ret = Introspector.getBeanInfo(clazz);
 			BeanInfo existing = COMPLIED_BEANINFO.putIfAbsent(clazz, ret);
 			if (existing != null) {
 				ret = existing;
 			}
 			return ret;
		} catch (IntrospectionException e) {
			LOG.error(e.getMessage(), e);
		}
		return ret;
	}

	public static List<Method> findMethods(Class<?> clazz, String regex) {
		List<Method> methods = new ArrayList<Method>();
		BeanInfo beanInfo = getBeanInfo(clazz);
		String mathodName = null;
		for (MethodDescriptor descriptor : beanInfo.getMethodDescriptors()) {
			mathodName = descriptor.getMethod().getName();
			if (mathodName.matches(regex)) {
				methods.add(descriptor.getMethod());
			}
		}
		return methods;
	}

	public static List<String> findMethodNames(Class<?> clazz, String regex) {
		List<String> methods = new ArrayList<String>();
		BeanInfo beanInfo = getBeanInfo(clazz);
		String mathodName = null;
		for (MethodDescriptor descriptor : beanInfo.getMethodDescriptors()) {
			mathodName = descriptor.getMethod().getName();
			if (mathodName.matches(regex)) {
				methods.add(mathodName);
			}
		}
		return methods;
	}

}

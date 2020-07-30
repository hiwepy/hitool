/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils.proxy.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hitool.core.beanutils.annotation.Intercepts;
import hitool.core.beanutils.annotation.Signature;
import hitool.core.beanutils.exception.PluginException;
import hitool.core.lang3.exception.ExceptionUtils;

public class Plugin implements InvocationHandler {

	private Object target;
	private Interceptor interceptor;
	private Map<Class<?>, Set<Method>> signatureMap;

	private Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
		this.target = target;
		this.interceptor = interceptor;
		this.signatureMap = signatureMap;
	}

	public static Object wrap(Object target, Interceptor interceptor) {
		Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
		Class<?> type = target.getClass();
		Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
		if (interfaces.length > 0) {
			return Proxy.newProxyInstance(type.getClassLoader(), interfaces, new Plugin(target, interceptor, signatureMap));
		}
		return target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			Set<Method> methods = signatureMap.get(method.getDeclaringClass());
			if (methods != null && methods.contains(method)) {
				return interceptor.intercept(new Invocation(target, method, args));
			}
			return method.invoke(target, args);
		} catch (Exception e) {
			throw ExceptionUtils.unwrapThrowable(e);
		}
	}

	private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
		Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);
		// issue #251
		if (interceptsAnnotation == null) {
			throw new PluginException("No @Intercepts annotation was found in interceptor " + interceptor.getClass().getName());
		}
		Signature[] sigs = interceptsAnnotation.value();
		Map<Class<?>, Set<Method>> signatureMap = new HashMap<Class<?>, Set<Method>>();
		for (Signature sig : sigs) {
			Set<Method> methods = signatureMap.get(sig.type());
			if (methods == null) {
				methods = new HashSet<Method>();
				signatureMap.put(sig.type(), methods);
			}
			try {
				Method method = sig.type().getMethod(sig.method(), sig.args());
				methods.add(method);
			} catch (NoSuchMethodException e) {
				throw new PluginException("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
			}
		}
		return signatureMap;
	}

	private static Class<?>[] getAllInterfaces(Class<?> type,Map<Class<?>, Set<Method>> signatureMap) {
		Set<Class<?>> interfaces = new HashSet<Class<?>>();
		while (type != null) {
			for (Class<?> c : type.getInterfaces()) {
				if (signatureMap.containsKey(c)) {
					interfaces.add(c);
				}
			}
			type = type.getSuperclass();
		}
		return interfaces.toArray(new Class<?>[interfaces.size()]);
	}

}

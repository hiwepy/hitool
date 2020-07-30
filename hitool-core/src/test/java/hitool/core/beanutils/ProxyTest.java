package hitool.core.beanutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import hitool.core.beanutils.annotation.Proxy;

public class ProxyTest {
	
	@SuppressWarnings("unchecked")
	public static <T> T getProxyEntity(Class<T> clazz){
		
		T t = (T) newInstance(clazz);
		Method[] methods = clazz.getMethods();
		for(int i=0;i<methods.length;i++){
			Proxy pt = methods[i].getAnnotation(Proxy.class);
			if(pt == null){
				continue;
			}
			ProxyEntity pe = (ProxyEntity) newInstance(pt.proxyClass());
			//t = (T) pe.bind(t, pe, methods[i].getName());
		}
		return t;
	}
	
	private static <T> Object newInstance(final Class<T> clazz){
		try {
			Constructor<T> cons = clazz.getConstructor();
			return cons.newInstance();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}

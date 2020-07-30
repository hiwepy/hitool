/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

public class AnnotationUtils {

	/*Annotation[] annotations = extension.getClass().getAnnotations();
	for (Annotation annotation : annotations) {
		if (!annotation.annotationType().getName().equals(ExtensionMapping.class.getName())) {
			continue;
		}
		// 代理对象
		if (Proxy.isProxyClass(annotation.getClass())) {
			try {
				
				// 获取 memberValues
				Map<String, String> memberValues = AnnotationProxyUtils.getMemberValues(annotation);
				rtList.add(new PairModel(memberValues.get("id"), memberValues.get("title")));

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			ExtensionMapping mapping = (ExtensionMapping) annotation;
			rtList.add(new PairModel(mapping.id(), mapping.title()));
		}

	}*/
	
	public static Map<String, String> getProxyMemberValues(Annotation annotation) throws Exception {
		// 代理对象
		if (Proxy.isProxyClass(annotation.getClass())) {
			// 获取 annotation 注解对象 所持有的 InvocationHandler
			InvocationHandler handler = Proxy.getInvocationHandler(annotation);
			// 获取 AnnotationInvocationHandler 的 memberValues 字段
			Field hField = handler.getClass().getDeclaredField("memberValues");
			// 因为这个字段事 private final 修饰，所以要打开权限
			hField.setAccessible(true);
			// 获取 memberValues
			return (Map<String, String>) hField.get(handler);
		}
		return null;

	}

}

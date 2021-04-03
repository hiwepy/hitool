/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils.proxy.plugin;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InterceptorChain {

	private final List<Interceptor> interceptors = new ArrayList<Interceptor>();

	public Object pluginAll(Object target) {
		for (Interceptor interceptor : interceptors) {
			target = interceptor.plugin(target);
		}
		return target;
	}

	public void addInterceptor(Interceptor interceptor) {
		interceptors.add(interceptor);
	}

	public List<Interceptor> getInterceptors() {
		return Collections.unmodifiableList(interceptors);
	}

}

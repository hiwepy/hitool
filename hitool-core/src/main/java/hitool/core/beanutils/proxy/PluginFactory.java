/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils.proxy;

import java.util.List;

import hitool.core.beanutils.proxy.plugin.Interceptor;
import hitool.core.beanutils.proxy.plugin.InterceptorChain;

@SuppressWarnings("unchecked")
public class PluginFactory {

	protected static final InterceptorChain interceptorChain = new InterceptorChain();

	public List<Interceptor> getInterceptors() {
		return interceptorChain.getInterceptors();
	}
	
	public static <T> T createProxy(T target){
		return (T) interceptorChain.pluginAll(target);
	}

}

package hitool.core.beanutils;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
@FixMethodOrder(MethodSorters.JVM) 
public class BeanPropertyUtilsTest {
	
	@Before
	public void setUp() {
		 
	}
	
	@Test
 	public void testUtils() {
 		Map<String, Object> source = new HashMap<String, Object>();
		// source.put("methodName", "测试名称1");
		// source.put("methodName2", "测试名称2");
		// source.put("methodName3", "测试名称3");

		// source.put("proxyBean.methodName", "测试名称1");
		// source.put("proxyBean.methodName2", "测试名称2");
		// source.put("proxyBean.methodName3", "测试名称3");

		source.put("proxyBean.array[1]", "测试名称1");
		source.put("proxyBean.proxyBean.methodName", "测试名称1");

		ProxyEntity entity = new ProxyEntity();

		// BeanUtils.populate(entity, source);

		BeanPropertyUtils.populate(entity, source, true);
		// System.out.println(entity.getMethodName());
		System.out.println("===========================RESULT================================");
		System.out.println(entity.getProxyBean().getArray()[0]);
		System.out.println("proxyBean.proxyBean.methodName："+entity.getProxyBean().getProxyBean().getMethodName());
 	}
 	
 	 
 	
 }
 
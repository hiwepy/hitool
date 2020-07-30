package hitool.core.beanutils;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
@FixMethodOrder(MethodSorters.JVM) 
public class OgnlPropertyUtilsTest {
	
	@Before
	public void setUp() {
	}
	
	@Test
 	public void testUtils() {
		
 		System.out.println(" 取值==================================================================================");
		
		Map<String, Object> source1 = new HashMap<String, Object>();
		source1.put("methodName", "测试名称1");
		source1.put("methodName2", "测试名称2");
		source1.put("methodName3", "测试名称3");
		
		
		ProxyEntity entity_a = new ProxyEntity();

		ProxyEntity entity1 = new ProxyEntity();
		entity1.setProxyBean(new ProxyEntity());
		entity1.setArray(new String[]{"2","3","4"});
		entity1.setMethodName("sdasd");
		
		entity_a.setProxyBean(entity1);
		source1.put("proxyBean", entity_a);

		System.out.println("#methodName :" + OgnlPropertyUtils.getProperty(source1, "#methodName == #methodName"));
		System.out.println("#methodName2 :" + OgnlPropertyUtils.getProperty(source1, "#methodName2"));
		System.out.println("#methodName3 :" + OgnlPropertyUtils.getProperty(source1, "#methodName3"));
		System.out.println("#proxyBean.proxyBean.array【0】 :" + OgnlPropertyUtils.getProperty(source1, "#proxyBean.proxyBean.array[0]"));
		System.out.println("#proxyBean.proxyBean.methodName :" + OgnlPropertyUtils.getProperty(source1, "#proxyBean.proxyBean.methodName"));
		
		System.out.println(" 赋值==================================================================================");

		Map<String, Object> source11 = new HashMap<String, Object>();
		source11.put("#root.methodName", "测试名称1");
		source11.put("#methodName2", "测试名称2");
		source11.put("#methodName3", "测试名称3");
		source11.put("#root.proxyBean.array[0]", "测试名称1");
		source11.put("#root.proxyBean.methodName", "测试名称1");

		ProxyEntity entity_b = new ProxyEntity();
		ProxyEntity entity2 = new ProxyEntity();
		entity2.setProxyBean(new ProxyEntity());
		entity2.setArray(new String[10]);
		entity_b.setProxyBean(entity2);

		OgnlPropertyUtils.populate(entity_b, source11);
		
		System.out.println("entity_b.getMethodName():"+entity_b.getMethodName());
		System.out.println("entity_b.getProxyBean().getArray()[0]:"+entity_b.getProxyBean().getArray()[0]);
		System.out.println("entity_b.getProxyBean().getMethodName():"+entity_b.getProxyBean().getMethodName());
 	}
 	
 	
 }
 
package hitool.core.beanutils;

import java.util.List;
import java.util.Map;


public class ProxyEntity{
	
	
	private String methodName;
	private String methodName2;
	private String methodName3;
	
	private ProxyEntity proxyBean;
	private String[] array;
	private List<String> list;
	private Map<String,String> map;
	
	public ProxyEntity(){}

	public ProxyEntity getProxyBean() {
	
		return proxyBean;
	}

	public void setProxyBean(ProxyEntity proxyBean) {
	
		this.proxyBean = proxyBean;
	}

	public String getMethodName() {
	
		return methodName;
	}

	public void setMethodName(String methodName) {
	
		this.methodName = methodName;
	}

	public String getMethodName2() {
	
		return methodName2;
	}

	public void setMethodName2(String methodName2) {
	
		this.methodName2 = methodName2;
	}

	public String getMethodName3() {
	
		return methodName3;
	}

	public void setMethodName3(String methodName3) {
	
		this.methodName3 = methodName3;
	}

	public String[] getArray() {
		return array;
	}

	public void setArray(String[] array) {
		this.array = array;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
	
	
}

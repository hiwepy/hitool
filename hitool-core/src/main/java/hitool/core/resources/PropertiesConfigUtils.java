package hitool.core.resources;

import java.util.Properties;

import hitool.core.io.ConfigUtils;
import hitool.core.lang3.StringUtils;
public class PropertiesConfigUtils extends ConfigUtils {
	
	protected Properties configCached = new Properties();  

	public <T> PropertiesConfigUtils(Class<T> clazz,String location) {
		if (configCached.isEmpty()) {
			configCached.putAll(ConfigUtils.getProperties(clazz , location ));
        }
	}

	public static <T> PropertiesConfigUtils newInstance(Class<T> clazz,String location) {
		return new PropertiesConfigUtils(clazz , location);
	}
	
	public String getText(String key) {
		return getText(key,"");
	}
	
	public String getText(String key, String defaultValue) {
		return configCached.getProperty(key, defaultValue); 
	}
	
	public String getText(String key, Object... params) {
		String message = getText(key,"");
		return StringUtils.getSafeStr(setParams(message,params), "") ;
	}
	
	protected String setParams(String message,Object... params) {
		if (null != params && params.length > 0){
			for (int i = 0 ; i < params.length ; i++){
				message = message.replaceFirst("\\{"+i+"\\}", String.valueOf(params[i]));
			}
		}
		return message;
	}
 
}

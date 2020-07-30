package hitool.freemarker.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.io.ConfigUtils;
import hitool.core.lang3.StringUtils;

public class TemplateConfigUtils extends ConfigUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(TemplateConfigUtils.class);
	private static ThreadLocal<Properties> configCached = new ThreadLocal<Properties>();  
	private static Object lock = new Object();
	private static String location = "freemarker.properties";
	private volatile static TemplateConfigUtils singleton;

	public static TemplateConfigUtils getInstance(String location) {
		if (singleton == null) {
			synchronized (TemplateConfigUtils.class) {
				if (singleton == null) {
					singleton = new TemplateConfigUtils(location);
				}
			}
		}
		return singleton;
	}
	
	private TemplateConfigUtils(String location) {
		TemplateConfigUtils.location = location;
	}
	
	private static Properties getConfig() {
		if (configCached.get() == null) {
			synchronized (lock) {
				configCached.set(ConfigUtils.getProperties(TemplateConfigUtils.class, location ));
			}
        }
		return configCached.get(); 
	}
	
	public static String getText(String key) {
		return TemplateConfigUtils.getText(key,"");
	}
	
	public static String getText(String key, String defaultValue) {
		return getConfig().getProperty(key, defaultValue); 
	}
	
	public static String getText(String key, Object... params) {
		String message = getText(key,"");
		return StringUtils.getSafeStr(setParams(message,params), "") ;
	}
	
	/**
	 * 为包含如 { 0 }占位符的字符串中的占位符设值
	 * @param message
	 * @param params
	 * @return
	 */
	protected static String setParams(String message,Object... params) {
		if (null != params && params.length > 0){
			for (int i = 0 ; i < params.length ; i++){
				message = message.replaceFirst("\\{"+i+"\\}", String.valueOf(params[i]));
			}
		}
		return message;
	}
 
}

package hitool.core.io;

import java.util.Properties;

import hitool.core.lang3.StringUtils;

/**
 *  Properties初始化参数
 */
public class ConfigPropertiesUtils extends ConfigUtils {

	protected Properties configCached = new Properties();

	public <T> ConfigPropertiesUtils(Class<T> clazz, String location) {
		if (configCached.isEmpty()) {
			configCached.putAll(ConfigUtils.getProperties(clazz, location));
		}
	}

	public static <T> ConfigPropertiesUtils newInstance(Class<T> clazz,
			String location) {
		return new ConfigPropertiesUtils(clazz, location);
	}

	public String getText(String key) {
		return getText(key, "");
	}

	public String getText(String key, String defaultValue) {
		return configCached.getProperty(key, defaultValue);
	}

	public String getText(String key, Object... params) {
		String message = getText(key, "");
		return StringUtils.getSafeStr(setParams(message, params), "");
	}

	/**
	 *  为包含如 {0}占位符的字符串中的占位符设值
	 * @param message
	 * @param params
	 * @return ：
	 */
	protected String setParams(String message, Object... params) {
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				message = message.replaceFirst("\\{" + i + "\\}",
						String.valueOf(params[i]));
			}
		}
		return message;
	}

}

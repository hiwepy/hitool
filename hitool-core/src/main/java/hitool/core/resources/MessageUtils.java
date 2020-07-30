package hitool.core.resources;

import java.text.MessageFormat;

import hitool.core.lang3.StringUtils;

public abstract class MessageUtils {
	
	public static String getText(String key) {
		return getText(key, "");
	}
	
	public static String getText(String key, Object... arguments) {
		String message = PropertiesUtils.getProperty(key);
		return StringUtils.getSafeStr(arguments != null ? message : MessageFormat.format(message, arguments),"");
	}
	
}

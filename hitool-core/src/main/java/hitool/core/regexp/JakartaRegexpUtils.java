package hitool.core.regexp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class JakartaRegexpUtils {

	protected static ConcurrentMap<String, Pattern> COMPLIED_PATTERN = new ConcurrentHashMap<String, Pattern>();
	
	/*
	 * 正则表达式验证方法:匹配表达式则返回true,不匹配则返回false
	 */
	public static boolean matches(String regexp, String str) { 
        Pattern pattern = getPattern(regexp); 
        Matcher matcher = pattern.matcher(str); 
        return matcher.matches(); 
    }

	public static Pattern getPattern(String regexp) {
		if (StringUtils.isNotEmpty(regexp)) {
			Pattern ret = COMPLIED_PATTERN.get(regexp);
			if (ret != null) {
				return ret;
			}
			ret = Pattern.compile(regexp);
			Pattern existing = COMPLIED_PATTERN.putIfAbsent(regexp, ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}
    
}

package hitool.core.regexp;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.io.ConfigUtils;
import hitool.core.lang3.StringUtils;

/**
 */
public class RegexpPatternUtils {

	protected static Logger LOG = LoggerFactory.getLogger(RegexpPatternUtils.class);
	protected static ConcurrentMap<String, Pattern> COMPLIED_PATTERN = new ConcurrentHashMap<String, Pattern>();
	protected static String[] REGEXP_ARR = new String[]{"date","html","math","mobile","net","normal","special","sql"};
	
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
	
	public static Properties getRegexpProperties(String name) {
		Properties regexpProperties = null;
		if(StringUtils.hasText(name)){
			if(name.matches("([a-zA-Z0-9_$\u4E00-\u9FA5]+)")){
				String regexp_file_name = "regexp_"+name+".properties";
				regexpProperties = ConfigUtils.getProperties(RegexpPatternUtils.class, regexp_file_name);
				if(null != regexpProperties){
					LOG.error("the Properties which named "+regexp_file_name+" is not find on root directory !");
				}
			}else{
				LOG.error("name not match [a-zA-Z0-9_$\u4E00-\u9FA5]");
			}
		}else{
			LOG.error(" name is null !");
		}
		return regexpProperties;
	}

	public static String getRegexp(String fileName,String regexpName) {
		if (!StringUtils.hasText(fileName) || !StringUtils.hasText(regexpName)) {
			return null;
		}
		Properties regexpProperties = getRegexpProperties(fileName);
		String regexStr = regexpProperties.getProperty(regexpName);
		if(!StringUtils.hasText(regexStr)){
			LOG.error("the regex which key named "+regexpName+" is not find in Properties !");
		}
		return regexStr;
	}
	
	public static String getRegexp(String regexpName) {
		if (!StringUtils.hasText(regexpName)) {
			return null;
		}
		String regexStr = null;
		for (String name : REGEXP_ARR) {
			String regexp_file_name = "regexp_"+name+".properties";
			Properties regexpProperties = ConfigUtils.getProperties(RegexpPatternUtils.class, regexp_file_name);
			if(null != regexpProperties){
				regexStr = regexpProperties.getProperty(regexpName);
				if(StringUtils.hasText(regexStr)){
					break;
				}
			}
		}
		if(!StringUtils.hasText(regexStr)){
			LOG.error("the regex which key named "+regexpName+" is not find in Properties !");
		}
		return regexStr;
	}
	
}

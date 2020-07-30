/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.format.property;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import hitool.core.io.ConfigUtils;
import hitool.core.lang3.StringUtils;
/**
 *  *.properties 字符格式化如：系统异常，异常信息为{0}！
 */
public class PropertyFormat {
	
	private static Set<String> classPaths = new HashSet<String>();
	private static String classpath = null;
	private MessageFormat f = null;
	public PropertyFormat(String classpath){
		PropertyFormat.classpath = classpath;
		PropertyFormat.classPaths.add(classpath);
	}
	
	public PropertyFormat(HashSet<String> propertiesPaths){
		PropertyFormat.classPaths = propertiesPaths;
	}
	
	public String format(String key, Object... arguments){
		String valueString = null;
		//取得对应key的值
		if(StringUtils.hasText(classpath)){
			valueString = ConfigUtils.getProperties(PropertyFormat.class, classpath).getProperty(classpath, key);
		}
		//如果对应key的值为空
		if(!StringUtils.hasText(valueString)){
			for (String path : classPaths) {
				valueString =  ConfigUtils.getProperties(PropertyFormat.class, path).getProperty(key);
				if(StringUtils.hasText(valueString)){
					break;
				}
			} 
		} 
		f = new MessageFormat(valueString==null?"":valueString);
		return f.format(arguments);
	}
	
}

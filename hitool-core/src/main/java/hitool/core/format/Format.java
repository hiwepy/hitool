/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.format;

/**
 * format object to String
 */
public interface Format {
	
	/**
	 *  对源字符串中符合正则进行替换，得到替换后的字符;
	 * 				此方法，不用担心位置和顺序，只要给出的源数据有此属性即可，并且可实现 bean.xxx.xx 、lis[0] 类似的取值
	 * 				1.如果JDBC语句 "select a,b,c from table t where t.a = #{bean.a} and t.b = #{bean.b} " 可用 "#{xx.xx}"格式正则，匹配 替换
	 */
	public <T> String format(String message,T bean);
	
	/**
	 *  调用MessageFormat 的 format 方法处理，忽略位置问题，根据中括号中数字为数组下标
	 * 				1.如果JDBC语句 "select a,b,c from table t where t.a = {0} and t.b = {1} " ,中括号内数字为数组元素对应下标
	 */
	public String format(String message, String... replaceParas);
	
	/**
	 *  占位符替换格式法，此方法可根据用户给出的正则进行符合正则的占位符替换，占位符出现顺序为数组元素顺序，数组元素个数需多于占位符个数
	 * 				1.如果JDBC语句 "select a,b,c from table t where t.a = ? and t.b = ? " 可用 "\\?"正则，匹配 替换该匹配字符的下标为数组中对应下标的元素
	 */
	public String format(String message,String regex,String[] replaceParas);
	
}

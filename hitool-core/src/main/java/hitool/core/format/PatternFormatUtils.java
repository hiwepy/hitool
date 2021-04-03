/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.format;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hitool.core.beanutils.BeanPropertyUtils;
import hitool.core.lang3.StringUtils;

/*
 * 
 */
public abstract class PatternFormatUtils  {


	protected static Pattern pattern_x = Pattern.compile("(?:(?:\\#\\{)(.*?)(?:\\}))+");
	protected static Pattern pattern_y = Pattern.compile("(?:(?:\\$\\{)(.*?)(?:\\}))+");
	protected static Pattern pattern_z = Pattern.compile("(?:(?:\\{)(\\d*?)(?:\\}))+");
	
	public static <T> String format(String message,T bean) {
		if(StringUtils.hasText(message)){
			Matcher matcher_x = pattern_x.matcher(message);
			Matcher matcher_y = pattern_y.matcher(message);
			while (matcher_x.find()) {
				try {
					String key = matcher_x.group(1);
					Object target = BeanPropertyUtils.getProperty(bean, key , true);
					message = message.replaceAll( "\\#\\{" + key + "\\}", target == null ? "null" : target.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			while (matcher_y.find()) {
				try {
					String key = matcher_y.group(1);
					Object target = BeanPropertyUtils.getProperty(bean, key, true);
					message = message.replaceAll( "\\$\\{" + key + "\\}", target == null ? "null" : target.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return message;
	}

	public static String format(String message, String... arguments) {
		if(StringUtils.hasText(message)){
			Matcher matcher_z = pattern_z.matcher(message);
			while (matcher_z.find()) {
				int index = Integer.valueOf(matcher_z.group(1));
				if(null != arguments && index < arguments.length){
		            message = (arguments[index] != null )? message.replaceAll("\\{" + index + "\\}",arguments[index]):message;
				}
			}
			return message;
		}
		return null;//new MessageFormat(message).format(arguments);
	}
	
	public static String format(String message,String regex,String[] arguments) {
		if(StringUtils.hasText(message)){
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(message);
			String[] splits = message.split(regex);
			StringBuilder build = new StringBuilder();
			int index = 0;
			while (matcher.find()) {
				build.append(splits[index]).append(arguments[index]);
				index++;
			}
			build.append(splits[index]);
			return build.toString();
		}
		return message;
	}


	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("xn", "2013-2014");
		map.put("xq", "2");
		//此方法，不用担心位置和顺序，只要给出的源数据有此属性即可，并且可实现 bean.xxx.xx 、lis[0] 类似的取值
		System.out.println(PatternFormatUtils.format("新增【${xn}】学年【${xq}】学期", map));
		System.out.println(PatternFormatUtils.format("新增【#{xn}】学年【#{xq}】学期", map));
		//调用MessageFormat 的 format 方法处理，忽略位置问题，根据中括号中数字为数组下标
		System.out.println(PatternFormatUtils.format("新增【'{1}'】学年【{0}】学期", new String[]{"2","2012-2013"}));
		//占位符替换格式法，此方法可根据用户给出的正则进行符合正则的占位符替换，占位符出现顺序为数组元素顺序，数组元素个数需多于占位符个数
		System.out.println(PatternFormatUtils.format("新增【?】学年【?】学期","\\?", new String[]{"2","2012-2013"}));

	}
}

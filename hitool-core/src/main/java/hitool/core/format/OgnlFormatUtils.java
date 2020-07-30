/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.format;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hitool.core.beanutils.OgnlPropertyUtils;
import hitool.core.lang3.StringUtils;

/**
 * 字符串格式化：其中从bean中获得数据方式使用到了ognl
 * <b>Example:</b>
 * <pre>
 * Map<String, Object> context = new HashMap<String, Object>();
 * context.put("xn", "2013-2014");
 * context.put("xq", "2");
 * context.put("xm", "test");
 * context.put("xh", "20112564564685");
 * context.put("a", "10000");
 * context.put("b", "2");
 * 		
 * Map<String, Object> p1 = new HashMap<String, Object>();
 * Map<String, Object> p2 = new HashMap<String, Object>();
 * p1.put("name", "张三");
 * p2.put("name", "李四");
 * context.put("p1", p1);
 * context.put("p2", p2);
 * Map<String, Object> root = new HashMap<String, Object>();
 * root.put("username","zhangsan");
 * context.put("user", root);
 * 		
 * System.out.println(OgnlFormatUtils.format("用户名：{#p1.name + ',' + #p2.name + ',' + #user.username}", context));
 * System.out.println(OgnlFormatUtils.format("计算结果:{#a / #b }", context));
 * //此方法，不用担心位置和顺序，只要给出的源数据有此属性即可，并且可实现 bean.xxx.xx 、lis[0] 类似的取值
 * System.out.println(OgnlFormatUtils.format("新增【{#xn}】学年【{#xq}】学期", context));
 * //调用MessageFormat 的 format 方法处理，忽略位置问题，根据中括号中数字为数组下标
 * System.out.println(OgnlFormatUtils.format("新增【'{1}'】学年【{0}】学期", new String[]{"2","2012-2013"}));
 * //占位符替换格式法，此方法可根据用户给出的正则进行符合正则的占位符替换，占位符出现顺序为数组元素顺序，数组元素个数需多于占位符个数
 * System.out.println(OgnlFormatUtils.format("新增【?】学年【?】学期","\\?", new String[]{"2","2012-2013"}));
 * </pre>
 */
public class OgnlFormatUtils {

	protected static Pattern pattern_x = Pattern.compile("(?:(?:\\{)([^\\{\\}]*?)(?:\\}))+");
	protected static Pattern pattern_z = Pattern.compile("(?:(?:\\{)(\\d*?)(?:\\}))+");
	
	public static <T> String format(String message,T context) {
		return format(message, context, new Object());
	}
	
	public static <T, R> String format(String message,T context,R root) {
		if(StringUtils.hasText(message)){
			StringBuilder builder = new StringBuilder();
			Matcher matcher_x = pattern_x.matcher(message);
			int relatedEnd = 0;
			while (matcher_x.find()) {
				
				//"新增【{#xn + #xq}】学年【{#xq}】学期"
				
				//借助OGNL组件获取数据
				Object target = OgnlPropertyUtils.getProperty(context, matcher_x.group(1));
				
				//System.out.println("start:" + matcher_z.start() + ",end : " + matcher_z.end());
				//截取字符
				builder.append(message.substring(relatedEnd, matcher_x.start() ));
				builder.append(null!= target ? target.toString():"null");
				//重置最后一次end
				relatedEnd = matcher_x.end();
				
				//message = message.replaceAll( "\\{" + matcher_z.group(1) + "\\}", target == null ? "null" : target.toString());
			}
			builder.append(message.substring(relatedEnd , message.length() ));
			return builder.toString();
		}
		return message;
	}

	public static String format(String message, String... arguments) {
		if(StringUtils.hasText(message)){
			Matcher matcher_z = pattern_z.matcher(message);
			while (matcher_z.find()) {
				try {
					int index = Integer.valueOf(matcher_z.group(1));
					if(null != arguments && index < arguments.length){
			            message = (arguments[index] != null )? message.replaceAll("\\{" + index + "\\}",arguments[index]):message;
					}
				} catch (NumberFormatException e) {
					
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
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("xn", "2013-2014");
		context.put("xq", "2");
		context.put("xm", "test");
		context.put("xh", "20112564564685");
		context.put("a", "10000");
		context.put("b", "2");
		
		Map<String, Object> p1 = new HashMap<String, Object>();
		Map<String, Object> p2 = new HashMap<String, Object>();
		p1.put("name", "张三");
		p2.put("name", "李四");
		context.put("p1", p1);
		context.put("p2", p2);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("username","zhangsan");
		context.put("user", root);
		
		System.out.println(OgnlFormatUtils.format("用户名：{#p1.name + ',' + #p2.name + ',' + #user.username}", context));
		
		
		System.out.println(OgnlFormatUtils.format("计算结果:{#a / #b }", context));
		
		
		//此方法，不用担心位置和顺序，只要给出的源数据有此属性即可，并且可实现 bean.xxx.xx 、lis[0] 类似的取值
		System.out.println(OgnlFormatUtils.format("新增【{#xn}】学年【{#xq}】学期", context));
		//调用MessageFormat 的 format 方法处理，忽略位置问题，根据中括号中数字为数组下标
		System.out.println(OgnlFormatUtils.format("新增【'{1}'】学年【{0}】学期", new String[]{"2","2012-2013"}));
		//占位符替换格式法，此方法可根据用户给出的正则进行符合正则的占位符替换，占位符出现顺序为数组元素顺序，数组元素个数需多于占位符个数
		System.out.println(OgnlFormatUtils.format("新增【?】学年【?】学期","\\?", new String[]{"2","2012-2013"}));

	}
}

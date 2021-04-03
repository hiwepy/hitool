/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.io;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 
 */
public class CapacityUtils {

	protected static Pattern pattern_find = Pattern
			.compile("^([1-9]\\d*|[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)(B|KB|MB|GB|TB|PB|EB|ZB|YB|BB)$");
	protected static Map<String, Float> powers = new HashMap<String, Float>();
	static {
		// 1KB(Kilobyte 千字节)=1024B
		powers.put("KB", Float.valueOf(1024));
		// 1MB(Megabyte 兆字节 简称“兆”)=1024KB
		powers.put("MB", Float.valueOf(1024 * 1024));
		// 1GB(Gigabyte 吉字节 又称“千兆”)=1024MB
		powers.put("GB", Float.valueOf(1024 * 1024 * 1024));
		// 1TB(Trillionbyte 万亿字节 太字节)=1024GB
		powers.put("TB", Float.valueOf(1024 * 1024 * 1024 * 1024));

		/*
		 * , //1PB（Petabyte 千万亿字节 拍字节）=1024TB 'PB': 1024 * 1024 * 1024 * 1024 *
		 * 1024, 　　//1EB（Exabyte 百亿亿字节 艾字节）=1024PB 'EB': 1024 * 1024 * 1024 *
		 * 1024 * 1024 * 1024, 　　//1ZB(Zettabyte 十万亿亿字节 泽字节)= 1024 EB 'ZB': 1024
		 * * 1024 * 1024 * 1024 * 1024 * 1024 * 1024, 　　//1YB(Yottabyte 一亿亿亿字节
		 * 尧字节)= 1024 ZB 'YB': 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 *
		 * 1024, 　　//1BB(Brontobyte 一千亿亿亿字节)= 1024 YB 'BB': 1024 * 1024 * 1024 *
		 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024
		 */
	}

	/*
	 *  计算指定数值单位对应的字节数：如 1KB 计算得到 1024
	 * @param value
	 * @return
	 */
	public static BigDecimal getCapacity(String value) {
		if (value == null || value.trim().length() == 0) {
			return BigDecimal.ZERO;
		}
		value = value.trim().toUpperCase();
		Matcher matcher = pattern_find.matcher(value);
		if (matcher.find()) {
			BigDecimal num = new BigDecimal(matcher.group(1));
			BigDecimal mult = new BigDecimal(powers.get(matcher.group(2)));
			return num.multiply(mult);
		} else {
			return BigDecimal.ZERO;
		}
	}

	public static long getLongCapacity(String value) {
		if (value == null || value.trim().length() == 0) {
			return 0;
		}
		value = value.trim().toUpperCase();
		Matcher matcher = pattern_find.matcher(value);
		if (matcher.find()) {
			Long num = Long.valueOf(matcher.group(1));
			Float mult = powers.get(matcher.group(2));
			return num.longValue() * mult.longValue();
		} else {
			return 0;
		}
	}

	public static float getFloatCapacity(String value) {
		if (value == null || value.trim().length() == 0) {
			return 0;
		}
		value = value.trim().toUpperCase();
		Matcher matcher = pattern_find.matcher(value);
		if (matcher.find()) {
			Float num = Float.valueOf(matcher.group(1));
			Float mult = powers.get(matcher.group(2));
			return num.floatValue() * mult.floatValue();
		} else {
			return -1;
		}
	}

	public static BigDecimal getCapacity(long value, String unit) {
		BigDecimal num = new BigDecimal(value);
		BigDecimal divide = new BigDecimal(powers.get(unit));
		return num.divide(divide, 2, BigDecimal.ROUND_HALF_UP);
	}

}

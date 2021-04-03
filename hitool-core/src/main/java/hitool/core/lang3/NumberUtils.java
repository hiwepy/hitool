/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3;

import java.util.HashMap;
import java.util.Map;

public abstract class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {
	
	public static boolean isDigit(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	private static final Character[] CN_NUMERIC = { '一', '二', '三', '四', '五',
		'六', '七', '八', '九', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖',
		'十', '百', '千', '拾', '佰', '仟', '万', '亿', '○', 'Ｏ', '零' };

	private static Map<Character, Integer> cnNumeric = null;
	
	static {
		cnNumeric = new HashMap<Character, Integer>(40, 0.85f);
		for (int j = 0; j < 9; j++) {
			cnNumeric.put(CN_NUMERIC[j], j + 1);
		}
		for (int j = 9; j < 18; j++) {
			cnNumeric.put(CN_NUMERIC[j], j - 8);
		}
		cnNumeric.put('两', 2);
		cnNumeric.put('十', 10);
		cnNumeric.put('拾', 10);
		cnNumeric.put('百', 100);
		cnNumeric.put('佰', 100);
		cnNumeric.put('千', 1000);
		cnNumeric.put('仟', 1000);
		cnNumeric.put('万', 10000);
		cnNumeric.put('亿', 100000000);
	}
	
	public static int isCNNumeric(char c) {
		Integer i = cnNumeric.get(c);
		if (i == null) {
			return -1;
		}
		return i.intValue();
	}
	
	public static int cnNumericToArabic(String cnn, boolean flag) {
	
		cnn = cnn.trim();
		if (cnn.length() == 1)
			return isCNNumeric(cnn.charAt(0));
	
		if (flag){
			cnn = cnn.replace('佰', '百').replace('仟', '千').replace('拾', '十').replace('零', ' ');
		}
		// System.out.println(cnn);
		int yi = -1, wan = -1, qian = -1, bai = -1, shi = -1;
		int val = 0;
		yi = cnn.lastIndexOf('亿');
		if (yi > -1) {
			val += cnNumericToArabic(cnn.substring(0, yi), false) * 100000000;
			if (yi < cnn.length() - 1) {
				cnn = cnn.substring(yi + 1, cnn.length());
			} else {
				cnn = "";
			}
			if (cnn.length() == 1) {
				int arbic = isCNNumeric(cnn.charAt(0));
				if (arbic <= 10) {
					val += arbic * 10000000;
				}
				cnn = "";
			}
		}
	
		wan = cnn.lastIndexOf('万');
		if (wan > -1) {
			val += cnNumericToArabic(cnn.substring(0, wan), false) * 10000;
			if (wan < cnn.length() - 1) {
				cnn = cnn.substring(wan + 1, cnn.length());
			} else {
				cnn = "";
			}
			if (cnn.length() == 1) {
				int arbic = isCNNumeric(cnn.charAt(0));
				if (arbic <= 10) {
					val += arbic * 1000;
				}
				cnn = "";
			}
		}
	
		qian = cnn.lastIndexOf('千');
		if (qian > -1) {
			val += cnNumericToArabic(cnn.substring(0, qian), false) * 1000;
			if (qian < cnn.length() - 1) {
				cnn = cnn.substring(qian + 1, cnn.length());
			} else {
				cnn = "";
			}
			if (cnn.length() == 1) {
				int arbic = isCNNumeric(cnn.charAt(0));
				if (arbic <= 10) {
					val += arbic * 100;
				}
				cnn = "";
			}
		}
	
		bai = cnn.lastIndexOf('百');
		if (bai > -1) {
			val += cnNumericToArabic(cnn.substring(0, bai), false) * 100;
			if (bai < cnn.length() - 1) {
				cnn = cnn.substring(bai + 1, cnn.length());
			} else {
				cnn = "";
			}
			if (cnn.length() == 1) {
				int arbic = isCNNumeric(cnn.charAt(0));
				if (arbic <= 10) {
					val += arbic * 10;
				}
				cnn = "";
			}
		}
	
		shi = cnn.lastIndexOf('十');
		if (shi > -1) {
			if (shi == 0) {
				val += 1 * 10;
			} else {
				val += cnNumericToArabic(cnn.substring(0, shi), false) * 10;
			}
			if (shi < cnn.length() - 1) {
				cnn = cnn.substring(shi + 1, cnn.length());
			} else {
				cnn = "";
			}
		}
	
		cnn = cnn.trim();
		for (int j = 0; j < cnn.length(); j++) {
			val += isCNNumeric(cnn.charAt(j)) * Math.pow(10, cnn.length() - j - 1);
		}
		return val;
	}
	
	public static int qCNNumericToArabic(String cnn) {
		int val = 0;
		cnn = cnn.trim();
		for (int j = 0; j < cnn.length(); j++) {
			val += isCNNumeric(cnn.charAt(j)) * Math.pow(10, cnn.length() - j - 1);
		}
		return val;
	}
	
	public static void main(String[] args) {
		int val = 0;
		long s = System.nanoTime();
		val = cnNumericToArabic("三亿二千零六万七千五百六", true);
		System.out.println(val);
		val = cnNumericToArabic("一九九八", true);
		System.out.println(val);
		long e = System.nanoTime();
		System.out.format("Done[" + val + "], cost: %.5fsec\n", ((float) (e - s)) / 1E9);
	}
	

}

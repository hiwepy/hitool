package hitool.core.format.number;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  数值的常用函数
 */
public abstract class NumberUtils {

	/**
	 * 精度,并且“四舍五入”。如（11，2）--->11;(11.123,2)-->11.12;(11.156,2)-->11.16
	 */
	public static Object numberFormat(Number num, int precision) {
		if (isNull(num)) {
			return null;
		}
		Format nf = null;
		StringBuffer format = new StringBuffer("###,###");
		if (precision > 0) {
			format.append(".");
			for (int i = 0; i < precision; i++) {
				format.append("#");
			}
		}
		nf = new DecimalFormat(format.toString());
		return nf.format(num);
	}

	/**
	 * 判断是否为数字[0-9]。包括 负数或科学记数  
	 */
	public static boolean isNumeric(Object num) {
		String regex = "^(-)?\\d+\\.?\\d*[E]?\\d*";
		if (isNull(num)) {
			return false;
		}
		return isMatch(num, regex);
	}

	/**
	 *  判断是否为正数
	 */
	public static boolean isPositive(Object num) {
		if (isNull(num)) {
			return false;
		}
		String regex = "^(-){1}\\d+\\.?\\d*[E]?\\d*$";
		return !isMatch(num, regex);
	}

	private static boolean isNull(Object obj) {
		return (obj == null || obj.toString().trim().length() == 0) ;
	}

	private static boolean isMatch(Object matStr, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(matStr.toString());
		return !isNum.matches() ? false : true;
	}

	public static void main(String[] args) {
		// System.out.println(numberFormat(11.115, 2));
		// System.out.println("numeric " + isNumeric(" "));
		// System.out.println("Positive " + isPositive(11));
	}
}

/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.math.logic;

import java.math.BigDecimal;

/*
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精确的浮点数运算，包括加减乘除和四舍五入。
 */
public class DoubleArith {

	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 2;
	// 这个类不能实例化
	private DoubleArith() {}

	/*
	 * 功能介绍：提供精确的加法运算。
	 * @param v1被加数
	 * @param v2加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/*
	 * 功能介绍：提供精确的减法运算。
	 * @param v1被减数
	 * @param v2减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/*
	 * 功能介绍：提供精确的乘法运算。
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/*
	 * 功能介绍：提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后2位，以后的数字四舍五入。
	 * @param v1被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/*
	 * 功能介绍：提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 * @param v1被除数
	 * @param v2除数
	 * @param scale 表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/*
	 * 功能介绍：提供精确的小数位四舍五入处理。
	 * @param v需要四舍五入的数字
	 * @param scale小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/*
	 * 方法用途和描述: 向上取整
	 * @param value
	 * @return 如 ceil(11.1)=12,ceil(11.0)=11,ceil(11.5)=12, ceil(-11.1)=11,ceil(-11.5)=11
	 */
	public static int ceil(double value) {
		return (int) Math.ceil(value);
	}

	/*
	 * 方法用途和描述: 向下取整数
	 * @param value
	 * @return 如 floor(11.1)=11,floor(11.5)=11,floor(-11.1)=-12,floor(-11.5)=-12
	 */
	public static int floor(double value) {
		return (int) Math.floor(value);
	}

	/*
	 * 方法用途和描述: 四舍五入取整
	 * @param value
	 * @return 如 round(11.1)=11,round(11.5)=12,round(-11.1)=-11,round(-11.5)=-12
	 */
	public static int round(double value) {
		return (int) round(value, 0);
	}

	public static void main(String[] args) {
		System.out.println(sub(12.123, 18.1595));
		System.out.println(round(sub(12.123, 18.1595), 1));
		System.out.println(ceil(-11.5));
		System.out.println(floor(-11.6));
		System.out.println(round(-11.4));
	}

}

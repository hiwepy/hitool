/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.math.logic;

import java.math.BigDecimal;

public class BigDecimalArith {

	/**
	 * 
	 * 功能描述：四舍五入的加法运算
	 * @param plus1 被加数
	 * @param plus2 加数
	 * @param newScale 小数点位数
	 * @return 
	 */
	public static BigDecimal add(BigDecimal plus1, BigDecimal plus2,int newScale) {
		if (null == plus1 || null == plus2) {
			return new BigDecimal(0.00);
		}
		return setScale(plus1.add(plus2), newScale);
	}

	/**
	 *  
	 * 功能描述：四舍五入的减法运算
	 * @param sub1 被减数
	 * @param sub2 减数
	 * @param newScale 小数点位数
	 * @return 
	 */
	public static BigDecimal sub(BigDecimal sub1, BigDecimal sub2, int newScale) {
		if (null == sub1 || null == sub2) {
			return new BigDecimal(0.00);
		}
		return setScale(sub1.subtract(sub2), newScale);
	}

	/**
	 * 
	 * 
	 * 功能描述：四舍五入的乘法运算
	 * @param mul1 被乘数
	 * @param mul2乘数
	 * @param newScale 小数点位数
	 * @return 
	 */
	public static BigDecimal mul(BigDecimal mul1, BigDecimal mul2, int newScale) {
		if (null == mul1 || null == mul2) {
			return new BigDecimal(0.00);
		}
		return setScale(mul1.multiply(mul2), newScale);
	}

	/**
	 * 
	 * 功能描述：四舍五入的除法运算
	 * @param div1 被除数
	 * @param div2 除数
	 * @param newScale 小数点位数
	 * @return 
	 */
	public static BigDecimal div(BigDecimal div1, BigDecimal div2, int newScale) {
		if (null == div1 || null == div2) {
			return new BigDecimal(0.00);
		}
		return setScale(div1.divide(div2), newScale);
	}

	private static BigDecimal setScale(BigDecimal result, int newScale) {
		return result.setScale(newScale, BigDecimal.ROUND_HALF_UP);
	}

	public static void main(String[] args) {
		System.out.println(add(new BigDecimal(66.3358), new BigDecimal(0.26), 2));
		System.out.println(sub(new BigDecimal(1.255), new BigDecimal(0.05), 2));
		System.out.println(mul(new BigDecimal(1.25), new BigDecimal(2.3), 2));
		System.out.println(div(new BigDecimal(1), new BigDecimal(8), 2));
	}

}

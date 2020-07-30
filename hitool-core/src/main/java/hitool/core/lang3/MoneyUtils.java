package hitool.core.lang3;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * 货币计算的工具类。
 * @author zhangqy
 * @version v1.0.0
 */
public abstract class MoneyUtils {
	
	/** 货币保留位数，如123.45元 */
	public final static int MONEY_POS = 2;

	/** 默认的货币值,0.00 */
	public final static BigDecimal DEFAULT_MONEY = fixMoney(new BigDecimal(0));

	/** 大写数字 */
	private static final String[] NUMBERS = { "零", "壹", "贰", "叁", "肆", "伍",
			"陆", "柒", "捌", "玖" };
	/** 整数部分的单位 */
	private static final String[] IUNIT = { "元", "拾", "佰", "仟", "万", "拾", "佰",
			"仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟" };
	/** 小数部分的单位 */
	private static final String[] DUNIT = { "角", "分", "厘" };

	/**
	 * 得到大写金额。
	 */
	public static String toChinese(String str) {
		str = str.replaceAll(",", "");// 去掉","
		String integerStr;// 整数部分数字
		String decimalStr;// 小数部分数字

		// 初始化：分离整数部分和小数部分
		if (str.indexOf(".") > 0) {
			integerStr = str.substring(0, str.indexOf("."));
			decimalStr = str.substring(str.indexOf(".") + 1);
		} else if (str.indexOf(".") == 0) {
			integerStr = "";
			decimalStr = str.substring(1);
		} else {
			integerStr = str;
			decimalStr = "";
		}
		// integerStr去掉首0，不必去掉decimalStr的尾0(超出部分舍去)
		if (!integerStr.equals("")) {
			integerStr = Long.toString(Long.parseLong(integerStr));
			if (integerStr.equals("0")) {
				integerStr = "";
			}
		}
		// overflow超出处理能力，直接返回
		if (integerStr.length() > IUNIT.length) {
			System.out.println(str + ":超出处理能力");
			return str;
		}

		int[] integers = toArray(integerStr);// 整数部分数字
		boolean isMust5 = isMust5(integerStr);// 设置万单位
		int[] decimals = toArray(decimalStr);// 小数部分数字
		return getChineseInteger(integers, isMust5)
				+ getChineseDecimal(decimals);
	}

	/**
	 * 整数部分和小数部分转换为数组，从高位至低位
	 */
	private static int[] toArray(String number) {
		int[] array = new int[number.length()];
		for (int i = 0; i < number.length(); i++) {
			array[i] = Integer.parseInt(number.substring(i, i + 1));
		}
		return array;
	}

	/**
	 * 得到中文金额的整数部分。
	 */
	private static String getChineseInteger(int[] integers, boolean isMust5) {
		StringBuffer chineseInteger = new StringBuffer("");
		int length = integers.length;
		for (int i = 0; i < length; i++) {
			// 0出现在关键位置：1234(万)5678(亿)9012(万)3456(元)
			// 特殊情况：10(拾元、壹拾元、壹拾万元、拾万元)
			String key = "";
			if (integers[i] == 0) {
				if ((length - i) == 13)// 万(亿)(必填)
					key = IUNIT[4];
				else if ((length - i) == 9)// 亿(必填)
					key = IUNIT[8];
				else if ((length - i) == 5 && isMust5)// 万(不必填)
					key = IUNIT[4];
				else if ((length - i) == 1)// 元(必填)
					key = IUNIT[0];
				// 0遇非0时补零，不包含最后一位
				if ((length - i) > 1 && integers[i + 1] != 0)
					key += NUMBERS[0];
			}
			chineseInteger.append(integers[i] == 0 ? key
					: (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
		}
		return chineseInteger.toString();
	}

	/**
	 * 得到中文金额的小数部分。
	 */
	private static String getChineseDecimal(int[] decimals) {
		StringBuffer chineseDecimal = new StringBuffer("");
		for (int i = 0; i < decimals.length; i++) {
			// 舍去3位小数之后的
			if (i == 3)
				break;
			chineseDecimal.append(decimals[i] == 0 ? ""
					: (NUMBERS[decimals[i]] + DUNIT[i]));
		}
		return chineseDecimal.toString();
	}

	/**
	 * 判断第5位数字的单位"万"是否应加。
	 */
	private static boolean isMust5(String integerStr) {
		int length = integerStr.length();
		if (length > 4) {
			String subInteger = "";
			if (length > 8) {
				// 取得从低位数，第5到第8位的字串
				subInteger = integerStr.substring(length - 8, length - 4);
			} else {
				subInteger = integerStr.substring(0, length - 4);
			}
			return Integer.parseInt(subInteger) > 0;
		} else {
			return false;
		}
	}
	
	/**
	 * 如果<code>money</code>为null,返回默认的货币值,0.00； 否则调整货币，使之保留小数点后两位，并符合四舍五入。
	 * 
	 * @param money
	 * @return
	 */
	public static BigDecimal defaultIfNull(BigDecimal money) {
		if (money == null) {
			return DEFAULT_MONEY;
		}
		return fixMoney(money);
	}

	/**
	 * 调整货币，使之保留小数点后两位，并符合四舍五入
	 * 
	 * @param money 要格式化的数字
	 * @see #fixMoney(BigDecimal, int)
	 * @return BigDecimal 返回BigDecimal对象类型，格式为保留小数点后两位，并四舍五入
	 */
	public static BigDecimal fixMoney(BigDecimal money) {
		return fixMoney(money, MONEY_POS);
	}

	/**
	 * 调整货币，根据参数设置保留小数点位数，并符合四舍五入
	 * 
	 * @param money 要格式化的数字
	 * @param pos 要保留的小数位数，默认为保留两位
	 * @see #fixMoney(BigDecimal, int, int)
	 * @return BigDecimal 返回BigDecimal对象类型，格式为保留参数<code>pos</code>定义的小数位数，并四舍五入
	 */
	public static BigDecimal fixMoney(BigDecimal money, int pos) {
		return fixMoney(money, pos, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 调整货币，根据参数设置要保留的位数和取舍
	 * 
	 * @param money 要格式化的数字
	 * @param pos 	要保留的小数位数
	 * @param round 格式化时进行何种舍入，常见有以下方式：<code>ROUND_CEILING</code>，<code>ROUND_DOWN</code>，
	 *            <code>ROUND_FLOOR</code>，<code>ROUND_HALF_DOWN</code>，<code>ROUND_HALF_EVEN</code>，
	 *            <code>ROUND_HALF_UP</code>，<code>ROUND_UNNECESSARY</code>，<code>ROUND_UP</code>，
	 *            详细请参看{@link java.math.BigDecimal}
	 * @see java.math.BigDecimal
	 * @return BigDecimal 返回BigDecimal对象类型，所有的格式由参数定义
	 */
	public static BigDecimal fixMoney(BigDecimal money, int pos, int round) {
		BigDecimal result = null;
		if (money == null) {
			return null;
		}
		result = money.setScale(pos, round);
		return result;
	}

	/**
	 * 根据设置的保留小数点位数获得货币金额，默认为保留两位小数和四舍五入
	 * 
	 * @param money 要格式化的数字，类行为String
	 * @see #getMoney(String, int)
	 * @return BigDecimal 返回BigDecimal类型，默认保留两位小数点，并四舍五入
	 */
	public static BigDecimal getMoney(String money) {
		return getMoney(money, MONEY_POS);
	}

	/**
	 * 根据设置的保留小数点位数获得货币金额，默认为四舍五入
	 * 
	 * @param money 要格式化的数字，类型为String
	 * @param pos  要保留的小数位数
	 * @see #getMoney(String, int, int)
	 * @return BigDecimal 返回BigDecimal类型，格式为保留参数<code>pos</code>定义的小数位数，默认为四舍五入
	 */
	public static BigDecimal getMoney(String money, int pos) {
		return getMoney(money, pos, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 根据设置的保留小数点位数和取舍规则获得货币金额
	 * 
	 * @param money 要格式化的数字，类行为String
	 * @param pos 要保留的小数位数
	 * @param round 格式化时进行何种舍入，常见有以下方式：<code>ROUND_CEILING</code>，<code>ROUND_DOWN</code>，
	 *            <code>ROUND_FLOOR</code>，<code>ROUND_HALF_DOWN</code>，<code>ROUND_HALF_EVEN</code>，
	 *            <code>ROUND_HALF_UP</code>，<code>ROUND_UNNECESSARY</code>，<code>ROUND_UP</code>，
	 *            详细请参看{@link java.math.BigDecimal}
	 * @see #fixMoney(BigDecimal, int, int)
	 * @return BigDecimal 返回BigDecimal类型，格式为保留参数<code>pos</code>定义的小数位数，舍入规则为参数<code>round</code>定义
	 */
	public static BigDecimal getMoney(String money, int pos, int round) {
		if (money != null) {
			try {
				return fixMoney(new BigDecimal(money), pos, round);
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 根据设置的保留小数点位数获得货币金额，默认为四舍五入
	 * 
	 * @param money 要格式化的数字
	 * @see #getMoneyStr(BigDecimal, int)
	 * @return String 返回String类型，格式为默认保留两位小数，并四舍五入
	 */
	public static String getMoneyStr(BigDecimal money) {
		return getMoneyStr(money, MONEY_POS);
	}

	/**
	 * 根据设置的保留小数点位数获得货币金额，默认为四舍五入
	 * 
	 * @param money 要格式化的数字
	 * @param pos 要保留的小数位数
	 * @see #getMoneyStr(BigDecimal, int, int)
	 * @return String 返回String类型，格式为保留参数<code>pos</code>定义的小数位数，默认四舍五入
	 */
	public static String getMoneyStr(BigDecimal money, int pos) {
		return getMoneyStr(money, pos, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 根据设置的保留小数点位数和取舍规则获得货币金额
	 * 
	 * @param money 要格式化的数字
	 * @param pos 要保留的小数位数
	 * @param round 格式化时进行何种舍入，常见有以下方式：<code>ROUND_CEILING</code>，<code>ROUND_DOWN</code>，
	 *            <code>ROUND_FLOOR</code>，<code>ROUND_HALF_DOWN</code>，<code>ROUND_HALF_EVEN</code>，
	 *            <code>ROUND_HALF_UP</code>，<code>ROUND_UNNECESSARY</code>，<code>ROUND_UP</code>，
	 *            详细请参看{@link java.math.BigDecimal}
	 * @see #fixMoney(BigDecimal, int, int)
	 * @return String 返回String类型，格式为保留参数<code>pos</code>定义的小数位数，舍入规则为参数<code>round</code>定义
	 */
	public static String getMoneyStr(BigDecimal money, int pos, int round) {
		if (money != null) {
			return fixMoney(money, pos, round).toString();
		} else {
			return null;
		}
	}

	/**
	 * 货币加运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1 第一个加参数
	 * @param money2 第二个加参数
	 * @see #add(BigDecimal, BigDecimal, int)
	 * @return BigDecimal 返回BigDecimal类型，得到参数<code>money1</code>和<code>money2</code>的和，默认为保留两位小数和四舍五入
	 */
	public static BigDecimal add(BigDecimal money1, BigDecimal money2) {
		return add(money1, money2, MONEY_POS);
	}

	/**
	 * 货币加运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1  第一个加参数
	 * @param money2 第二个加参数
	 * @param pos  要保留的小数位数
	 * @see #add(BigDecimal, BigDecimal, int, int)
	 * @return BigDecimal 返回BigDecimal类型，得到参数<code>money1</code>和<code>money2</code>的和，
	 *         保留参数<code>pos</code>定义的小数位数，默认为四舍五入
	 */
	public static BigDecimal add(BigDecimal money1, BigDecimal money2, int pos) {
		return add(money1, money2, pos, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 货币加运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1 第一个加参数
	 * @param money2 第二个加参数
	 * @param pos 要保留的小数位数
	 * @param round 格式化时进行何种舍入，常见有以下方式：<code>ROUND_CEILING</code>，<code>ROUND_DOWN</code>，
	 *            <code>ROUND_FLOOR</code>，<code>ROUND_HALF_DOWN</code>，<code>ROUND_HALF_EVEN</code>，
	 *            <code>ROUND_HALF_UP</code>，<code>ROUND_UNNECESSARY</code>，<code>ROUND_UP</code>，
	 *            详细请参看{@link java.math.BigDecimal}
	 * @see java.math.BigDecimal#add(java.math.BigDecimal)
	 * @return BigDecimal 返回BigDecimal类型，得到参数<code>money1</code>和<code>money2</code>的和，
	 *         保留参数<code>pos</code>定义的小数位数，舍入规则由参数<code>round</code>定义
	 */
	public static BigDecimal add(BigDecimal money1, BigDecimal money2, int pos, int round) {
		if (money1 == null) {
			return fixMoney(money2, pos, round);
		}

		if (money2 == null) {
			return fixMoney(money1, pos, round);
		}

		return money1.add(money2).setScale(pos, round);
	}

	/**
	 * 货币减运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1 第一个被减参数
	 * @param money2 第二个键参数
	 * @see #subtract(BigDecimal, BigDecimal, int)
	 * @return BigDecimal 返回BigDecimal类型，默认为保留两位小数和四舍五入
	 */
	public static BigDecimal subtract(BigDecimal money1, BigDecimal money2) {
		return subtract(money1, money2, MONEY_POS);
	}

	/**
	 * 货币减运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1  第一个被减参数
	 * @param money2  第二个键参数
	 * @param pos 结果要保留的小数位数
	 * @see #subtract(BigDecimal, BigDecimal, int, int)
	 * @return BigDecimal 返回BigDecimal类型，保留参数<code>pos</code>定义的小数位数，默认为四舍五入
	 */
	public static BigDecimal subtract(BigDecimal money1, BigDecimal money2, int pos) {
		return subtract(money1, money2, pos, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 货币减运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1 第一个被减参数
	 * @param money2  第二个键参数
	 * @param pos 结果要保留的小数位数
	 * @param round 格式化时进行何种舍入，常见有以下方式：<code>ROUND_CEILING</code>，<code>ROUND_DOWN</code>，
	 *            <code>ROUND_FLOOR</code>，<code>ROUND_HALF_DOWN</code>，<code>ROUND_HALF_EVEN</code>，
	 *            <code>ROUND_HALF_UP</code>，<code>ROUND_UNNECESSARY</code>，<code>ROUND_UP</code>，
	 *            详细请参看{@link java.math.BigDecimal}
	 * @see java.math.BigDecimal#subtract(java.math.BigDecimal)
	 * @return BigDecimal 返回BigDecimal类型，保留参数<code>pos</code>定义的小数位数，舍入规则由参数<code>round</code>定义
	 */
	public static BigDecimal subtract(BigDecimal money1, BigDecimal money2, int pos, int round) {
		if (money1 == null) {
			return null;
		}

		if (money2 == null) {
			return fixMoney(money1, pos, round);
		}

		return money1.subtract(money2).setScale(pos, round);
	}

	/**
	 * 货币乘运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1
	 *            第一个被乘参数
	 * @param money2
	 *            第二个乘参数
	 * @see #multiply(BigDecimal, BigDecimal, int)
	 * @return BigDecimal 返回BigDecimal类型，默认为保留两位小数和四舍五入
	 */
	public static BigDecimal multiply(BigDecimal money1, BigDecimal money2) {
		return multiply(money1, money2, MONEY_POS);
	}

	/**
	 * 货币乘运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1
	 *            第一个被乘参数
	 * @param money2
	 *            第二个乘参数
	 * @param pos
	 *            要保留的小数位数
	 * @see #multiply(BigDecimal, BigDecimal, int, int)
	 * @return BigDecimal 返回BigDecimal类型，保留参数<code>pos</code>定义的小数位数，默认为四舍五入
	 */
	public static BigDecimal multiply(BigDecimal money1, BigDecimal money2, int pos) {
		return multiply(money1, money2, pos, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 货币乘运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1
	 *            第一个被乘参数
	 * @param money2
	 *            第二个乘参数
	 * @param pos
	 *            要保留的小数位数
	 * @param round
	 *            格式化时进行何种舍入，常见有以下方式：<code>ROUND_CEILING</code>，<code>ROUND_DOWN</code>，
	 *            <code>ROUND_FLOOR</code>，<code>ROUND_HALF_DOWN</code>，<code>ROUND_HALF_EVEN</code>，
	 *            <code>ROUND_HALF_UP</code>，<code>ROUND_UNNECESSARY</code>，<code>ROUND_UP</code>，
	 *            详细请参看{@link java.math.BigDecimal}
	 * @see java.math.BigDecimal#multiply(java.math.BigDecimal)
	 * @return BigDecimal 返回BigDecimal类型，保留参数<code>pos</code>定义的小数位数，舍入规则由参数<code>round</code>定义
	 */
	public static BigDecimal multiply(BigDecimal money1, BigDecimal money2, int pos, int round) {
		if ((money1 == null) || (money2 == null)) {
			return null;
		}

		return money1.multiply(money2).setScale(pos, round);
	}

	/**
	 * 货币除运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1
	 *            第一个被除数
	 * @param money2
	 *            第二个除数
	 * @return BigDecimal 返回BigDecimal类型，默认为保留两位小数和四舍五入
	 */
	public static BigDecimal divide(BigDecimal money1, BigDecimal money2) {
		return divide(money1, money2, MONEY_POS);
	}

	/**
	 * 货币除运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1
	 *            第一个被除数
	 * @param money2
	 *            第二个除数
	 * @param pos
	 *            要保留的小数位数
	 * @return BigDecimal 返回BigDecimal类型，保留参数<code>pos</code>定义的小数位数，默认为四舍五入
	 */
	public static BigDecimal divide(BigDecimal money1, BigDecimal money2, int pos) {
		return divide(money1, money2, pos, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 货币除运算，默认返回结果为保留小数点两位和四舍五入
	 * 
	 * @param money1
	 *            第一个被除数
	 * @param money2
	 *            第二个除数
	 * @param pos
	 *            要保留的小数位数
	 * @param round
	 *            格式化时进行何种舍入，常见有以下方式：<code>ROUND_CEILING</code>，<code>ROUND_DOWN</code>，
	 *            <code>ROUND_FLOOR</code>，<code>ROUND_HALF_DOWN</code>，<code>ROUND_HALF_EVEN</code>，
	 *            <code>ROUND_HALF_UP</code>，<code>ROUND_UNNECESSARY</code>，<code>ROUND_UP</code>，
	 *            详细请参看{@link java.math.BigDecimal}
	 * @return BigDecimal 返回BigDecimal类型，保留参数<code>pos</code>定义的小数位数，舍入规则由参数<code>round</code>定义
	 */
	public static BigDecimal divide(BigDecimal money1, BigDecimal money2, int pos, int round) {
		if ((money1 == null) || (money2 == null)) {
			return null;
		}

		return money1.divide(money2, pos, round).setScale(pos, round);
	}

	/**
	 * 得到格式化的数字，当前默认为中国区域
	 * 
	 * @param data
	 *            要格式化的数字，类型为long
	 * @return String 格式化的结果，类型为String，形如：123,543
	 */
	public static String formatNumber(long data) {
		NumberFormat nFormat = NumberFormat.getInstance(Locale.CHINA);

		return nFormat.format(data);
	}

	/**
	 * 得到格式化的数字，当前默认为中国区域
	 * 
	 * @param data
	 *            要格式化的数字，类型为double
	 * @return String 格式化的结果，类型为String，形如：123,543.12
	 */
	public static String formatNumber(double data) {
		NumberFormat nFormat = NumberFormat.getInstance(Locale.CHINA);

		return nFormat.format(data);
	}

	/**
	 * 得到格式化的数字，当前默认为中国区域
	 * 
	 * @param data
	 *            要格式化的数字，类型为Object
	 * @return String 格式化的结果，类型为String，形如：123,543.12
	 */
	public static String formatNumber(Object data) {
		NumberFormat nFormat = NumberFormat.getInstance(Locale.CHINA);

		return nFormat.format(data);
	}

	/**
	 * 从一个百分数得到格式化后的数字，如从10%变为0.1
	 * 
	 * @param per
	 *            要转化的百分数，格式如20%
	 * @return double 返回转化后的数值，如0.2
	 */
	public static double getDoubleFromPercent(String per) {
		NumberFormat nFormat = NumberFormat.getPercentInstance(Locale.CHINA);

		try {
			Number n = nFormat.parse(per);

			return n.doubleValue();
		} catch (ParseException e) {
			return 0;
		}
	}

	/**
	 * 根据格式得到格式化的浮点型数字
	 * 
	 * @param data
	 *            要格式化的数字，类型为long
	 * @param type
	 *            格式类型，如#,###.00
	 * @return String 返回String类型的数值，形式如：23,345,445.34
	 */
	public static String formatDecimal(long data, String type) {
		DecimalFormat dFormat = new DecimalFormat(type);

		return dFormat.format(data);
	}

	/**
	 * 根据格式得到格式化的浮点型数字
	 * 
	 * @param data
	 *            要格式化的数字，类型为double
	 * @param type
	 *            格式类型，如#,###.00
	 * @return String 返回String类型的数值，形式如：23,345,445.34
	 */
	public static String formatDecimal(double data, String type) {
		DecimalFormat dFormat = new DecimalFormat(type);

		return dFormat.format(data);
	}

	/**
	 * 根据格式得到格式化的浮点型数字
	 * 
	 * @param data
	 *            要格式化的数字，类型为Object
	 * @param type
	 *            格式类型，如#,###.00
	 * @return String 返回String类型的数值，形式如：23,345,445.34
	 */
	public static String formatDecimal(Object data, String type) {
		DecimalFormat dFormat = new DecimalFormat(type);

		return dFormat.format(data);
	}

	/**
	 * 得到格式化的中国区域货币，如￥100,000.00
	 * 
	 * @param data
	 *            要格式化的数字，类型为long
	 * @return String 返回String类型的数值，形式如：￥100,000.00
	 */
	public static String formatCurrency(long data) {
		NumberFormat nFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);

		return nFormat.format(data);
	}

	/**
	 * 得到格式化的中国区域货币，如￥100,000.00
	 * 
	 * @param data
	 *            要格式化的数字，类型为dobule
	 * @return String 返回String类型的数值，形式如：￥100,000.00
	 */
	public static String formatCurrency(double data) {
		NumberFormat nFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);

		return nFormat.format(data);
	}

	/**
	 * 得到格式化的中国区域货币，如￥100,000.00
	 * 
	 * @param data
	 *            要格式化的数字，类型为Object
	 * @return String 返回String类型的数值，形式如：￥100,000.00
	 */
	public static String formatCurrency(Object data) {
		NumberFormat nFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);

		return nFormat.format(data);
	}

	/**
	 * 
	 * @param per
	 * @return
	 */
	public static String formatDecimal2Percent(BigDecimal per) {
		if (per == null)
			return "-";
		NumberFormat nFormat = NumberFormat.getPercentInstance(Locale.CHINA);
		nFormat.setMaximumFractionDigits(3);
		String n = nFormat.format(per);
		return n;
	}

	public static BigDecimal divide(Long long1, Long long2) {
		if (long1 == null || long2 == null) {
			return null;
		}
		if (long2.equals(0L)) {
			return null;
		}
		return new BigDecimal(long1 * 1.0d / long2);
	}

	public static void main(String[] args) {
		System.out.println(MoneyUtils.divide(11L, 266L));
		System.out.println(MoneyUtils.formatDecimal2Percent(MoneyUtils.divide(22L, 10000000L)));
		System.out.println(MoneyUtils.fixMoney(new BigDecimal(0.0001)));
		System.out.println(MoneyUtils.fixMoney(new BigDecimal(0.0001)).compareTo(new BigDecimal(0)));
		System.out.println(MoneyUtils.toChinese("10001"));
	}

}

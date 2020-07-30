package hitool.core.regexp;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Hashtable;

/**
 * <pre>
 * 身份证验证的工具（支持5位或18位省份证）
 * 身份证号码结构：
 * 17位数字和1位校验码：6位地址码数字，8位生日数字，3位出生时间顺序号，1位校验码。
 * 地址码（前6位）：表示对象常住户口所在县（市、镇、区）的行政区划代码，按GB/T2260的规定执行。
 * 出生日期码，（第七位 至十四位）：表示编码对象出生年、月、日，按GB按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
 * 顺序码（第十五位至十七位）：表示在同一地址码所标示的区域范围内，对同年、同月、同日出生的人编订的顺序号，
 * 顺序码的奇数分配给男性，偶数分配给女性。
 * 校验码（第十八位数）：
 * 十七位数字本体码加权求和公式 s = sum(Ai*Wi), i = 0,,16，先对前17位数字的权求和；  
 *  Ai:表示第i位置上的身份证号码数字值.Wi:表示第i位置上的加权因.Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2；
 * 计算模 Y = mod(S, 11)
 * 通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
 * </pre>
 */
public abstract class IDCardUtils {

	private final static Hashtable<String, String> areaCodeMap = new Hashtable<String, String>();
	private final static Hashtable<String, Integer> maxDateMap = new Hashtable<String, Integer>();
	
	static {

		//月份最大日期
		maxDateMap.put("01", 31);
		maxDateMap.put("02", null);
		maxDateMap.put("03", 31);
		maxDateMap.put("04", 30);
		maxDateMap.put("05", 31);
		maxDateMap.put("06", 30);
		maxDateMap.put("07", 31);
		maxDateMap.put("08", 31);
		maxDateMap.put("09", 30);
		maxDateMap.put("10", 31);
		maxDateMap.put("11", 30);
		maxDateMap.put("12", 31);
		
		// 地区码
		areaCodeMap.put("11", "北京");
		areaCodeMap.put("12", "天津");
		areaCodeMap.put("13", "河北");
		areaCodeMap.put("14", "山西");
		areaCodeMap.put("15", "内蒙古");
		areaCodeMap.put("21", "辽宁");
		areaCodeMap.put("22", "吉林");
		areaCodeMap.put("23", "黑龙江");
		areaCodeMap.put("31", "上海");
		areaCodeMap.put("32", "江苏");
		areaCodeMap.put("33", "浙江");
		areaCodeMap.put("34", "安徽");
		areaCodeMap.put("35", "福建");
		areaCodeMap.put("36", "江西");
		areaCodeMap.put("37", "山东");
		areaCodeMap.put("41", "河南");
		areaCodeMap.put("42", "湖北");
		areaCodeMap.put("43", "湖南");
		areaCodeMap.put("44", "广东");
		areaCodeMap.put("45", "广西");
		areaCodeMap.put("46", "海南");
		areaCodeMap.put("50", "重庆");
		areaCodeMap.put("51", "四川");
		areaCodeMap.put("52", "贵州");
		areaCodeMap.put("53", "云南");
		areaCodeMap.put("54", "西藏");
		areaCodeMap.put("61", "陕西");
		areaCodeMap.put("62", "甘肃");
		areaCodeMap.put("63", "青海");
		areaCodeMap.put("64", "宁夏");
		areaCodeMap.put("65", "新疆");
		areaCodeMap.put("71", "台湾");
		areaCodeMap.put("81", "香港");
		areaCodeMap.put("82", "澳门");
		areaCodeMap.put("91", "国外");

	}

	// wi = 2(n-1)(mod 11)
	private final static int[] wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
	// verify digit
	private final static String[] vi = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
	
	private static String _codeError;

	/** 验证身份证位数,15位和18位身份证  */
	public static boolean verifyLength(String certNo) {
		int length = certNo.length();
		if (length == 15 || length == 18) {
			return true;
		} else {
			 _codeError="错误：输入的身份证号码长度应该为15位或18位";
			return false;
		}
	}

	/** 验证身份证 地区码  */
	public static boolean verifyAreaCode(String certNo) {
		String areaCode = certNo.substring(0, 2);
		if (areaCodeMap.containsKey(areaCode)) {
			return true;
		} else {
			_codeError = "错误：输入的身份证号的地区码(1-2位)[" + areaCode + "]不符合中国行政区划分代码规定(GB/T2260-1999)";
			return false;
		}
	}
	
	/** 判断年份、月份、日期 */
	public static boolean verifyBirthdayCode(String certNo) {

		boolean isEighteenCode = (18 == certNo.length());
		// 校验年份
		String year = certNo.length() == 15 ? "19" + certNo.substring(6, 8) : certNo.substring(6, 10);
		int iyear = Integer.parseInt(year);
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		// 1900年的PASS，超过今年的PASS
		if (iyear < 1900 || iyear > curYear) {
			_codeError = "错误：输入的身份证号" + (isEighteenCode ? "(6-8位)" : "(6-10位)") + "年份小于 1900 或者 大于[" + curYear + "],不符合要求(GB/T7408)";
			return false;
		}
		
		// 验证月份
		String month = certNo.substring(10, 12);
		if (!maxDateMap.containsKey(month)) {
			_codeError = "错误：输入的身份证号" + (isEighteenCode ? "(11-12位)" : "(9-10位)") + "不存在[" + month + "]月份,不符合要求(GB/T7408)";
			return false;
		}
		// 验证日期
		String dayCode = certNo.substring(12, 14);
		Integer maxDay = maxDateMap.get(month);
		// 非2月的情况
		if (maxDay != null) {
			if (Integer.valueOf(dayCode) > maxDay || Integer.valueOf(dayCode) < 1) {
				_codeError = "错误：输入的身份证号" + (isEighteenCode ? "(13-14位)" : "(11-13位)") + "[" + dayCode + "]号不符合小月1-30天大月1-31天的规定(GB/T7408)";
				return false;
			}
		}
		// 2月的情况
		else {
			// 闰月的情况
			if ((iyear % 4 == 0 && iyear % 100 != 0) || (iyear % 400 == 0)) {
				if (Integer.valueOf(dayCode) > 29 || Integer.valueOf(dayCode) < 1) {
					_codeError = "错误：输入的身份证号" + (isEighteenCode ? "(13-14位)" : "(11-13位)") + "[" + dayCode + "]号在" + iyear + "闰年的情况下未符合1-29号的规定(GB/T7408)";
					return false;
				}
			}
			// 非闰月的情况
			else {
				if (Integer.valueOf(dayCode) > 28 || Integer.valueOf(dayCode) < 1) {
					_codeError = "错误：输入的身份证号" + (isEighteenCode ? "(13-14位)" : "(11-13位)") + "[" + dayCode + "]号在" + iyear + "平年的情况下未符合1-28号的规定(GB/T7408)";
					return false;
				}
			}
		}
		return true;
	}

	/** 验证身份除了最后位其他的是否包含字母 */
	public static boolean verifyNumber(String certNo) {
		String str = "";
		if (certNo.length() == 15) {
			str = certNo.substring(0, 15);
		} else if (certNo.length() == 18) {
			str = certNo.substring(0, 17);
		}
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (!(ch[i] >= '0' && ch[i] <= '9')) {
				_codeError = "错误：输入的身份证号第" + (i + 1) + "位包含字母";
				return false;
			}
		}
		return true;
	}
	
	/**  验证18位校验码,校验码采用ISO 7064：1983，MOD 11-2 校验码系统 */
	public static boolean verifyMOD(String certNo) {
		String verify = certNo.substring(17, 18);
		if ("x".equals(verify)) {
			certNo = certNo.replaceAll("x", "X");
			verify = "X";
		}
		int power = 0;
		final char[] char_arr = certNo.toUpperCase().toCharArray();
		for (int i = 0; i < 17; i++) {
			if (i < char_arr.length - 1) {
				power += (char_arr[i] - '0') * wi[i];
			}
		}
		if (verify.equals(getMod(certNo))) {
			return true;
		}
		_codeError = "错误：输入的身份证号最末尾的数字验证码错误";
		return false;
	}
	
	/**
	 * 
	 * 计算身份证校验码
	 * <pre>
	 * 	1、将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7－9－10－5－8－4－2－1－6－3－7－9－10－5－8－4－2。
	 * 	2、将这17位数字和系数相乘的结果相加。
	 *  3、用加出来和除以11，看余数是多少？
	 * 	4、余数只可能有0－1－2－3－4－5－6－7－8－9－10这11个数字。其分别对应的最后一位身份证的号码为1－0－X －9－8－7－6－5－4－3－2。
	 * 	5、通过上面得知如果余数是3，就会在身份证的第18位数字上出现的是9。如果对应的数字是10，身份证的最后一位号码就是罗马数字x。
	 * 
	 * 		例如：某男性的身份证号码为【53010219200508011x】， 我们看看这个身份证是不是合法的身份证。
	 * 		首先我们得出前17位的乘积和【(5*7)+(3*9)+(0*10)+(1*5)+(0*8)+(2*4)+(1*2)+(9*1)+(2*6)+(0*3)+(0*7)+(5*9)+(0*10)+(8*5)+(0*8)+(1*4)+(1*2)】是189，
	 *   	然后用189除以11得出的结果是189/11=17----2，也就是说其余数是2。最后通过对应规则就可以知道余数2对应的检验码是X。所以，可以判定这是一个正确的身份证号码。
	 *   
	 * 	</pre>
	 * @param certNo
	 * @return
	 */
	public static String getMod(String certNo) {
		int remaining = 0;
		if (certNo.length() == 18) {
			certNo = certNo.substring(0, 17);
		}
		if (certNo.length() == 17) {
			// 校验位数
			int power = 0;
			for (int i = 0; i < certNo.length(); i++) {
				power += Integer.parseInt( certNo.substring(i, i + 1)) * wi[i];
			}
			remaining = power % 11;
		}
		return remaining == 2 ? "X" : vi[remaining];
	}

	/** 15位转18位身份证 */
	public static String upToEighteen(String certNo) {
		if (certNo.length() == 15) {
			String 	newNo = certNo.substring(0, 6);
					newNo = newNo + "19";
					newNo = newNo + certNo.substring(6, 15);
					newNo = newNo + getMod(newNo);
			return newNo;
		}
		return certNo;
	}
	
	/**
	 * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
	 * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
	 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
	 * 3、出生日期码（第七位至十四位）表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
	 * 4、顺序码（第十五位至十七位）表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
	 * 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数） （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i =
	 * 0, ... , 16 ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10
	 * 5 8 4 2 1 6 3 7 9 10 5 8 4 * 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0
	 * 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 * X 9 8 7 6 5 4 3 2
	 */

	public static boolean isIDCard(String certNo) {
		_codeError = "";
		// 校验身份证不为空
		if (certNo == null ) {
			_codeError = "错误：输入的身份证号码为空";
			return false;
		}
		// 验证身份证位数,15位和18位身份证
		if (!verifyLength(certNo)) {
			return false;
		}
		// 验证身份除了最后位其他的是否包含字母
		if (!verifyNumber(certNo)) {
			return false;
		}
		// 验证身份证的地区码
		if (!verifyAreaCode(certNo)) {
			return false;
		}
		// 判断月份和日期
		if (!verifyBirthdayCode(certNo)) {
			return false;
		}
		// 如果是15位的就转成18位的身份证:验证18位校验码,校验码采用ISO 7064：1983，MOD 11-2 校验码系统
		if (!verifyMOD(upToEighteen(certNo))) {
			return false;
		}
		return true;
	}

	public static String getCodeError() {
		return _codeError;
	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		// String IDCardNum="210102820826411";
		// String IDCardNum="210102198208264114";
		String IDCardNum = "210102820826411";
		System.out.println(IDCardUtils.isIDCard(IDCardNum));
		// System.out.println(cc.isDate("1996-02-29"));
	}
	
}

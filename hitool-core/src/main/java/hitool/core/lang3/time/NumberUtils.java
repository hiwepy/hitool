/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.time;

public abstract class NumberUtils {
	
	  /***
     * <b>function:</b> 将数字转化为大写
     * @createDate 2010-5-27 上午10:28:12
     * @param num 数字
     * @return 转换后的大写数字
     */
    public static String numToUpper(int num) {
        // String u[] = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
        //String u[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String u[] = {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        char[] str = String.valueOf(num).toCharArray();
        String rstr = "";
        for (int i = 0; i < str.length; i++) {
            rstr = rstr + u[Integer.parseInt(str[i] + "")];
        }
        return rstr;
    }
    
    /***
	 * <b>function:</b> 日转化为大写
	 * 
	 * @createDate 2010-5-27 上午10:43:32
	 * @param day
	 *            日期
	 * @return 转换大写的日期格式
	 */
	public static String dayToUppder(int day) {
		if (day < 20) {
			return monthToUppder(day);
		} else {
			char[] str = String.valueOf(day).toCharArray();
			if (str[1] == '0') {
				return NumberUtils.numToUpper(Integer.parseInt(str[0] + "")) + "十";
			} else {
				return NumberUtils.numToUpper(Integer.parseInt(str[0] + "")) + "十" + NumberUtils.numToUpper(Integer.parseInt(str[1] + ""));
			}
		}
	}
	
	/***
	 * <b>function:</b> 月转化为大写
	 * 
	 * @createDate 2010-5-27 上午10:41:42
	 * @param month
	 *            月份
	 * @return 返回转换后大写月份
	 */
	public static String monthToUppder(int month) {
		if (month < 10) {
			return NumberUtils.numToUpper(month);
		} else if (month == 10) {
			return "十";
		} else {
			return "十" + NumberUtils.numToUpper(month - 10);
		}
	}
	
    public static void main(String[] args) {
		
    	System.out.println(numToUpper(2015));
    	/*
    	Calendar calendar = Calendar.getInstance();
    	try {
			calendar.setTime(DateFormat.getInstance().parse("2016/3/06"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK) - 1); ;*/
	}
	
}
